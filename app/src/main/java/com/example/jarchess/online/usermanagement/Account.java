package com.example.jarchess.online.usermanagement;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.online.JSONCompiler.JSONAccount;
import com.example.jarchess.online.networking.LogonIO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Account {
    private JSONAccount jsonAccount;
    private LogonIO logonIO;
    private String username;
    private String password;
    private static final String TAG = "Account";

    public Account(){
        this.jsonAccount = new JSONAccount();
        this.logonIO = new LogonIO();
    }

    public boolean signin(String username, String password){
        Log.d(TAG, "signin() called with: username = [" + username + "], password = [" + password + "]");
        Log.d(TAG, "signin is running on thread: " + Thread.currentThread().getName());
        boolean status = false;
        if(username.equals("")){
            Log.i(TAG, "signin: username was empty string");
            Log.d(TAG, "signin() returned: " + false);
            return false;
        }else if(username == null){
            Log.i(TAG, "signin: username was null");
            Log.d(TAG, "signin() returned: " + false);
            return false;
        }
        JSONObject jsonObject =  jsonAccount.signin(username, password);
        try {
            JSONObject jsonResponse = logonIO.send(jsonObject);
            String statusResp = (String) jsonResponse.get("status");
            if(statusResp.equals("succes")){
                status = true;
                JarAccount.getInstance().setSignonToken((String) jsonResponse.get("token"));
                JarAccount.getInstance().setName(username);
                Log.i("Signin",(String) jsonResponse.get("status"));
            }else{
                status = false;
                Log.i("Signin","Logon failure");
            }
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

}
