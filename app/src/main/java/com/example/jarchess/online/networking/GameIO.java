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

    private static final String PAYLOAD_KEY = "payload";
    private static final String MATCH_COMMUNICATION = "MatchCommunication";
    private static final String TYPE_KEY = "type";
    private static final Datapackage.DatapackageJSONConverter JSON_TO_DATAPACKAGE_CONVERTER = Datapackage.DatapackageJSONConverter.getInstance();
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
                  RemoteOpponentInfoBundle remoteOpponentInfoBundle) {

        boolean canStart = false;
        this.datapackageQueue = datapackageQueue;
        this.remoteOpponentInfoBundle = remoteOpponentInfoBundle;
        this.gameToken = gameToken;
        try {
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
            try {
                in.close();
            } catch (IOException ex) {
                // just continue
            }
            try {
                out.close();
            } catch (IOException ex) {
                // just continue
            }
            try {
                socket.close();
            } catch (IOException ex) {
                // just continue;
            }
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
            closeables = new Closeable[]{in, out, socket};
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

    private void receiveNext() throws JSONException, IOException {
        int bytesRead = 0;
        StringBuilder msgBuilder = new StringBuilder();
        final byte[] buffer = new byte[1024];

        while (!isDone() && bytesRead != -1) {
            bytesRead = in.read(buffer);
            msgBuilder.append(buffer);
        }
        String msg = msgBuilder.toString().trim();
        if (msg.isEmpty() || isDone()) {
            return;
        }
        Log.i(TAG, "msg: " + msg);
        JSONObject messageObject = new JSONObject(msg);

        switch (messageObject.getString(TYPE_KEY)) {
            case MATCH_COMMUNICATION:
                JSONObject payload = messageObject.getJSONObject(PAYLOAD_KEY);
                datapackageQueue.insertClientBoundDatapackageQueue(
                        JSON_TO_DATAPACKAGE_CONVERTER.convertFromJSONObject(payload));

                Log.i(TAG, "inserted in to Client Bound Datapackage Queue");
                break;

            default:
                Log.i(TAG, "unexpected message type received: " + messageObject.getString(TYPE_KEY));

        }
    }

    private void sendNext() throws JSONException, IOException {

        JSONObject outgoingDatapackageJSON = datapackageQueue.getServerBoundDatapackage().getJSONObject();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType", MATCH_COMMUNICATION);
        jsonObject.put("username", JarAccount.getInstance().getName());
        jsonObject.put("game_token", gameToken);
        jsonObject.put("signon_token", JarAccount.getInstance().getSignonToken());
        jsonObject.put("payload", outgoingDatapackageJSON);
        out.writeUTF(jsonObject.toString());
        out.flush();
        Log.i(TAG, "JsonObject: " + jsonObject.toString());
        Log.i(TAG, String.valueOf(socket));
        Log.i(TAG, "waiting on IO");
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
