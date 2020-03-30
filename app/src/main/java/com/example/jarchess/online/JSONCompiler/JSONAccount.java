package com.example.jarchess.online.JSONCompiler;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONAccount {

    public JSONObject createAccount(String username, String password, String firstName,
                                    String lastname, String email){
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("requestType","createAccount");
            jsonObj.put("username",username);
            jsonObj.put("firstName", firstName);
            jsonObj.put("lastName", lastname);
            jsonObj.put("email",email);
            jsonObj.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject signin(String username, String password){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "signin");
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getUserStats(String username){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getUserStats");
            jsonObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getFriendsList(String username){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getFriendsList");
            jsonObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject sendFriendReq(String username, String signonToken, String friendsName){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "sendFriendRequest");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
            jsonObj.put("friendsUsername", friendsName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject acceptFriendReq(String username, String signonToken, String friendsName){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "validateFriendRequest");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
            jsonObj.put("friendsUsername", friendsName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
