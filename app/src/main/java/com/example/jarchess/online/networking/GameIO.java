package com.example.jarchess.online.networking;

import android.provider.ContactsContract;
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
    private int serverPort = 12345;
    private DatapackageQueue datapackageQueue;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;


    public GameIO(DatapackageQueue datapackageQueue, String gameToken, RemoteOpponentInfoBundle remoteOpponentInfoBundle){
        this.datapackageQueue = datapackageQueue;
        this.remoteOpponentInfoBundle = remoteOpponentInfoBundle;
        this.gameToken = gameToken;
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

        socket = new Socket(gameServer, serverPort);
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(
                        socket.getInputStream()));
        DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        socket.getOutputStream()));
        final byte[] buffer = new byte[1024];
        JSONObject initialObject = new JSONObject();
        Log.i(TAG, "remoteOpponentInfoBundle Color = " + remoteOpponentInfoBundle.getColor().toString());
        if(remoteOpponentInfoBundle.getColor().toString().equals("WHITE")){
            Log.i(TAG,"Initial move - waiting for oponent to move");
            int response = in.read(buffer);
            String respString = new String(buffer).trim();
            String respSting = new String(buffer).trim();
            Log.i(TAG, "Response: " + respSting);
            datapackageQueue.insertClientBoundDatapackageQueue(
                    Datapackage.DatapackageJSONConverter.getInstance().convertFromJSONObject(initialObject));
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
            out.writeUTF(jsonObject.toString());
            out.flush();
            Log.i(TAG,"waiting on IO");
            int response = in.read(buffer);
            String respSting = new String(buffer).trim();
            Log.i(TAG,"insertClientBoundDatapackageQueue");
            datapackageQueue.insertClientBoundDatapackageQueue(
                    Datapackage.DatapackageJSONConverter.getInstance().convertFromJSONObject(jsonObject));
        }
    }

}
