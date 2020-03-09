package com.example.jarchess.online.networking;

import com.example.jarchess.online.move.DatapackageFormatter;
import com.example.jarchess.online.move.DatapackageQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Controller {
    private String logonServer = "ChessGame-edd728ce6eceeae0.elb.us-east-2.amazonaws.com";
    private String gameServer = "ChessGame-edd728ce6eceeae0.elb.us-east-2.amazonaws.com";
    private String testServer = "192.168.1.174";
    private int serverPort = 12345;
    //private NetworkReceiver receiver;
    private NetworkSender sender;
    private DatapackageQueue moveQueue;
    private DatapackageFormatter moveFormatter;

    public Controller(){
        //this.receiver = new NetworkReceiver();
        this.sender = new NetworkSender(testServer, testServer, serverPort);
        this.moveQueue = new DatapackageQueue();
        this.moveFormatter = new DatapackageFormatter();
    }



    public void testSend(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("test","testval");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            sender.send(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
