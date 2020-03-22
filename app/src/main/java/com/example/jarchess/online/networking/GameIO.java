package com.example.jarchess.online.networking;

public class GameIO {
    private String gameToken;
    private String gameServer = "AppLB-f1eb9121f64bbd52.elb.us-east-2.amazonaws.com";


    public GameIO(String gameToken){
        this.gameToken = gameToken;
    }



}
