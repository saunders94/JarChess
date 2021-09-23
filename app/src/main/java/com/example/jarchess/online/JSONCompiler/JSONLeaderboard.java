package com.example.jarchess.online.JSONCompiler;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONLeaderboard {


    public JSONObject getLongestWinStreak(int numberOfPlayers) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("request_type", "getLongestWinStreak");
            jsonObj.put("number_of_games", numberOfPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getMostGamesWon(int numberOfPlayers) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("request_type", "getMostChessGamesWon");
            jsonObj.put("number_of_games", numberOfPlayers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
