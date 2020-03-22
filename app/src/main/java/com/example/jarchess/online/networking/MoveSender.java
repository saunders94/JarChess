package com.example.jarchess.online.networking;

import com.example.jarchess.online.move.DatapackageFormatter;
import com.example.jarchess.online.move.DatapackageQueue;

import java.io.IOException;

public class MoveSender {
    private DatapackageQueue queue;
    private DatapackageFormatter formatter;
    private LogonIO sender;

    public MoveSender(DatapackageQueue queue, LogonIO sender) {
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
        //FIXME
//        Datapackage datapackage = this.queue.getDatapackage();
//        //convert datapackage to JSONObject
//        JSONObject jsonObject = formatter.dataPkgToJson(datapackage);
//        //call network.sendData with new JSONObject
//        JSONObject rcvdMsg = sender.send(jsonObject);
//        Datapackage recvdPackage = formatter.jsonObjToDataPkg(rcvdMsg);
//        queue.insertDatapackage(recvdPackage);
    }

}
