package com.example.jarchess.online.networking;

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


    public NetworkReceiver(port){
        this.port = port;
        socket = new ServerSocket(port);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                runwork();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void runwork(){
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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
