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
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
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
    private ThreadSafeSocketStream socketStream;
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;

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
        this.socketStream = new ThreadSafeSocketStream();

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
                    socketStream.close();

                    // signal that the GameIO is dead
                    isDying = false;
                    isAlive = false;
                }

                Log.i(TAG, "Done closing GameIO");
            }
        };
        new LoggedThread(TAG, r, "GameIO-KillThread").start();
    }

    /**
     * Checks to see if the GameIO is done.
     *
     * @return true if it is in the process of dying or if the the connection is closed
     */
    private synchronized boolean isDone() {
        return isDying || socketStream == null || !socketStream.isConnected();
    }

    /**
     * Receives the next message
     * @throws JSONException if the received message cannot be converted into JSON
     * @throws IOException if the input is unsuccessful
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    private void receiveNext() throws JSONException, IOException, InterruptedException {

        // wait for next client bound message
        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        while (!isDone() && bytesRead != -1) {
            bytesRead = socketStream.read(buffer);
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

    /**
     * Sends the next Message
     *
     * @throws IOException if the output is unsuccessful
     */
    private void sendNext() throws IOException {
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

    /**
     * Starts running the GameIO threads
     */
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

            socketStream.writeUTF(jsonObject.toString());
            socketStream.flush();
            Log.i(TAG, "Sent JsonObject: " + jsonObject.toString());

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

            if (socketStream == null) {
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

            socketStream.writeUTF(jsonObject.toString());
            socketStream.flush();
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

    private class ThreadSafeSocketStream implements Closeable {
        Object outLock, inLock;
        private Socket socket = null;
        private DataInputStream in = null;
        private DataOutputStream out = null;


        public ThreadSafeSocketStream() throws IOException {
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
        }

        /**
         * Closes this stream and releases any system resources associated
         * with it. If the stream is already closed then invoking this
         * method has no effect.
         *
         * <p> As noted in {@link AutoCloseable#close()}, cases where the
         * close may fail require careful attention. It is strongly advised
         * to relinquish the underlying resources and to internally
         * <em>mark</em> the {@code Closeable} as closed, prior to throwing
         * the {@code IOException}.
         *
         * @throws IOException if an I/O error occurs
         */
        @Override
        public synchronized void close() {
            try {
                synchronized (inLock) {
                    in.close();
                }
            } catch (Exception e) {
                // just continue
            }

            try {
                synchronized (inLock) {
                    out.close();
                }
            } catch (Exception e) {
                // just continue
            }

            try {
                synchronized (this) {
                    socket.close();
                }
            } catch (Exception e) {
                // just continue
            }

        }

        /**
         * Flushes this data output stream. This forces any buffered output
         * bytes to be written out to the stream.
         * <p>
         * The <code>flush</code> method of <code>DataOutputStream</code>
         * calls the <code>flush</code> method of its underlying output stream.
         *
         * @throws IOException if an I/O error occurs.
         * @see OutputStream#flush()
         */
        public void flush() throws IOException {
            synchronized (outLock) {
                out.flush();
            }
        }

        /**
         * Returns the connection state of the socket.
         * <p>
         * Note: Closing a socket doesn't clear its connection state, which means
         * this method will return {@code true} for a closed socket
         * (see ) if it was successfuly connected prior
         * to being closed.
         *
         * @return true if the socket was successfuly connected to a server
         * @since 1.4
         */
        public synchronized boolean isConnected() {
            return socket.isConnected();
        }

        /**
         * Reads some number of bytes from the contained input stream and
         * stores them into the buffer array <code>b</code>. The number of
         * bytes actually read is returned as an integer. This method blocks
         * until input data is available, end of file is detected, or an
         * exception is thrown.
         *
         * <p>If <code>b</code> is null, a <code>NullPointerException</code> is
         * thrown. If the length of <code>b</code> is zero, then no bytes are
         * read and <code>0</code> is returned; otherwise, there is an attempt
         * to read at least one byte. If no byte is available because the
         * stream is at end of file, the value <code>-1</code> is returned;
         * otherwise, at least one byte is read and stored into <code>b</code>.
         *
         * <p>The first byte read is stored into element <code>b[0]</code>, the
         * next one into <code>b[1]</code>, and so on. The number of bytes read
         * is, at most, equal to the length of <code>b</code>. Let <code>k</code>
         * be the number of bytes actually read; these bytes will be stored in
         * elements <code>b[0]</code> through <code>b[k-1]</code>, leaving
         * elements <code>b[k]</code> through <code>b[b.length-1]</code>
         * unaffected.
         *
         * <p>The <code>read(b)</code> method has the same effect as:
         * <blockquote><pre>
         * read(b, 0, b.length)
         * </pre></blockquote>
         *
         * @param b the buffer into which the data is read.
         * @return the total number of bytes read into the buffer, or
         * <code>-1</code> if there is no more data because the end
         * of the stream has been reached.
         * @throws IOException if the first byte cannot be read for any reason
         *                     other than end of file, the stream has been closed and the underlying
         *                     input stream does not support reading after close, or another I/O
         *                     error occurs.
         * @see InputStream#read(byte[], int, int)
         */
        public int read(byte[] b) throws IOException {
            synchronized (inLock) {
                return in.read(b);
            }
        }

        /**
         * Reads up to <code>len</code> bytes of data from the contained
         * input stream into an array of bytes.  An attempt is made to read
         * as many as <code>len</code> bytes, but a smaller number may be read,
         * possibly zero. The number of bytes actually read is returned as an
         * integer.
         *
         * <p> This method blocks until input data is available, end of file is
         * detected, or an exception is thrown.
         *
         * <p> If <code>len</code> is zero, then no bytes are read and
         * <code>0</code> is returned; otherwise, there is an attempt to read at
         * least one byte. If no byte is available because the stream is at end of
         * file, the value <code>-1</code> is returned; otherwise, at least one
         * byte is read and stored into <code>b</code>.
         *
         * <p> The first byte read is stored into element <code>b[off]</code>, the
         * next one into <code>b[off+1]</code>, and so on. The number of bytes read
         * is, at most, equal to <code>len</code>. Let <i>k</i> be the number of
         * bytes actually read; these bytes will be stored in elements
         * <code>b[off]</code> through <code>b[off+</code><i>k</i><code>-1]</code>,
         * leaving elements <code>b[off+</code><i>k</i><code>]</code> through
         * <code>b[off+len-1]</code> unaffected.
         *
         * <p> In every case, elements <code>b[0]</code> through
         * <code>b[off]</code> and elements <code>b[off+len]</code> through
         * <code>b[b.length-1]</code> are unaffected.
         *
         * @param b   the buffer into which the data is read.
         * @param off the start offset in the destination array <code>b</code>
         * @param len the maximum number of bytes read.
         * @return the total number of bytes read into the buffer, or
         * <code>-1</code> if there is no more data because the end
         * of the stream has been reached.
         * @throws NullPointerException      If <code>b</code> is <code>null</code>.
         * @throws IndexOutOfBoundsException If <code>off</code> is negative,
         *                                   <code>len</code> is negative, or <code>len</code> is greater than
         *                                   <code>b.length - off</code>
         * @throws IOException               if the first byte cannot be read for any reason
         *                                   other than end of file, the stream has been closed and the underlying
         *                                   input stream does not support reading after close, or another I/O
         *                                   error occurs.
         * @see InputStream#read(byte[], int, int)
         */
        public int read(byte[] b, int off, int len) throws IOException {
            synchronized (inLock) {
                return in.read(b, off, len);
            }
        }

        /**
         * See the general contract of the <code>readBoolean</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes for this operation are read from the contained
         * input stream.
         *
         * @return the <code>boolean</code> value read.
         * @throws EOFException if this input stream has reached the end.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public boolean readBoolean() throws IOException {
            synchronized (inLock) {
                return in.readBoolean();
            }
        }

        /**
         * See the general contract of the <code>readByte</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next byte of this input stream as a signed 8-bit
         * <code>byte</code>.
         * @throws EOFException if this input stream has reached the end.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public byte readByte() throws IOException {
            synchronized (inLock) {
                return in.readByte();
            }
        }

        /**
         * See the general contract of the <code>readChar</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next two bytes of this input stream, interpreted as a
         * <code>char</code>.
         * @throws EOFException if this input stream reaches the end before
         *                      reading two bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public char readChar() throws IOException {
            synchronized (inLock) {
                return in.readChar();
            }
        }

        /**
         * See the general contract of the <code>readDouble</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next eight bytes of this input stream, interpreted as a
         * <code>double</code>.
         * @throws EOFException if this input stream reaches the end before
         *                      reading eight bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         * @see DataInputStream#readLong()
         * @see Double#longBitsToDouble(long)
         */
        public double readDouble() throws IOException {
            synchronized (inLock) {
                return in.readDouble();
            }
        }

        /**
         * See the general contract of the <code>readFloat</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next four bytes of this input stream, interpreted as a
         * <code>float</code>.
         * @throws EOFException if this input stream reaches the end before
         *                      reading four bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         * @see DataInputStream#readInt()
         * @see Float#intBitsToFloat(int)
         */
        public float readFloat() throws IOException {
            synchronized (inLock) {
                return in.readFloat();
            }
        }

        /**
         * See the general contract of the <code>readFully</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @param b the buffer into which the data is read.
         * @throws EOFException if this input stream reaches the end before
         *                      reading all the bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public void readFully(byte[] b) throws IOException {
            synchronized (inLock) {
                in.readFully(b);
            }
        }

        /**
         * See the general contract of the <code>readFully</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @param b   the buffer into which the data is read.
         * @param off the start offset of the data.
         * @param len the number of bytes to read.
         * @throws EOFException if this input stream reaches the end before
         *                      reading all the bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public void readFully(byte[] b, int off, int len) throws IOException {
            synchronized (inLock) {
                in.readFully(b, off, len);
            }
        }

        /**
         * See the general contract of the <code>readInt</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next four bytes of this input stream, interpreted as an
         * <code>int</code>.
         * @throws EOFException if this input stream reaches the end before
         *                      reading four bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public int readInt() throws IOException {
            synchronized (inLock) {
                return in.readInt();
            }
        }

        /**
         * See the general contract of the <code>readLong</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next eight bytes of this input stream, interpreted as a
         * <code>long</code>.
         * @throws EOFException if this input stream reaches the end before
         *                      reading eight bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public long readLong() throws IOException {
            synchronized (inLock) {
                return in.readLong();
            }
        }

        /**
         * See the general contract of the <code>readShort</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next two bytes of this input stream, interpreted as a
         * signed 16-bit number.
         * @throws EOFException if this input stream reaches the end before
         *                      reading two bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public short readShort() throws IOException {
            synchronized (inLock) {
                return in.readShort();
            }
        }

        /**
         * See the general contract of the <code>readUTF</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return a Unicode string.
         * @throws EOFException           if this input stream reaches the end before
         *                                reading all the bytes.
         * @throws IOException            the stream has been closed and the contained
         *                                input stream does not support reading after close, or
         *                                another I/O error occurs.
         * @throws UTFDataFormatException if the bytes do not represent a valid
         *                                modified UTF-8 encoding of a string.
         * @see DataInputStream#readUTF(DataInput)
         */
        public String readUTF() throws IOException {
            synchronized (inLock) {
                return in.readUTF();
            }
        }

        /**
         * See the general contract of the <code>readUnsignedByte</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next byte of this input stream, interpreted as an
         * unsigned 8-bit number.
         * @throws EOFException if this input stream has reached the end.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public int readUnsignedByte() throws IOException {
            synchronized (inLock) {
                return in.readUnsignedByte();
            }
        }

        /**
         * See the general contract of the <code>readUnsignedShort</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes
         * for this operation are read from the contained
         * input stream.
         *
         * @return the next two bytes of this input stream, interpreted as an
         * unsigned 16-bit integer.
         * @throws EOFException if this input stream reaches the end before
         *                      reading two bytes.
         * @throws IOException  the stream has been closed and the contained
         *                      input stream does not support reading after close, or
         *                      another I/O error occurs.
         */
        public int readUnsignedShort() throws IOException {
            synchronized (inLock) {
                return in.readUnsignedShort();
            }
        }

        /**
         * Returns the current value of the counter <code>written</code>,
         * the number of bytes written to this data output stream so far.
         * If the counter overflows, it will be wrapped to Integer.MAX_VALUE.
         *
         * @return the value of the <code>written</code> field.
         */
        public int size() {
            synchronized (outLock) {
                return out.size();
            }
        }

        /**
         * See the general contract of the <code>skipBytes</code>
         * method of <code>DataInput</code>.
         * <p>
         * Bytes for this operation are read from the contained
         * input stream.
         *
         * @param n the number of bytes to be skipped.
         * @return the actual number of bytes skipped.
         * @throws IOException if the contained input stream does not support
         *                     seek, or the stream has been closed and
         *                     the contained input stream does not support
         *                     reading after close, or another I/O error occurs.
         */
        public int skipBytes(int n) throws IOException {
            synchronized (inLock) {
                return in.skipBytes(n);
            }
        }

        /**
         * Writes the specified byte (the low eight bits of the argument
         * <code>b</code>) to the underlying output stream. If no exception
         * is thrown, the counter <code>written</code> is incremented by
         * <code>1</code>.
         * <p>
         * Implements the <code>write</code> method of <code>OutputStream</code>.
         *
         * @param b the <code>byte</code> to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void write(int b) throws IOException {
            synchronized (outLock) {
                out.write(b);
            }
        }

        /**
         * Writes <code>len</code> bytes from the specified byte array
         * starting at offset <code>off</code> to the underlying output stream.
         * If no exception is thrown, the counter <code>written</code> is
         * incremented by <code>len</code>.
         *
         * @param b   the data.
         * @param off the start offset in the data.
         * @param len the number of bytes to write.
         * @throws IOException if an I/O error occurs.
         */
        public void write(byte[] b, int off, int len) throws IOException {
            synchronized (outLock) {
                out.write(b, off, len);
            }
        }

        /**
         * Writes a <code>boolean</code> to the underlying output stream as
         * a 1-byte value. The value <code>true</code> is written out as the
         * value <code>(byte)1</code>; the value <code>false</code> is
         * written out as the value <code>(byte)0</code>. If no exception is
         * thrown, the counter <code>written</code> is incremented by
         * <code>1</code>.
         *
         * @param v a <code>boolean</code> value to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeBoolean(boolean v) throws IOException {
            synchronized (outLock) {
                out.writeBoolean(v);
            }
        }

        /**
         * Writes out a <code>byte</code> to the underlying output stream as
         * a 1-byte value. If no exception is thrown, the counter
         * <code>written</code> is incremented by <code>1</code>.
         *
         * @param v a <code>byte</code> value to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeByte(int v) throws IOException {
            synchronized (outLock) {
                out.writeByte(v);
            }
        }

        /**
         * Writes out the string to the underlying output stream as a
         * sequence of bytes. Each character in the string is written out, in
         * sequence, by discarding its high eight bits. If no exception is
         * thrown, the counter <code>written</code> is incremented by the
         * length of <code>s</code>.
         *
         * @param s a string of bytes to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeBytes(String s) throws IOException {
            synchronized (outLock) {
                out.writeBytes(s);
            }
        }

        /**
         * Writes a <code>char</code> to the underlying output stream as a
         * 2-byte value, high byte first. If no exception is thrown, the
         * counter <code>written</code> is incremented by <code>2</code>.
         *
         * @param v a <code>char</code> value to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeChar(int v) throws IOException {
            synchronized (outLock) {
                out.writeChar(v);
            }
        }

        /**
         * Writes a string to the underlying output stream as a sequence of
         * characters. Each character is written to the data output stream as
         * if by the <code>writeChar</code> method. If no exception is
         * thrown, the counter <code>written</code> is incremented by twice
         * the length of <code>s</code>.
         *
         * @param s a <code>String</code> value to be written.
         * @throws IOException if an I/O error occurs.
         * @see DataOutputStream#writeChar(int)
         */
        public void writeChars(String s) throws IOException {
            synchronized (outLock) {
                out.writeChars(s);
            }
        }

        /**
         * Converts the double argument to a <code>long</code> using the
         * <code>doubleToLongBits</code> method in class <code>Double</code>,
         * and then writes that <code>long</code> value to the underlying
         * output stream as an 8-byte quantity, high byte first. If no
         * exception is thrown, the counter <code>written</code> is
         * incremented by <code>8</code>.
         *
         * @param v a <code>double</code> value to be written.
         * @throws IOException if an I/O error occurs.
         * @see Double#doubleToLongBits(double)
         */
        public void writeDouble(double v) throws IOException {
            synchronized (outLock) {
                out.writeDouble(v);
            }
        }

        /**
         * Converts the float argument to an <code>int</code> using the
         * <code>floatToIntBits</code> method in class <code>Float</code>,
         * and then writes that <code>int</code> value to the underlying
         * output stream as a 4-byte quantity, high byte first. If no
         * exception is thrown, the counter <code>written</code> is
         * incremented by <code>4</code>.
         *
         * @param v a <code>float</code> value to be written.
         * @throws IOException if an I/O error occurs.
         * @see Float#floatToIntBits(float)
         */
        public void writeFloat(float v) throws IOException {
            synchronized (outLock) {
                out.writeFloat(v);
            }
        }

        /**
         * Writes an <code>int</code> to the underlying output stream as four
         * bytes, high byte first. If no exception is thrown, the counter
         * <code>written</code> is incremented by <code>4</code>.
         *
         * @param v an <code>int</code> to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeInt(int v) throws IOException {
            synchronized (outLock) {
                out.writeInt(v);
            }
        }

        /**
         * Writes a <code>long</code> to the underlying output stream as eight
         * bytes, high byte first. In no exception is thrown, the counter
         * <code>written</code> is incremented by <code>8</code>.
         *
         * @param v a <code>long</code> to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeLong(long v) throws IOException {
            synchronized (outLock) {
                out.writeLong(v);
            }
        }

        /**
         * Writes a <code>short</code> to the underlying output stream as two
         * bytes, high byte first. If no exception is thrown, the counter
         * <code>written</code> is incremented by <code>2</code>.
         *
         * @param v a <code>short</code> to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeShort(int v) throws IOException {
            synchronized (outLock) {
                out.writeShort(v);
            }
        }

        /**
         * Writes a string to the underlying output stream using
         * <a href="DataInput.html#modified-utf-8">modified UTF-8</a>
         * encoding in a machine-independent manner.
         * <p>
         * First, two bytes are written to the output stream as if by the
         * <code>writeShort</code> method giving the number of bytes to
         * follow. This value is the number of bytes actually written out,
         * not the length of the string. Following the length, each character
         * of the string is output, in sequence, using the modified UTF-8 encoding
         * for the character. If no exception is thrown, the counter
         * <code>written</code> is incremented by the total number of
         * bytes written to the output stream. This will be at least two
         * plus the length of <code>str</code>, and at most two plus
         * thrice the length of <code>str</code>.
         *
         * @param str a string to be written.
         * @throws IOException if an I/O error occurs.
         */
        public void writeUTF(String str) throws IOException {
            synchronized (outLock) {
                out.writeUTF(str);
            }
        }

    }
}
