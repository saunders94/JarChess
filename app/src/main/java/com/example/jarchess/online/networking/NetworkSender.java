package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.match.datapackage.Datapackege;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;




public class NetworkSender {
    private String logonServer;
    private  String gameServer;
    private int port;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private JSONObject jsonObject;
    private JSONObject responseObject;

    public NetworkSender(String logonServer, String gameServer, int port){
        this.logonServer = logonServer;
        this.gameServer = gameServer;
        this.port = port;
    }

    public JSONObject send(JSONObject jsonObject) throws IOException {
        this.jsonObject = jsonObject;
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

        Thread t = new Thread(r, "CandidateGenerator");
        t.start();
        return this.responseObject;
    }

    private void sendData()throws IOException{
        socket = new Socket(gameServer, port);
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
        Log.i("Sender",respString);

        socket.close();
        try {
            JSONObject jsonObj = new JSONObject(respString);
            String reqType = jsonObj.getString("ERROR");
            Log.i("reqType",reqType);
            this.responseObject = jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            this.responseObject = null;
        }
    }

}
