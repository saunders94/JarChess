package com.example.jarchess.online;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.match.MatchStarter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static android.support.constraint.Constraints.TAG;

public class OnlineMatchMaker {


    private static OnlineMatchMaker instance;
    private final Object lock;
    private boolean wasCanceled = false;
    private IOException ioException = null;
    private OnlineMatch onlineMatch = null;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private int serverPort = 12345;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

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



    public synchronized void cancel() {
        Log.d(TAG, "cancel() called");
        Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());

        wasCanceled = true;
        notifyAll();
        if (socket != null && !socket.isClosed()) {
            try {
                // tell the server that the request was canceled
                //TODO


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

    public synchronized OnlineMatch getOnlineMatch() throws SearchCanceledException, IOException, InterruptedException {
        Log.d(TAG, "getOnlineMatch() called");
        Log.d(TAG, "getOnlineMatch is running on thread: " + Thread.currentThread().getName());
        wasCanceled = false;


        // start the search
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    byte[] buffer = new byte[1024];

                    try {

                        //create and bind socket to server
                        socket = new Socket(gameServer, serverPort);

                        //send a request to the server to find a match for an online game
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("requestType","RequestGame");
                            jsonObj.put("username",JarAccount.getInstance().getName());
                            jsonObj.put("signon_token", JarAccount.getInstance().getSignonToken());
                            Log.i("reqString",jsonObj.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        in = new DataInputStream(
                                new BufferedInputStream(
                                        socket.getInputStream()));
                        out = new DataOutputStream(
                                new BufferedOutputStream(
                                        socket.getOutputStream()));
                        out.writeUTF(jsonObj.toString());
                        out.flush();

                        //receive a response from the server with all the needed match information (or a failure notification)
                        int response = in.read(buffer);
                        String respString = new String(buffer).trim();
                        socket.close();


                        try {
                            JSONObject jsonResp = new JSONObject(respString);
                            Log.i("Match Creation Response", jsonResp.toString());
                            onlineMatch = new OnlineMatch(jsonResp);
                            MatchStarter.getInstance().multiplayerSetup(onlineMatch);
                            Log.d(TAG, "run: set online match: " + onlineMatch);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("OnlineMatchmakerResp", "Error");
                        }
                        //use the received information to create an online match
                        lock.notifyAll();


                    } catch (IOException e1) { // if an I/O exception is experienced
                        ioException = e1; //record the exception
                    }

                }

            }
        }, "onlineMatchMakerThread");

        t.start();
        while (onlineMatch == null && !wasCanceled) {
            lock.wait(1000);
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

        return onlineMatch;
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
