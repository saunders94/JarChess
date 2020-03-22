package com.example.jarchess.online;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.online.move.DatapackageQueue;

import org.json.JSONObject;

public class OnlineMatch {
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;
    private JSONObject responseObject;

    public OnlineMatch(JSONObject responseObject){
        this.responseObject = responseObject;
    }

    private void setRemoteOpponentInfoBundle(){

    }

    public RemoteOpponentInfoBundle getBlackOpponentInfoBundle() {
        return null;//FIXME
    }

    public DatapackageQueue getDatapackageQueue() {
        return null;//FIXME
    }

    public String getMatchToken() {
        return null;//FIXME
    }

    public RemoteOpponentInfoBundle getWhiteOpponentInfoBundle() {
        return null;//FIXME
    }
}
