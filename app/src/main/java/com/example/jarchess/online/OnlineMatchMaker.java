package com.example.jarchess.online;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.LoggedThread;

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
    private String serverIp = "3.18.79.149";
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
                    //close the socket to stop the match making thread TODO make sure this actually stops thread
                    try {
                        socket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "cancel: socket close failed. ", e);
                    }
                }

            }
        }

    }

    public OnlineMatchInfoBundle getOnlineMatchInfoBundle() throws SearchCanceledException, IOException, InterruptedException {

        Log.v(TAG, "getOnlineMatchInfoBundle() called");
        Log.v(TAG, "getOnlineMatchInfoBundle is running on thread: " + Thread.currentThread().getName());
        try {
            this.socket = new Socket(gameServer, serverPort);
        } catch (IOException e) {
            throw e;
        }
        socket.setSoTimeout(500);
        boolean failed;
        final byte[] buffer = new byte[1024];

        this.in = new DataInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        this.out = new DataOutputStream(
                new BufferedOutputStream(
                        socket.getOutputStream()));

        // start the search
        Thread t = new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    wasCanceled = false;
                    done = false;

                    try {

                        //create and bind socket to server

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

                        final JSONObject jsonObjFinal = jsonObj;
                        tryUntilSuccessOrCancel(new SocketRunnable() {
                            @Override
                            public void run() throws IOException {

                                out.writeUTF(jsonObjFinal.toString());
                                out.flush();
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
                        Log.i(TAG,"Socket closed: " + socket);


                        try {
                            JSONObject jsonResp = new JSONObject(respString);
                            Log.i(TAG, jsonResp.toString());
                            if(jsonResp.getString("status").equals("success")){
                                onlineMatchInfoBundle = new OnlineMatchInfoBundle(jsonResp);
                                Log.i(TAG, "run: set online match: " + onlineMatchInfoBundle);
                            }else{
                                Log.i(TAG, "Server send failure response");
                                throw  new SearchCanceledException();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "run: ", e);
                        }
                        //use the received information to create an online match
                        lock.notifyAll();


                    } catch (IOException e1) { // if an I/O exception is experienced
                        ioException = e1; //record the exception
                        Log.i(TAG, e1.toString());
                    } catch (SearchCanceledException e2) {
                        // just get out
                        Log.i(TAG, "run: caught the cancel exception");
                    } catch (InterruptedException e3) {
                        //just get out
                        Log.i(TAG, e3.toString());
                    } finally {
                        lock.notifyAll();
                        done = true;
                        lock.notifyAll();
                        Log.d(TAG, "run: onlineMatchMaker thread set done true");
                        for (Closeable c : new Closeable[]{in, out, socket}) {
                            if (c != null) {
                                try {
                                    c.close();
                                } catch (IOException e) {
                                    // continue

                                }
                            } else {
                                Log.e(TAG, "run: trying to close something set to null");
                            }
                        }
                    }

                }

            }
        }, "onlineMatchMakerThread");

        t.start();
        synchronized (lock) {
            Log.i(TAG, "wasCaceled = " + wasCanceled);
            while (!done && !wasCanceled) {
                lock.wait(500);
            }

            if (wasCanceled) {
                Log.d(TAG, "getOnlineMatchInfoBundle: was canceled");
                wasCanceled = false;
                throw new SearchCanceledException();
            } else if (ioException != null) {
                try {
                    Log.e(TAG, "getOnlineMatchInfoBundle: ", ioException);
                    throw ioException;
                } finally {
                    ioException = null;
                }
            }
        }


        Log.d(TAG, "getOnlineMatchInfoBundle() returned: " + onlineMatchInfoBundle);
        return onlineMatchInfoBundle;
    }

    private void tryUntilSuccessOrCancel(SocketRunnable runnable) throws IOException, SearchCanceledException, InterruptedException {
        boolean failed;

        synchronized (lock) {

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
