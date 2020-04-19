package com.example.jarchess.online.JSONCompiler;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONLeaderboard {


    public JSONObject getMostGamesWon(int numberOfPlayers){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getMostChessGamesWon");
            jsonObj.put("numberOfGames", numberOfPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getLongestWinStreak(int numberOfPlayers){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getLongestWinStreak");
            jsonObj.put("numberOfGames", numberOfPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
