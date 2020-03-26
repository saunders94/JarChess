package com.example.jarchess.online;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.MatchStarter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class OnlineMatchMaker {


    private static final String TAG = "OnlineMatchMaker";
    private static OnlineMatchMaker instance;
    private final Object lock;
    int response;
    private boolean wasCanceled = false;
    private IOException ioException = null;
    private OnlineMatchInfoBundle onlineMatchInfoBundle = null;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private int serverPort = 12345;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private boolean done;

    /**
     * Creates an instance of <code>OnlineMatchMaker</code> to construct a singleton instance
     */
    private OnlineMatchMaker() {
        lock = this;
    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static OnlineMatchMaker getInstance() {
        Log.d(TAG, "getInstance() called");
        Log.d(TAG, "getInstance is running on thread: " + Thread.currentThread().getName());
        if (instance == null) {
            instance = new OnlineMatchMaker();
        }

        return instance;
    }


    public void cancel() {
        synchronized (lock) {
            Log.d(TAG, "cancel() called");
            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());

            wasCanceled = true;
            notifyAll();
            if (socket != null && !socket.isClosed()) {
                try {
                    // tell the server that the request was canceled if you want and prepare to close socket

                    //TODO add any message you want to send to the server to announce that we canceled our search
                    //        We don't want players to get locked into match making without a way to get out.
                    //        Any thing that gets put here should have a 500 ms timeout, or starts a background thread
                    //        with a longer timeout.


                } finally {
                    //close the socket to stop the match making thread
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "cancel: ", e);
                    }
                }

            }
        }

    }

    public synchronized OnlineMatchInfoBundle getOnlineMatchInfoBundle() throws SearchCanceledException, IOException, InterruptedException {
        Log.d(TAG, "getOnlineMatchInfoBundle() called");
        Log.d(TAG, "getOnlineMatchInfoBundle is running on thread: " + Thread.currentThread().getName());
        wasCanceled = false;


        // start the search
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (lock) {
                    final byte[] buffer = new byte[1024];


                    try {

                        //create and bind socket to server

                        socket = new Socket(gameServer, serverPort);
                        socket.setSoTimeout(500);
                        boolean failed;

                        //send a request to the server to find a match for an online game
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("requestType", "RequestGame");
                            jsonObj.put("username", JarAccount.getInstance().getName());
                            jsonObj.put("signon_token", JarAccount.getInstance().getSignonToken());
                            Log.i("reqString", jsonObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        in = new DataInputStream(
                                new BufferedInputStream(
                                        socket.getInputStream()));
                        out = new DataOutputStream(
                                new BufferedOutputStream(
                                        socket.getOutputStream()));

                        if (!wasCanceled) {
                            do {
                                failed = false;
                                try {
                                    out.flush();
                                } catch (SocketTimeoutException e) {
                                    failed = true;
                                }
                            } while (failed && !wasCanceled);
                        }

                        final JSONObject jsonObjFinal = jsonObj;
                        tryUntilSuccessOrCancel(new SocketRunnable() {
                            @Override
                            public void run() throws IOException {

                                out.writeUTF(jsonObjFinal.toString());
                            }
                        });
                        Log.i(TAG, "request sent");
                        //receive a response from the server with all the needed match information (or a failure notification)

                        tryUntilSuccessOrCancel(new SocketRunnable() {
                            @Override
                            public void run() throws IOException {
                                response = in.read(buffer);
                            }
                        });
                        String respString = new String(buffer).trim();
                        Log.i(TAG, "response: \"" + respString + "\"");
                        socket.close();


                        try {
                            JSONObject jsonResp = new JSONObject(respString);
                            Log.i("Match Creation Response", jsonResp.toString());
                            onlineMatchInfoBundle = new OnlineMatchInfoBundle(jsonResp);
                            MatchStarter.getInstance().multiplayerSetup(onlineMatchInfoBundle);
                            Log.d(TAG, "run: set online match: " + onlineMatchInfoBundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("OnlineMatchmakerResp", "Error");
                        }
                        //use the received information to create an online match
                        lock.notifyAll();


                    } catch (IOException e1) { // if an I/O exception is experienced
                        ioException = e1; //record the exception
                    } catch (SearchCanceledException e2) {
                        // just get out
                    } catch (InterruptedException e3) {
                        //just get out
                    } finally {
                        done = true;
                        for (Closeable c : new Closeable[]{in, out, socket}) {
                            try {
                                c.close();
                            } catch (IOException e) {
                                // continue

                            }
                        }
                    }

                }
            }
        }, "onlineMatchMakerThread");

        t.start();
        synchronized (lock) {
            while (!done && !wasCanceled) {
                lock.wait(500);
            }

            if (wasCanceled) {
                throw new SearchCanceledException();
            } else if (ioException != null) {
                try {
                    throw ioException;
                } finally {
                    ioException = null;
                }
            }
        }
        return onlineMatchInfoBundle;
    }

    private void tryUntilSuccessOrCancel(SocketRunnable runnable) throws IOException, SearchCanceledException, InterruptedException {
        boolean failed;

        if (!wasCanceled) {
            do {
                failed = false;
                try {
                    runnable.run();
                } catch (SocketTimeoutException e) {
                    failed = true;
                    lock.wait(10);
                }
            } while (failed && !wasCanceled);
            if (wasCanceled) {
                throw new SearchCanceledException();
            }
        }
    }

    private interface SocketRunnable {
        void run() throws IOException;
    }

    public class SearchCanceledException extends Exception {
        private final static String msgHead = "Search Canceled Exception";

        public SearchCanceledException() {
            super(msgHead);
        }

        public SearchCanceledException(String message) {
            super(msgHead + ": " + message);
        }
    }
}
