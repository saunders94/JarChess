package com.example.jarchess.online.networking;

import com.example.jarchess.match.datapackage.Datapackege;
import com.example.jarchess.online.move.MoveFormatter;
import com.example.jarchess.online.move.MoveQueue;

import org.json.JSONObject;

import java.io.IOException;

public class MoveSender {
    private MoveQueue queue;
    private MoveFormatter formatter;
    private NetworkSender sender;

    public MoveSender(MoveQueue queue, NetworkSender sender){
        this.queue = queue;
        this.formatter = new MoveFormatter();
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
        Datapackege datapackege = this.queue.getMove();
        //convert datapackage to JSONObject
        JSONObject jsonObject = formatter.dataPkgToJson(datapackege);
        //call network.sendData with new JSONObject
        JSONObject rcvdMsg = sender.send(jsonObject);
        Datapackege recvdPackage = formatter.jsonObjToDataPkg(rcvdMsg);
        queue.insertMove(recvdPackage);
    }

}
