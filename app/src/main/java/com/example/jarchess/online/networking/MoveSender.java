package com.example.jarchess.online.networking;

import com.example.jarchess.online.datapackage.UnsignedDatapackage;
import com.example.jarchess.online.move.DatapackageFormatter;
import com.example.jarchess.online.move.DatapackageQueue;

import org.json.JSONObject;

import java.io.IOException;

public class MoveSender {
    private DatapackageQueue queue;
    private DatapackageFormatter formatter;
    private NetworkSender sender;

    public MoveSender(DatapackageQueue queue, NetworkSender sender) {
        this.queue = queue;
        this.formatter = new DatapackageFormatter();
        this.sender = sender;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    processMoves();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(r, "CandidateGenerator");
        t.start();
    }

    private void processMoves() throws IOException{
        UnsignedDatapackage unsignedDatapackage = this.queue.getDatapackage();
        //convert unsignedDatapackage to JSONObject
        JSONObject jsonObject = formatter.dataPkgToJson(unsignedDatapackage);
        //call network.sendData with new JSONObject
        JSONObject rcvdMsg = sender.send(jsonObject);
        UnsignedDatapackage recvdPackage = formatter.jsonObjToDataPkg(rcvdMsg);
        queue.insertDatapackage(recvdPackage);
    }

}
