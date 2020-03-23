package com.example.jarchess.online.networking;

import android.media.audiofx.AcousticEchoCanceler;

import com.example.jarchess.online.move.DatapackageFormatter;
import com.example.jarchess.online.move.DatapackageQueue;
import com.example.jarchess.online.move.MoveAvailableQueue;
import com.example.jarchess.online.usermanagement.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Controller {
    private String logonServer = "ChessGame-edd728ce6eceeae0.elb.us-east-2.amazonaws.com";
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";
    private String testServer = "192.168.1.174";
    private int serverPort = 12345;
    private NetworkReceiver receiver;
    private LogonIO logonIO;
    private DatapackageQueue moveQueue;
    private DatapackageFormatter moveFormatter;
    private MoveAvailableQueue moveAvailableQueue;

    public Controller(){
        //this.receiver = new NetworkReceiver();
        //this.logonIO = new LogonIO(logonServer, serverPort);
        this.moveQueue = new DatapackageQueue();
        this.moveFormatter = new DatapackageFormatter();
        this.moveAvailableQueue = new MoveAvailableQueue();
    }




    public void testSend(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("test","testval");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            logonIO.send(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
