package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.jaraccount.JarAccount;
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
    private static final String TYPE_KEY = "type";
    private static final Datapackage.DatapackageJSONConverter JSON_TO_DATAPACKAGE_CONVERTER = Datapackage.DatapackageJSONConverter.getInstance();
    private static final int SERVER_BOUND_MESSAGES = 0;
    private static final int CLIENT_BOUND_MESSAGES = 1;
    private static final String CLIENT_RESPONSE = "clientResponse";
    private static final String ERROR_REPORT = "errorReport";
    private final String TAG = "GameIO";
    private final LoggedThread senderThread;
    private final LoggedThread receiverThread;
    private boolean isAlive = false;
    private boolean isDying = false;
    private String gameToken;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private String serverIp = "3.18.79.149";
    private int[] serverPort = {12345, 12346};
    private DatapackageQueue datapackageQueue;
    private Socket[] socket;
    private DataInputStream[] in;
    private DataOutputStream[] out;
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

            // set up server bound socket and data streams
            this.socket[SERVER_BOUND_MESSAGES] = new Socket(serverIp, serverPort[SERVER_BOUND_MESSAGES]);
            this.socket[SERVER_BOUND_MESSAGES].setSoTimeout(TIMEOUT);

            this.in[SERVER_BOUND_MESSAGES] = new DataInputStream(
                    new BufferedInputStream(
                            socket[SERVER_BOUND_MESSAGES].getInputStream()));

            this.out[SERVER_BOUND_MESSAGES] = new DataOutputStream(
                    new BufferedOutputStream(
                            socket[SERVER_BOUND_MESSAGES].getOutputStream()));

            // set up client bound socket and data streams
            this.socket[CLIENT_BOUND_MESSAGES] = new Socket(serverIp, serverPort[CLIENT_BOUND_MESSAGES]);
            this.socket[CLIENT_BOUND_MESSAGES].setSoTimeout(TIMEOUT);

            this.in[CLIENT_BOUND_MESSAGES] = new DataInputStream(
                    new BufferedInputStream(
                            socket[CLIENT_BOUND_MESSAGES].getInputStream()));

            this.out[CLIENT_BOUND_MESSAGES] = new DataOutputStream(
                    new BufferedOutputStream(
                            socket[CLIENT_BOUND_MESSAGES].getOutputStream()));

            canStart = true;

        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());

            // close all server bound closeables
            try {
                in[SERVER_BOUND_MESSAGES].close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                out[SERVER_BOUND_MESSAGES].close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                socket[SERVER_BOUND_MESSAGES].close();
            } catch (Exception ex) {
                // just continue;
            }

            // close all client bound closeables
            try {
                in[CLIENT_BOUND_MESSAGES].close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                out[CLIENT_BOUND_MESSAGES].close();
            } catch (Exception ex) {
                // just continue
            }
            try {
                socket[CLIENT_BOUND_MESSAGES].close();
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
                    in[SERVER_BOUND_MESSAGES],
                    out[SERVER_BOUND_MESSAGES],
                    socket[SERVER_BOUND_MESSAGES],
                    in[CLIENT_BOUND_MESSAGES],
                    out[CLIENT_BOUND_MESSAGES],
                    socket[CLIENT_BOUND_MESSAGES]};
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
        return isDying || socket[SERVER_BOUND_MESSAGES] == null || !socket[SERVER_BOUND_MESSAGES].isConnected() || socket[CLIENT_BOUND_MESSAGES] == null || !socket[CLIENT_BOUND_MESSAGES].isConnected();
    }

    private void receiveNext() throws JSONException, IOException {

        // wait for next client bound message
        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        while (!isDone() && bytesRead != -1) {
            bytesRead = in[CLIENT_BOUND_MESSAGES].read(buffer);
            msgBuilder.append(new String(buffer));
        }
        String msg = msgBuilder.toString().trim();
        if (msg.isEmpty()) {
            sendResponse(out[CLIENT_BOUND_MESSAGES], FAILURE_RESPONSE);
            return;
        }
        Log.i(TAG, "msg: " + msg);
        JSONObject messageObject = new JSONObject(msg);

        // make sure the received json object has a type field.
        if (!messageObject.has(TYPE_KEY)) {

            // if it doesn't we send failure response
            sendResponse(out[CLIENT_BOUND_MESSAGES], FAILURE_RESPONSE);
            return;
        }

        // handle the client side message, and send response to server.
        switch (messageObject.getString(TYPE_KEY)) {
            case MATCH_COMMUNICATION:

                // load the extracted payload into the queue
                JSONObject payload = messageObject.getJSONObject(PAYLOAD_KEY);
                datapackageQueue.insertClientBoundDatapackageQueue(
                        JSON_TO_DATAPACKAGE_CONVERTER.convertFromJSONObject(payload));
                Log.i(TAG, "inserted in to Client Bound Datapackage Queue");

                // send success response
                sendResponse(out[CLIENT_BOUND_MESSAGES], SUCCESS_RESPONSE);
                break;

            default:
                // send generic failure response
                sendResponse(out[CLIENT_BOUND_MESSAGES], FAILURE_RESPONSE);
                Log.i(TAG, "unexpected message type received: " + messageObject.getString(TYPE_KEY));

        }
    }

    private void sendError(Datapackage datapackage) throws IOException {
//        //FIXME
//        throw new UnsupportedOperationException("Unimplemented");


        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        // create the json object for the match communication server request
        JSONObject outgoingDatapackageJSON = null;
        try {
            outgoingDatapackageJSON = datapackage.getJSONObject();
        } catch (JSONException e) {
            Log.e(TAG, "sendMatchCommunication exception: ", e);
            throw new Error(e);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestType", ERROR_REPORT);
            jsonObject.put("username", JarAccount.getInstance().getName());
            jsonObject.put("game_token", gameToken);
            jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
            jsonObject.put("payload", outgoingDatapackageJSON);
        } catch (JSONException e) {
            Log.e(TAG, "sendMatchCommunication exception: ", e);
            throw new Error(e);
        }

        // send the message
        out[SERVER_BOUND_MESSAGES].writeUTF(jsonObject.toString());
        out[SERVER_BOUND_MESSAGES].flush();
        Log.i(TAG, "Sent JsonObject: " + jsonObject.toString());
        Log.i(TAG, String.valueOf(socket[SERVER_BOUND_MESSAGES]));

        // Wait for server response
        Log.i(TAG, "waiting on IO");
        while (!isDone() && bytesRead != -1) {
            bytesRead = in[SERVER_BOUND_MESSAGES].read(buffer);
            msgBuilder.append(new String(buffer));
        }
        String msg = msgBuilder.toString().trim();

        // TODO
        // Process the received response
        // currently we just ignore the response.
    }

    private void sendMatchCommunication(Datapackage datapackage) throws IOException {
        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        // create the json object for the match communication server request
        JSONObject outgoingDatapackageJSON = null;
        try {
            outgoingDatapackageJSON = datapackage.getJSONObject();
        } catch (JSONException e) {
            Log.e(TAG, "sendMatchCommunication exception: ", e);
            throw new Error(e);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestType", MATCH_COMMUNICATION);
            jsonObject.put("username", JarAccount.getInstance().getName());
            jsonObject.put("game_token", gameToken);
            jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
            jsonObject.put("payload", outgoingDatapackageJSON);
        } catch (JSONException e) {
            Log.e(TAG, "sendMatchCommunication exception: ", e);
            throw new Error(e);
        }

        // send the message
        out[SERVER_BOUND_MESSAGES].writeUTF(jsonObject.toString());
        out[SERVER_BOUND_MESSAGES].flush();
        Log.i(TAG, "Sent JsonObject: " + jsonObject.toString());
        Log.i(TAG, String.valueOf(socket[SERVER_BOUND_MESSAGES]));

        // Wait for server response
        Log.i(TAG, "waiting on IO");
        while (!isDone() && bytesRead != -1) {
            bytesRead = in[SERVER_BOUND_MESSAGES].read(buffer);
            msgBuilder.append(new String(buffer));
        }
        String msg = msgBuilder.toString().trim();

        // TODO
        // Process the received response
        // currently we just ignore the response.
    }

    private void sendMatchResult(Datapackage datapackage) {
        //FIXME
        throw new UnsupportedOperationException("Unimplemented");
    }

    private void sendNext() throws JSONException, IOException {
        Datapackage datapackage = datapackageQueue.getServerBoundDatapackage();

        switch (datapackage.getDatapackageType()) {

            case TURN:
                sendMatchCommunication(datapackage);
                break;
            case ERROR:
                sendError(datapackage);
                break;
            case PAUSE_REQUEST:
                sendMatchCommunication(datapackage);
                break;
            case PAUSE_ACCEPT:
                sendMatchCommunication(datapackage);
                break;
            case PAUSE_REJECT:
                sendMatchCommunication(datapackage);
                break;
            case MATCH_RESULT:
                sendMatchResult(datapackage);
                break;
            case DRAW_REQUEST:
                sendMatchCommunication(datapackage);
                break;
            case DRAW_ACCEPT:
                sendMatchCommunication(datapackage);
                break;
            case DRAW_REJECT:
                sendMatchCommunication(datapackage);
                break;
            case RESUME_REQUEST:
                sendMatchCommunication(datapackage);
                break;
        }
    }

    private void sendResponse(DataOutputStream out, String msg) throws IOException {

        if (out == null) {
            return;
        }

        if (msg == null) {
            throw new IllegalArgumentException("sendResponse must have a non-null message argument");
        }

        try {
            JSONObject outgoingDatapackageJSON = datapackageQueue.getServerBoundDatapackage().getJSONObject();
        } catch (JSONException e) {
            Log.e(TAG, "sendResponse exception: ", e);
            throw new Error(e);
        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("requestType", CLIENT_RESPONSE);
            jsonObject.put("username", JarAccount.getInstance().getName());
            jsonObject.put("game_token", gameToken);
            jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
            jsonObject.put("message", msg);
        } catch (JSONException e) {
            Log.e(TAG, "sendResponse exception: ", e);
            throw new Error(e); // this should not happen
        }

        out.writeUTF(jsonObject.toString());
        out.flush();
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
}
