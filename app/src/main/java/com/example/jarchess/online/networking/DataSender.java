package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.LoggedThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class DataSender {
    private static final String TAG = "DataSender";
    private final Object lock;
    private String logonServer = "ChessGame-edd728ce6eceeae0.elb.us-east-2.amazonaws.com";
    //private String logonServer = "3.20.74.62";
    private String testServer = "192.168.1.174";
    private int serverPort = 12345;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private JSONObject jsonObject;
    private JSONObject responseObject;

    public DataSender() {
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
                    Log.e(TAG, "run: ", e);
                }
            }
        };

        LoggedThread t = new LoggedThread(TAG, r, "sendThread");
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

    private void sendData() throws IOException {
        Log.d(TAG, "sendData() called");
        Log.d(TAG, "sendData is running on thread: " + Thread.currentThread().getName());

        Log.d(TAG, "sendData: waiting for lock: ");
        synchronized (lock) {
            Log.d(TAG, "sendData: got lock");

            Log.d(TAG, "sendData: creating socket");
            String respString = null;
            try {
                socket = new Socket();
                socket.setSoTimeout(500);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(logonServer, serverPort);
                socket.connect(inetSocketAddress, 1000);
                Log.d(TAG, "sendData: socket created");

                String data = jsonObject.toString();
                Log.i(TAG, "Data String: " + data);
                byte[] buffer = new byte[10240];
                in = new DataInputStream(
                        new BufferedInputStream(
                                socket.getInputStream()));
                out = new DataOutputStream(
                        new BufferedOutputStream(
                                socket.getOutputStream(),2048));

                out.writeUTF(data);
                out.flush();
                int response = in.read(buffer);
                respString = new String(buffer).trim();
                Log.i(TAG, "Response String: " + respString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
                out.close();
                in.close();
                lock.notifyAll();
            }
            try {

                JSONObject jsonObj = new JSONObject(respString);
                Log.i(TAG, "JSON response object: " + jsonObj.toString());
                //String reqType = jsonObj.getString("ERROR");
                //Log.i("reqType",respString);
                this.responseObject = jsonObj;
            } catch (JSONException e) {
                e.printStackTrace();
                this.responseObject = null;
            }

            Log.d(TAG, "sendData() returned");
        }


    }

}
