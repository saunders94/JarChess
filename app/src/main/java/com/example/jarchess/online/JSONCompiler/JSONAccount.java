package com.example.jarchess.online.JSONCompiler;

import android.util.Log;

import com.example.jarchess.jaraccount.JarAccountSettingType;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONAccount {

    private String TAG = "JSONAccount";

    public JSONObject acceptFriendReq(String username, String signonToken, String friendsName) {
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

    public JSONObject changePassword(String username, String oldPassword, String newPassword) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "changePassword");
            jsonObj.put("username", username);
            jsonObj.put("old_password", oldPassword);
            jsonObj.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, jsonObj.toString());
        return jsonObj;
    }

    public JSONObject createAccount(String username, String password, String firstName,
                                    String lastname, String email) {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("requestType", "createAccount");
            jsonObj.put("username", username);
            jsonObj.put("firstName", firstName);
            jsonObj.put("lastName", lastname);
            jsonObj.put("email", email);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getFriendRequests(String username) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getFriendRequests");
            jsonObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getFriendsList(String username) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getFriendsList");
            jsonObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject getUserStats(String username) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getUserStats");
            jsonObj.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject registerAccount(String username, String password) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "createAccount");
            jsonObj.put("username", username);
            jsonObj.put("firstName", "");
            jsonObj.put("lastName", "");
            jsonObj.put("email", "");
            jsonObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject removeFriend(String username, String friendsName) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "removeFriend");
            jsonObj.put("username", username);
            jsonObj.put("friendsUsername", friendsName); //TODO? Should we send a signonToken?
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public <T> JSONObject getAccountInfo(String key, T value, JarAccountSettingType type, String username, String signonToken) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "getAccountInfo");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
            jsonObj.put("key", key);
            jsonObj.put("value", value);
            jsonObj.put("type", type.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public <T> JSONObject saveAccountInfoByKey(String key, T value, JarAccountSettingType type, String username, String signonToken, String hash) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "saveAccountInfoByKey");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
            jsonObj.put("hash", hash);
            jsonObj.put("key", key);
            jsonObj.put("value", value);
            jsonObj.put("type", type.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject sendFriendReq(String username, String signonToken, String friendsName) {
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

    public JSONObject setAvatar(String username, String signonToken, int avatarInt) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "setAvatar");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
            jsonObj.put("avatarInt", avatarInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject signin(String username, String password) {
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

    public JSONObject signout(String username, String signonToken) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("requestType", "signout");
            jsonObj.put("username", username);
            jsonObj.put("signonToken", signonToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    public JSONObject verifySignonToken(String username, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestType", "validateSignonToken");
            jsonObject.put("username", username);
            jsonObject.put("signonToken", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
