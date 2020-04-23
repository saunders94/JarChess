package com.example.jarchess.online;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.styles.avatar.PlayerAvatarStyles;
import com.example.jarchess.online.move.DatapackageQueue;
import com.example.jarchess.online.networking.GameIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OnlineMatchInfoBundle {
    private RemoteOpponentInfoBundle remoteOpponentInfoBundle;
    private RemoteOpponentInfoBundle playerInfoBundle;
    private JSONObject responseObject;
    private DatapackageQueue datapackageQueue;
    private String gameToken;
    private GameIO gameIO;
    private static final String TAG = "OnlineMatchInfoBundle";

    public OnlineMatchInfoBundle(JSONObject responseObject) {
        this.responseObject = responseObject;
        this.datapackageQueue = new DatapackageQueue();
        setRemoteOpponentInfoBundle();

        // establish connection
        try {
            this.gameIO = new GameIO(datapackageQueue, gameToken, remoteOpponentInfoBundle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RemoteOpponentInfoBundle getBlackOpponentInfoBundle() {
        if (playerInfoBundle.getColor().equals("black")) {
            return playerInfoBundle;
        } else {
            return remoteOpponentInfoBundle;
        }
    }

    public RemoteOpponentInfoBundle getPlayerInfoBundle() {
        return playerInfoBundle;
    }

    public DatapackageQueue getDatapackageQueue() {
        Log.d(TAG, "getDatapackageQueue() called");
        Log.d(TAG, "getDatapackageQueue is running on thread: " + Thread.currentThread().getName());
        Log.d(TAG, "getDatapackageQueue() returned: " + datapackageQueue);
        return datapackageQueue;
    }

    public String getMatchToken() {
        return gameToken;
    }

    public RemoteOpponentInfoBundle getRemoteOpponentInfoBundle() {
        return remoteOpponentInfoBundle;
    }

    public RemoteOpponentInfoBundle getWhiteOpponentInfoBundle() {
        if (playerInfoBundle.getColor().equals("white")) {
            return playerInfoBundle;
        } else {
            return remoteOpponentInfoBundle;
        }
    }

    private void setRemoteOpponentInfoBundle(){
        String playerOne = "";
        String playerTwo = "";
        String gameToken = "";
        String playerOneColor = "";
        String playerTwoColor = "";
        String playerOneIp = "";
        String playerTwoIp = "";
        int playerOnePort = 0;
        int playerTwoPort = 0;
        int playerOneAvatarStyleInt = 0;
        int playerTwoAvatarStyleInt = 0;
        try {
            playerOne = (String) responseObject.get("player_one");
            playerTwo = (String) responseObject.get("player_two");
            gameToken = (String) responseObject.get("game_token");
            playerOneColor = (String) responseObject.get("player_one_color");
            playerTwoColor = (String) responseObject.get("player_two_color");
            playerOneIp = (String) responseObject.get("player_one_ip");
            playerTwoIp = (String) responseObject.get("player_two_ip");
            playerOnePort = (int) responseObject.get("player_one_port");
            playerTwoPort = (int) responseObject.get("player_two_port");

            //TODO enable this when it is working on the server side
//            playerOnePort = (int) responseObject.get("player_one_avatar");
//            playerTwoPort = (int) responseObject.get("player_two_avatar");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(JarAccount.getInstance().getName().equals(playerOne)){
            AvatarStyle avatarStyle = PlayerAvatarStyles.getFromInt(playerTwoAvatarStyleInt).getAvatarStyle();
            remoteOpponentInfoBundle = new RemoteOpponentInfoBundle(playerTwo, avatarStyle, playerTwoColor, playerOneIp, playerOnePort);
            playerInfoBundle = new RemoteOpponentInfoBundle(playerOne, JarAccount.getInstance().getAvatarStyle(), playerOneColor, playerOneIp, playerOnePort);
        }else if(JarAccount.getInstance().getName().equals(playerTwo)){
            AvatarStyle avatarStyle = PlayerAvatarStyles.getFromInt(playerOneAvatarStyleInt).getAvatarStyle();
            remoteOpponentInfoBundle = new RemoteOpponentInfoBundle(playerOne, avatarStyle, playerOneColor, playerOneIp, playerOnePort);
            playerInfoBundle = new RemoteOpponentInfoBundle(playerTwo, JarAccount.getInstance().getAvatarStyle(), playerTwoColor, playerTwoIp, playerTwoPort);
        }

        this.gameToken = gameToken;
    }
}
