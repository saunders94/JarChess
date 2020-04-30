package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.online.usermanagement.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class JarAccountSetting<T> {

    protected final String key;
    protected final JarAccountSettingType type;
    protected boolean doNotSaveOnServer = false;
    protected boolean doNotLoadFromServer = false;
    private final T defaultValue;
    protected T value;
    private static final String TAG = "JarAccountSetting";

    public JarAccountSetting(String key, T defaultValue, JarAccountSettingType type, Flag... flags) {
        this(key, defaultValue, type);

        for (Flag flag : flags) {
            switch (flag) {

                case DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER:
                    doNotSaveOnServer = true;
                    doNotLoadFromServer = true;
                    break;
            }
        }
    }

    public JarAccountSetting(String key, @NonNull T defaultValue, JarAccountSettingType type) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        value = defaultValue;
    }

    public void loadFromJson(JSONObject jsonObject, Account accountIO, String name, String signonToken) throws JSONException {
        if (!doNotLoadFromServer) {
            value = (T) jsonObject.get(key);
        } else {
            Log.d(TAG, "loadFromJson: not going to load " + key);
        }
    }

    public void loadFromServer(Account accountIO, String username, String signonToken) {
        if (!doNotSaveOnServer) {
            value = accountIO.getAccountInfo(this, username, signonToken);
        } else {
            Log.d(TAG, "loadFromServer: not going to load " + key);
        }
    }

    public void saveToJson(JSONObject jsonObject, Account accountIO, String username, String token) throws JSONException {
        jsonObject.put(key, value);
        Log.d(TAG, "saveToJson: " + jsonObject);
    }

    public void saveToServer(Account accountIO, String username, String signonToken) throws IOException {
        if (!doNotSaveOnServer) {
            accountIO.saveAccountInfo(this, username, signonToken);
        } else {
            Log.d(TAG, "loadFromServer: not going to save " + key);
        }
    }

    public void clear(SharedPreferences sharedPreferences) {
        value = defaultValue;
        saveToLocal(sharedPreferences);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getKey() {
        return key;
    }

    public JarAccountSettingType getType() {
        return type;
    }

    public synchronized T getValue() {

        return value;
    }

    public enum Flag {
        DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER,
        DO_NOT_SAVE_TO_SERVER,
        DO_NOT_LOAD_FROM_SERVER
    }

    public synchronized void setValue(T value) {
        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("null values are not accepted");
        }
    }

    public abstract void loadFromLocal(SharedPreferences sharedPreferences);

    public abstract boolean saveToLocal(SharedPreferences sharedPreferences);
}
