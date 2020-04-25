package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.online.datapackage.Datapackage;
import com.example.jarchess.online.move.DatapackageQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameIO {
    private final String TAG = "GameIO";
    private String gameToken;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private String serverIp = "3.18.79.149";
    private int serverPort = 12345;
    private DatapackageQueue datapackageQueue;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;


    public GameIO(DatapackageQueue datapackageQueue, String gameToken,
                  RemoteOpponentInfoBundle remoteOpponentInfoBundle) throws IOException {
        this.datapackageQueue = datapackageQueue;
        this.remoteOpponentInfoBundle = remoteOpponentInfoBundle;
        this.gameToken = gameToken;
        this.socket = new Socket(serverIp, serverPort);
        this.socket.setSoTimeout(300000);

        this.in = new DataInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        this.out = new DataOutputStream(
                new BufferedOutputStream(
                        socket.getOutputStream()));

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    processMoves();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void processMoves() throws IOException, JSONException {
        Log.d(TAG, "processMoves() called");
        Log.d(TAG, "processMoves is running on thread: " + Thread.currentThread().getName());

        //socket = new Socket(gameServer, serverPort);

        String response;
        final byte[] buffer = new byte[1024];
        Log.i(TAG, "Socket :" + socket.toString() + "   in: " + in + "      out: "+ out);
        JSONObject initialObject = new JSONObject();
        Log.i(TAG, "remoteOpponentInfoBundle Color = " + remoteOpponentInfoBundle.getColor().toString());
        if(remoteOpponentInfoBundle.getColor().toString().equals("WHITE")){
            JSONObject jsonObj = new JSONObject();
            Log.i(TAG,"Initial move - waiting for oponent to move");
            jsonObj.put("requestType","MakeMove");
            jsonObj.put("username", JarAccount.getInstance().getName());
            jsonObj.put("game_token",gameToken);
            jsonObj.put("signon_token",JarAccount.getInstance().getSignonToken());
            jsonObj.put("move","black");
            out.writeUTF(jsonObj.toString());
            out.flush();
            int resp = in.read(buffer);
            String respString = new String(buffer).trim();
            Log.i(TAG, "ResponseStr: " + respString);
            if (respString != null && respString.length() > 0) { //TODO << make sure this works
                JSONObject responseObject = new JSONObject(respString);
                datapackageQueue.insertClientBoundDatapackageQueue(
                        Datapackage.DatapackageJSONConverter.getInstance().convertFromJSONObject(responseObject));
            } //TODO << Make sure this works
        }else{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("requestType","MakeMove");
            jsonObj.put("username", JarAccount.getInstance().getName());
            jsonObj.put("game_token",gameToken);
            jsonObj.put("signon_token",JarAccount.getInstance().getSignonToken());
            jsonObj.put("move","white");
            out.writeUTF(jsonObj.toString());
            out.flush();
        }
        while(true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestType","MakeMove");
            jsonObject.put("username", JarAccount.getInstance().getName());
            jsonObject.put("game_token",gameToken);
            jsonObject.put("signon_token",JarAccount.getInstance().getSignonToken());
            Log.i(TAG,"JsonObject: " + jsonObject.toString());
            Log.i(TAG,"getClientBoundDatapackage");
            Datapackage datapackage = datapackageQueue.getServerBoundDatapackage();
            jsonObject.put("move",datapackage.getJSONObject());
            Log.i(TAG,"JsonObject: " + jsonObject.toString());
            Log.i(TAG,"sending datapackage");
            Log.i(TAG, String.valueOf(socket));
            out.writeUTF(jsonObject.toString());
            out.flush();
            Log.i(TAG,"waiting on IO");
            Log.i(TAG, String.valueOf(socket));
            int resp = in.read(buffer);
            String respSting = new String(buffer).trim();
            Log.i(TAG,"Response: " + respSting);
            Log.i(TAG,"insertClientBoundDatapackageQueue");
            JSONObject responseObject = new JSONObject(respSting);
            datapackageQueue.insertClientBoundDatapackageQueue(
                    Datapackage.DatapackageJSONConverter.getInstance().convertFromJSONObject(responseObject));
        }
    }

}
