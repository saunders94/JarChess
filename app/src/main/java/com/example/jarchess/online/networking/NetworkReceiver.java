package com.example.jarchess.online.networking;

import android.util.Log;

import com.example.jarchess.online.move.MoveAvailableQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkReceiver {
    private int port;
    private ServerSocket socket;
    MoveAvailableQueue moveQueue;


    public NetworkReceiver(int port, MoveAvailableQueue moveQueue){
        this.port = port;
        this.moveQueue = moveQueue;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true){
                    listen();
                }

            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void listen(){
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            while(true){
                Socket sock = socket.accept();
                in = new DataInputStream(
                        new BufferedInputStream(
                                sock.getInputStream()));
                out = new DataOutputStream(
                        new BufferedOutputStream(
                                sock.getOutputStream()));
                String request = in.readUTF();
                try {
                    JSONObject jsonObj = new JSONObject(request);
                    String reqType = jsonObj.getString("requestType");
                    Log.i("reqType",reqType);
                    moveQueue.insert(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    out.writeUTF("rcvd");
                    out.flush();
                    sock.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
