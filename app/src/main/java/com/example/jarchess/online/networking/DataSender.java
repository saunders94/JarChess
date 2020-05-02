package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.LoggedThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.jarchess.online.networking.BadRequestIOException.BAD_REQUEST;


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
    IOException ioException;

    public DataSender() {
        this.lock = new Object();
    }

    public JSONObject send(JSONObject jsonObject) throws IOException {
        Log.d(TAG, "send() called with: jsonObject = [" + jsonObject + "]");
        Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
        this.jsonObject = jsonObject;


        responseObject = null;
        ioException = null;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        sendData();
                    } catch (IOException e) {
                        Log.e(TAG, "run: ", e);
                        ioException = e;
                        lock.notifyAll();
                    }
                }
            }
        };

        LoggedThread t = new LoggedThread(TAG, r, "sendThread");
        synchronized (lock){
            try {
                t.start();
                while (responseObject == null && ioException == null) {
                    lock.wait();
                    Log.d(TAG, "send: responseObject = " + responseObject);
                    Log.d(TAG, "send: ioException " + ioException);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (ioException != null) {
            throw ioException;
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
            String fullString = "";
            boolean readAgain = true;
            JSONObject jsonObj = null;
            try {
                socket = new Socket();
                socket.setSoTimeout(500);
                InetSocketAddress inetSocketAddress = new InetSocketAddress(logonServer, serverPort);
                socket.connect(inetSocketAddress, 3000);
                Log.d(TAG, "sendData: socket created");

                String data = jsonObject.toString();
                Log.i(TAG, "Data String: " + data);
                byte[] buffer = new byte[10240];
                in = new DataInputStream(
                        new BufferedInputStream(
                                socket.getInputStream()));
                out = new DataOutputStream(
                        new BufferedOutputStream(
                                socket.getOutputStream(),10240));

                out.writeUTF(data);
                out.flush();


                while(readAgain){
                    try{
                        respString = "";
                        int response = in.read(buffer);
                        respString = new String(buffer).trim();
                        fullString = fullString + respString;
                        Log.i(TAG, "Response String: " + respString);
                        jsonObj = new JSONObject(fullString);

                        Log.i(TAG, "JSON response object: " + jsonObj.toString());
                        readAgain = false;
                    }catch(JSONException e){
                        readAgain = true;
                    }
                }



                //String reqType = jsonObj.getString("ERROR");
                //Log.i("reqType",respString);
                this.responseObject = jsonObj;

            } catch (IOException e) {
                ioException = e;
            } finally {
                Closeable[] closeables = new Closeable[]{socket, out, in};
                for (Closeable c : closeables) {
                    try {
                        if (c instanceof Flushable) {
                            ((Flushable) c).flush();
                        }
                        c.close();
                    } catch (NullPointerException e) {
                    } catch (IOException e) {
                        Log.e(TAG, "sendData: ", e);
                    }
                }
                lock.notifyAll();
            }

            if (fullString.equals("") || respString.equals(BAD_REQUEST)) {
                IOException e = new BadRequestIOException();
                Log.e(TAG, "sendData: ", e);
                ioException = e;
                throw e;
            }
            Log.d(TAG, "sendData() returned");
        }


    }

}
