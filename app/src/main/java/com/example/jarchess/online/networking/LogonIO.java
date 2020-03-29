package com.example.jarchess.online.networking;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;




public class LogonIO {
    private String logonServer = "ChessGame-edd728ce6eceeae0.elb.us-east-2.amazonaws.com";
    //private String logonServer = "3.20.74.62";
    private String testServer = "192.168.1.174";
    private int serverPort = 12345;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private JSONObject jsonObject;
    private JSONObject responseObject;
    private final Object lock;
    private static final String TAG = "LogonIO";

    public LogonIO() {
        this.lock = new Object();
    }

    public JSONObject send(JSONObject jsonObject) throws IOException {
        Log.d(TAG, "send() called with: jsonObject = [" + jsonObject + "]");
        Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
        this.jsonObject = jsonObject;


        responseObject = null;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r);
        synchronized (lock){
            try {
                t.start();
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "send() returned: " + this.responseObject);
        return this.responseObject;
    }

    private void sendData()throws IOException{
        Log.d(TAG, "sendData() called");
        Log.d(TAG, "sendData is running on thread: " + Thread.currentThread().getName());

        Log.d(TAG, "sendData: waiting for lock: ");
        synchronized (lock){
            Log.d(TAG, "sendData: got lock");

            Log.d(TAG, "sendData: creating socket");
            // TODO if the server cannot be reached the app hangs... we need to give the player a way to cancel login attempt or gracefully timeout.
            socket = new Socket(logonServer, serverPort);
            Log.d(TAG, "sendData: socket created");

            String data = jsonObject.toString();
            Log.i("Sender",data);
            byte[] buffer = new byte[1024];
            in = new DataInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()));
            out = new DataOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()));
            out.writeUTF(data);
            out.flush();
            int response = in.read(buffer);
            String respString = new String(buffer).trim();
            //Log.i("Sender",respString);

            socket.close();
            try {
                JSONObject jsonObj = new JSONObject(respString);
                Log.i("LogonIO","JSON response object: " + jsonObj.toString());
                //String reqType = jsonObj.getString("ERROR");
                //Log.i("reqType",respString);
                this.responseObject = jsonObj;
            } catch (JSONException e) {
                e.printStackTrace();
                this.responseObject = null;
            }
            lock.notifyAll();

            Log.d(TAG, "sendData() returned");
        }


    }

}
