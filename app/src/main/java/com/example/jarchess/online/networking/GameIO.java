package com.example.jarchess.online.networking;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.jaraccount.JarAccount;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.result.AgreedUponDrawResult;
import com.example.jarchess.match.result.CheckmateResult;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.result.FlagFallResult;
import com.example.jarchess.match.result.ResignationResult;
import com.example.jarchess.match.result.ServerErrorResult;
import com.example.jarchess.match.result.StalemateDrawResult;
import com.example.jarchess.match.result.WinResult;
import com.example.jarchess.online.datapackage.Datapackage;
import com.example.jarchess.online.move.DatapackageQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A self running Input/Output system that allows for asynchronous input and output.
 */
public class GameIO implements Closeable {

    /**
     * The timeout constant of 300000 which represents 5 minutes
     */
    public static final int TIMEOUT = 300000;
    public static final String SUCCESS_RESPONSE = "success";
    public static final String FAILURE_RESPONSE = "failure";
    private static final String PAYLOAD_KEY = "payload";
    private static final String MATCH_COMMUNICATION = "MatchCommunication";
    private static final String ERROR_REPORT = "ErrorReport";
    private static final String MATCH_RESULT_REPORT = "MatchResultReport";
    private static final String TYPE_KEY = "type";
    private static final Datapackage.DatapackageJSONConverter JSON_TO_DATAPACKAGE_CONVERTER = Datapackage.DatapackageJSONConverter.getInstance();
    private static final String CLIENT_RESPONSE = "clientResponse";
    public static final String RESULT_TYPE = "resultType";
    public static final String WINNER = "winner";
    private final ServerResponseQueue serverResponseQueue = new ServerResponseQueue();
    private final String TAG = "GameIO";
    private final LoggedThread senderThread;
    private final LoggedThread receiverThread;
    private boolean isAlive = false;
    private boolean isDying = false;
    private String gameToken;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private String serverIp = "3.18.79.149";
    private int serverPort = 12345;
    private DatapackageQueue datapackageQueue;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;
    private Closeable[] closeables;
    /**
     * Creates a GameIO object, which will connect to the server and begin sending and receiving available messages.
     *
     * @param datapackageQueue         the datapackage input and output queueing object.
     * @param gameToken                the token representing the game this GameIO is being used for
     * @param remoteOpponentInfoBundle the opponent's information
     */
    public GameIO(DatapackageQueue datapackageQueue, String gameToken,
                  RemoteOpponentInfoBundle remoteOpponentInfoBundle) throws IOException {

        boolean canStart = false;
        this.datapackageQueue = datapackageQueue;
        this.remoteOpponentInfoBundle = remoteOpponentInfoBundle;
        this.gameToken = gameToken;
        try {

            // set up socket and data streams
            this.socket = new Socket(serverIp, serverPort);
            this.socket.setSoTimeout(TIMEOUT);

            this.in = new DataInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()));

