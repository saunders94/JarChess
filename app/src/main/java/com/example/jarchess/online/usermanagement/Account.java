package com.example.jarchess.online.usermanagement;

import android.util.Log;

import com.example.jarchess.JarAccount;
import com.example.jarchess.online.JSONCompiler.JSONAccount;
import com.example.jarchess.online.networking.DataSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class  Account {
    private JSONAccount jsonAccount;
    private DataSender dataSender;
    private String username;
    private String password;
    private static final String TAG = "Account";

    public Account(){
        this.jsonAccount = new JSONAccount();
        this.dataSender = new DataSender();
    }

    private String getPasswordHash(String password){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public boolean signin(String username, String password){
        Log.d(TAG, "signin() called with: username = [" + username + "], password = [" + password + "]");
        Log.d(TAG, "signin is running on thread: " + Thread.currentThread().getName());
        boolean status = false;

        String hashedPass = getPasswordHash(password);
        if(hashedPass == null){
            return false;
        }

        if(username.equals("")){
            Log.i(TAG, "signin: username was empty string");
            Log.d(TAG, "signin() returned: " + false);
            return false;
        }else if(username == null){
            Log.i(TAG, "signin: username was null");
            Log.d(TAG, "signin() returned: " + false);
            return false;
        }
        JSONObject jsonObject =  jsonAccount.signin(username, hashedPass);



        try {
            JSONObject jsonResponse = dataSender.send(jsonObject);
            String statusResp = jsonResponse == null ? "" : (String) jsonResponse.get("status");
            if(statusResp.equals("success")){
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
            status = false;
        }

        return status;
    }

    public boolean signout(String username, String signonToken){
        Log.d(TAG, "signout() called with: username = [" + username + "], signonToken = [" + signonToken + "]");
        Log.d(TAG, "signout is running on thread: " + Thread.currentThread().getName());
        boolean status = false;
        if (username == null) {
            Log.i(TAG, "signout: username was null");
            Log.d(TAG, "signout() returned: " + false);
            return false;
        } else if (username.equals("")) {
            Log.i(TAG, "signout: username was empty string");
            Log.d(TAG, "signout() returned: " + false);
            return false;
        }
        JSONObject jsonObject =  jsonAccount.signout(username, signonToken);
        try {
            JSONObject jsonResponse = dataSender.send(jsonObject);
            String statusResp = (String) jsonResponse.get("status");
            if(statusResp.equals("success")){
                status = true;
                JarAccount.getInstance().setSignonToken((String) jsonResponse.get("token"));
                JarAccount.getInstance().setName(username);
                Log.i("signout",(String) jsonResponse.get("status"));
            }else{
                status = false;
                Log.i("Signout","Logon failure");
            }
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean registerAccount(String username, String password){
        boolean status = false;

        String hashedPass = getPasswordHash(password);
        if(hashedPass == null){
            return false;
        }

        JSONObject jsonObject = jsonAccount.registerAccount(username, hashedPass);
        try {
            JSONObject jsonResponse = dataSender.send(jsonObject);
            String statusResp = (String) jsonResponse.get("status");
            Log.i(TAG, "Status response: " + statusResp);
            if(statusResp.equals("success")){
                status = true;
                Log.i(TAG,"Registration Status: " + (String) jsonResponse.get("success"));
            }else{
                status = false;
                Log.i(TAG,"Registration Status: " + (String) jsonResponse.get("success"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean changePassword(String oldPass, String newPass){
        boolean status = false;

        String hashedOldPass = getPasswordHash(oldPass);
        String hashedNewPass = getPasswordHash(newPass);

        if(hashedOldPass == null || hashedNewPass == null){
            return false;
        }

        JSONObject jsonObject = jsonAccount.changePassword(JarAccount.getInstance().getName(),
                hashedOldPass, hashedNewPass);

        try {
            JSONObject jsonResponse = dataSender.send(jsonObject);
            String statusResp = (String) jsonResponse.get("status");
            Log.i(TAG, "Status response: " + statusResp);
            if(statusResp.equals("success")){
                status = true;
                Log.i(TAG,"Password change status: " + (String) jsonResponse.get("success"));
            }else{
                status = false;
                Log.i(TAG,"Password change status: " + (String) jsonResponse.get("success"));
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
