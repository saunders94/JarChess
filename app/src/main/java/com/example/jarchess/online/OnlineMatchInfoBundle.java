package com.example.jarchess.online;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.styles.avatar.YellowBlackYellowCircleAvatarStyle;
import com.example.jarchess.online.move.DatapackageQueue;
import com.example.jarchess.online.networking.GameIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(JarAccount.getInstance().getName().equals(playerOne)){
            remoteOpponentInfoBundle = new RemoteOpponentInfoBundle(playerTwo,YellowBlackYellowCircleAvatarStyle.getInstance(),playerTwoColor, playerOneIp, playerOnePort);
            playerInfoBundle = new RemoteOpponentInfoBundle(playerOne, YellowBlackYellowCircleAvatarStyle.getInstance(), playerOneColor, playerOneIp, playerOnePort);
        }else if(JarAccount.getInstance().getName().equals(playerTwo)){
            remoteOpponentInfoBundle = new RemoteOpponentInfoBundle(playerOne, YellowBlackYellowCircleAvatarStyle.getInstance(), playerOneColor, playerOneIp, playerOnePort);
            playerInfoBundle = new RemoteOpponentInfoBundle(playerTwo, YellowBlackYellowCircleAvatarStyle.getInstance(), playerTwoColor, playerTwoIp, playerTwoPort);
        }

        this.gameToken = gameToken;
    }
}
