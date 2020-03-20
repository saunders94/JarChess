package com.example.jarchess.online;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import static android.support.constraint.Constraints.TAG;

public class OnlineMatchMaker {


    private static OnlineMatchMaker instance;
    private final Object lock;
    private boolean wasCanceled = false;
    private Socket socket = null;
    private IOException ioException = null;
    private OnlineMatch onlineMatch = null;

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
        socket = null;
        ioException = null;
        onlineMatch = null;


        // start the search
        new Thread(new Runnable() {
            @Override
            public void run() {

                String host = ""; //FIXME
                int port = 0;//FIXME

                try {

                    //create and bind socket to server
                    socket = new Socket(host, port);

                    //send a request to the server to find a match for an online game
                    //TODO


                    //receive a response from the server with all the needed match information (or a failure notification)
                    //TODO

                    //use the received information to create an online match
                    synchronized (lock) {
                        onlineMatch = new OnlineMatch();//FIXME
                        Log.d(TAG, "run: set online match: " + onlineMatch);
                    }
                    lock.notifyAll();


                } catch (IOException e1) { // if an I/O exception is experienced
                    ioException = e1; //record the exception
                }

            }
        }, "onlineMatchMakerThread");

        while (onlineMatch == null && !wasCanceled) {
            wait();
        }

        if (wasCanceled) {
            throw new SearchCanceledException();
        }
//        else if (ioException != null) {
//            try {
//                throw ioException;
//            } finally {
//                ioException = null;
//            }
//        }

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