            this.out = new DataOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()));

            canStart = true;

        } catch (IOException e) {

            Log.e(TAG, e.getLocalizedMessage());

            //close everything we need to close and rethrow the exception
            try {
                in.close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                out.close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                socket.close();
            } catch (Exception ex) {
                // just continue;
            }

            throw e;
        }

        receiverThread = new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                while (!isDone()) {
                    try {
                        receiveNext();
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        // We log the error message and continue
                    } catch (JSONException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        // We log the error message and continue
                    } catch (InterruptedException e) {
                        close();
                    }
                }
                synchronized (GameIO.this) {
                    if (!isDying && isAlive) {
                        close();
                    }
                }
            }
        }, "GameIO-Receiver-Thread");

        senderThread = new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                while (!isDone()) {
                    try {
                        sendNext();
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        // We log the error message and continue
                    } catch (JSONException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        // We log the error message and continue
                    }
                }
                synchronized (GameIO.this) {
                    if (!isDying && isAlive) {
                        close();
                    }
                }
            }
        }, "GameIO-Sender-Thread");

        if (canStart) {
            closeables = new Closeable[]{
                    in,
                    out,
                    socket};
            this.start();
        }
    }

    /**
     * Close the GameIO object, terminating the connection with the server and stops sending and receiving messages.
     * <p>
     * This is a non-blocking call, and it is possible that messages will be sent or received shortly
     * after this method is called.
     */
    public void close() {
        Log.i(TAG, "closing GameIO");

        Runnable r = new Runnable() {
            @Override
            public void run() {

                // signal that the GameIO is dying
                synchronized (GameIO.this) {
                    isDying = true;
                }

                try { // wait for the receiver thread to end
                    if (receiverThread != null && receiverThread.isAlive()) {
                        receiverThread.join();
                    }
                } catch (InterruptedException e) {
                    // just continue
                }
                try {// wait for the sender thread to end
                    if (senderThread != null && senderThread.isAlive()) {
                        senderThread.join();
                    }
                } catch (InterruptedException e) {
                    // just continue
                }
                synchronized (GameIO.this) {

                    // close all the stuff we need to close
                    for (Closeable c : closeables) {
                        try {
                            c.close();
                        } catch (IOException e) {
                            // just keep moving along
                        }
                    }

                    // signal that the GameIO is dead
                    isDying = false;
                    isAlive = false;
                }

                Log.i(TAG, "Done closing GameIO");
            }
        };
        new LoggedThread(TAG, r, "GameIO-KillThread").start();
    }

    private synchronized boolean isDone() {
        return isDying || socket == null || !socket.isConnected();
    }

    private void receiveNext() throws JSONException, IOException, InterruptedException {

        // wait for next client bound message
        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        while (!isDone() && bytesRead != -1) {
            bytesRead = in.read(buffer);
            msgBuilder.append(new String(buffer));
        }
        String msg = msgBuilder.toString().trim();
        if (msg.isEmpty()) {
            return;// We don't have a message to respond to
        }
        Log.i(TAG, "msg: " + msg);
        JSONObject messageObject = new JSONObject(msg);

        // check if the received message is a response and add to queue if so
        if (ServerResponse.isServerResponse(messageObject)) {
            serverResponseQueue.put(new ServerResponse(messageObject));
            return; // we so not respond to server responses.
        }

        // make sure the received json object has a type field
        if (!messageObject.has(TYPE_KEY)) {

            // if it doesn't we send failure response
            new FailureResponse(messageObject).send();
            return;
        }

        // handle the client side message, and send response to server
        switch (messageObject.getString(TYPE_KEY)) {
            case MATCH_COMMUNICATION:

                // load the extracted payload into the queue
                JSONObject payload = messageObject.getJSONObject(PAYLOAD_KEY);
                datapackageQueue.insertClientBoundDatapackageQueue(
                        JSON_TO_DATAPACKAGE_CONVERTER.convertFromJSONObject(payload));
                Log.i(TAG, "inserted in to Client Bound Datapackage Queue");

                // send success response
                new SuccessResponse(messageObject).send();
                break;

            case MATCH_RESULT_REPORT:
                ChessMatchResult result;
                switch (messageObject.getString(RESULT_TYPE)) {

                    case "CHECKMATE":
                        result = new CheckmateResult(ChessColor.valueOf(messageObject.getString(WINNER)));
                        break;
                    case "RESIGNATION":
                        result = new ResignationResult(ChessColor.valueOf(messageObject.getString(WINNER)));
                        break;
                    case "FLAG_FALL":
                        result = new FlagFallResult(ChessColor.valueOf(messageObject.getString(WINNER)));
                        break;
                    case "AGREED_UPON_DRAW":
                        result = new AgreedUponDrawResult();
                        break;
                    case "REPETITION_RULE_DRAW":
                        throw new Error("Server should not send REPETITION_RULE_DRAW Match Results");
                    case "STALEMATE_DRAW":
                        result = new StalemateDrawResult();
                        break;
                    case "X_MOVE_RULE_DRAW":
                        throw new Error("Server should not send X_MOVE_RULE_DRAW Match Results");
                    case "ERROR":
                        result = new ServerErrorResult();
                }
                break;

            default:
                // send generic failure response
                new FailureResponse(messageObject).send();
                Log.i(TAG, "unexpected message type received: " + messageObject.getString(TYPE_KEY));

        }
    }

    private void sendNext() throws JSONException, IOException {
        Datapackage datapackage = datapackageQueue.getServerBoundDatapackage();

        switch (datapackage.getDatapackageType()) {

            case TURN:
            case PAUSE_REQUEST:
            case PAUSE_ACCEPT:
            case PAUSE_REJECT:
            case DRAW_REQUEST:
            case DRAW_ACCEPT:
            case DRAW_REJECT:
            case RESUME_REQUEST:
                new MatchCommunicationRequest(datapackage).send();
                break;
            case ERROR:
                new ErrorReportRequest(datapackage.getErrorMsg()).send();
                break;
            case MATCH_RESULT:
                new MatchResultReportRequest(datapackage.getMatchResult()).send();
                break;
        }
    }

    private void start() {

        Log.i(TAG, "starting GameIO");
        synchronized (GameIO.this) {

            if (isDying || isAlive) {
                Log.i(TAG, "GameIO can't start because it is " + (isDying ? "dying" : "already alive"));
                return; // without starting anything.
            }

            isAlive = true;
            receiverThread.start();
            senderThread.start();
        }
    }

    private static class ServerResponse {
        public ServerResponse(JSONObject jsonObject) {
            // TODO make the server response from the jsonObject
        }

        public static boolean isServerResponse(Object o) {
            return false; //FIXME
        }
    }

    private static class ServerResponseQueue {
        java.util.concurrent.LinkedBlockingQueue<ServerResponse> queue = new java.util.concurrent.LinkedBlockingQueue<>();

        /**
         * Atomically removes all of the elements from this queue.
         * The queue will be empty after this call returns.
         */
        public void clear() {
            queue.clear();
        }

        /**
         * Returns {@code true} if this queue contains the specified element.
         * More formally, returns {@code true} if and only if this queue contains
         * at least one element {@code e} such that {@code o.equals(e)}.
         *
         * @param o object to be checked for containment in this queue
         * @return {@code true} if this queue contains the specified element
         */
        public boolean contains(Object o) {
            return queue.contains(o);
        }

        /**
         * @param c
         * @throws UnsupportedOperationException {@inheritDoc}
         * @throws ClassCastException            {@inheritDoc}
         * @throws NullPointerException          {@inheritDoc}
         * @throws IllegalArgumentException      {@inheritDoc}
         */
        public int drainTo(Collection<? super ServerResponse> c) {
            return queue.drainTo(c);
        }

        /**
         * @param c
         * @param maxElements
         * @throws UnsupportedOperationException {@inheritDoc}
         * @throws ClassCastException            {@inheritDoc}
         * @throws NullPointerException          {@inheritDoc}
         * @throws IllegalArgumentException      {@inheritDoc}
         */
        public int drainTo(Collection<? super ServerResponse> c, int maxElements) {
            return queue.drainTo(c, maxElements);
        }

        /**
         * Returns an iterator over the elements in this queue in proper sequence.
         * The elements will be returned in order from first (head) to last (tail).
         *
         * <p>The returned iterator is
         * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
         *
         * @return an iterator over the elements in this queue in proper sequence
         */
        public Iterator<ServerResponse> iterator() {
            return queue.iterator();
        }

        /**
         * Inserts the specified element at the tail of this queue, waiting if
         * necessary up to the specified wait time for space to become available.
         *
         * @param serverResponse
         * @param timeout
         * @param unit
         * @return {@code true} if successful, or {@code false} if
         * the specified waiting time elapses before space is available
         * @throws InterruptedException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        public boolean offer(ServerResponse serverResponse, long timeout, TimeUnit unit) throws InterruptedException {
            return queue.offer(serverResponse, timeout, unit);
        }

        /**
         * Inserts the specified element at the tail of this queue if it is
         * possible to do so immediately without exceeding the queue's capacity,
         * returning {@code true} upon success and {@code false} if this queue
         * is full.
         * When using a capacity-restricted queue, this method is generally
         * preferable to method {@link BlockingQueue#add add}, which can fail to
         * insert an element only by throwing an exception.
         *
         * @param serverResponse
         * @throws NullPointerException if the specified element is null
         */
        public boolean offer(ServerResponse serverResponse) {
            return queue.offer(serverResponse);
        }

        public ServerResponse peek() {
            return queue.peek();
        }

        public ServerResponse poll(long timeout, TimeUnit unit) throws InterruptedException {
            return queue.poll(timeout, unit);
        }

        public ServerResponse poll() {
            return queue.poll();
        }

        /**
         * Inserts the specified element at the tail of this queue, waiting if
         * necessary for space to become available.
         *
         * @param serverResponse
         * @throws InterruptedException {@inheritDoc}
         * @throws NullPointerException {@inheritDoc}
         */
        public void put(ServerResponse serverResponse) throws InterruptedException {
            queue.put(serverResponse);
        }

        /**
         * Returns the number of additional elements that this queue can ideally
         * (in the absence of memory or resource constraints) accept without
         * blocking. This is always equal to the initial capacity of this queue
         * less the current {@code size} of this queue.
         *
         * <p>Note that you <em>cannot</em> always tell if an attempt to insert
         * an element will succeed by inspecting {@code remainingCapacity}
         * because it may be the case that another thread is about to
         * insert or remove an element.
         */
        public int remainingCapacity() {
            return queue.remainingCapacity();
        }

        /**
         * Removes a single instance of the specified element from this queue,
         * if it is present.  More formally, removes an element {@code e} such
         * that {@code o.equals(e)}, if this queue contains one or more such
         * elements.
         * Returns {@code true} if this queue contained the specified element
         * (or equivalently, if this queue changed as a result of the call).
         *
         * @param o element to be removed from this queue, if present
         * @return {@code true} if this queue changed as a result of the call
         */
        public boolean remove(Object o) {
            return queue.remove(o);
        }

        /**
         * Returns the number of elements in this queue.
         *
         * @return the number of elements in this queue
         */
        public int size() {
            return queue.size();
        }

        public ServerResponse take() throws InterruptedException {
            return queue.take();
        }

        /**
         * Returns an array containing all of the elements in this queue, in
         * proper sequence.
         *
         * <p>The returned array will be "safe" in that no references to it are
         * maintained by this queue.  (In other words, this method must allocate
         * a new array).  The caller is thus free to modify the returned array.
         *
         * <p>This method acts as bridge between array-based and collection-based
         * APIs.
         *
         * @return an array containing all of the elements in this queue
         */
        public Object[] toArray() {
            return queue.toArray();
        }

        /**
         * Returns an array containing all of the elements in this queue, in
         * proper sequence; the runtime type of the returned array is that of
         * the specified array.  If the queue fits in the specified array, it
         * is returned therein.  Otherwise, a new array is allocated with the
         * runtime type of the specified array and the size of this queue.
         *
         * <p>If this queue fits in the specified array with room to spare
         * (i.e., the array has more elements than this queue), the element in
         * the array immediately following the end of the queue is set to
         * {@code null}.
         *
         * <p>Like the {@link #toArray()} method, this method acts as bridge between
         * array-based and collection-based APIs.  Further, this method allows
         * precise control over the runtime type of the output array, and may,
         * under certain circumstances, be used to save allocation costs.
         *
         * <p>Suppose {@code x} is a queue known to contain only strings.
         * The following code can be used to dump the queue into a newly
         * allocated array of {@code String}:
         *
         * <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
         * <p>
         * Note that {@code toArray(new Object[0])} is identical in function to
         * {@code toArray()}.
         *
         * @param a the array into which the elements of the queue are to
         *          be stored, if it is big enough; otherwise, a new array of the
         *          same runtime type is allocated for this purpose
         * @return an array containing all of the elements in this queue
         * @throws ArrayStoreException  if the runtime type of the specified array
         *                              is not a supertype of the runtime type of every element in
         *                              this queue
         * @throws NullPointerException if the specified array is null
         */
        public <T> T[] toArray(T[] a) {
            return queue.toArray(a);
        }

        @NonNull
        @Override
        public String toString() {
            return queue.toString();
        }
    }

    private abstract class Request {

        protected final JSONObject jsonObject = new JSONObject();
        protected int bytesRead;

        protected Request(String requestType) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("requestType", requestType);
                jsonObject.put("username", JarAccount.getInstance().getName());
                jsonObject.put("game_token", gameToken);
                jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
            } catch (JSONException e) {
                throw new Error(e);
            }
        }

        public ServerResponse send() throws IOException {

            out.writeUTF(jsonObject.toString());
            out.flush();
            Log.i(TAG, "Sent JsonObject: " + jsonObject.toString());
            Log.i(TAG, String.valueOf(socket));

            return serverResponseQueue.poll();
        }
    }

    private class MatchCommunicationRequest extends Request {

        public static final String REQUEST_TYPE = "MatchCommunication";

        public MatchCommunicationRequest(Datapackage datapackage) {
            super(REQUEST_TYPE);
            try {
                super.jsonObject.put("payload", datapackage.getJSONObject());
            } catch (JSONException e) {
                throw new Error(e);
            }
        }
    }

    private class MatchResultReportRequest extends Request {

        public static final String REQUEST_TYPE = "MatchResultReport";

        public MatchResultReportRequest(ChessMatchResult result) {
            super(REQUEST_TYPE);

            try {
                String resultType = result.getType().toString();
                jsonObject.put(RESULT_TYPE, resultType);

                if (result instanceof WinResult) {
                    jsonObject.put("winner", ((WinResult) result).getWinnerColor().toString());
                }
            } catch (JSONException e) {
                throw new Error(e);
            }
        }
    }

    private class ErrorReportRequest extends Request {

        public static final String REQUEST_TYPE = "ErrorResultReport";

        public ErrorReportRequest(String errorMsg) {
            super(REQUEST_TYPE);

            try {
                jsonObject.put("error_msg", errorMsg);
            } catch (JSONException e) {
                throw new Error(e);
            }
        }
    }

    private abstract class Response {

        private final String msg;
        private final JSONObject originalMessage;

        public Response(JSONObject originalMessage, String responseMsg) {
            this.originalMessage = originalMessage;
            this.msg = responseMsg;
        }

        public void send() throws IOException {

            if (out == null) {
                return;
            }

            if (msg == null) {
                throw new IllegalArgumentException("sendResponse must have a non-null message argument");
            }

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("requestType", CLIENT_RESPONSE);
                jsonObject.put("username", JarAccount.getInstance().getName());
                jsonObject.put("game_token", gameToken);
                jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
                jsonObject.put("originalMessage", originalMessage);
                jsonObject.put("response", msg);
            } catch (JSONException e) {
                Log.e(TAG, "sendResponse exception: ", e);
                throw new Error(e); // this should not happen
            }

            out.writeUTF(jsonObject.toString());
            out.flush();
        }
    }

    private class SuccessResponse extends Response {

        public SuccessResponse(JSONObject originalMessage) {
            super(originalMessage, SUCCESS_RESPONSE);
        }
    }

    private class FailureResponse extends Response {

        public FailureResponse(JSONObject originalMessage) {
            super(originalMessage, FAILURE_RESPONSE);
        }
    }
}
