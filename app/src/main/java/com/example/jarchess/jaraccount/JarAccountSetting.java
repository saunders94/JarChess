package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.jarchess.online.usermanagement.Account;

import java.io.IOException;

public abstract class JarAccountSetting<T> {

    private boolean doNotSaveOnServer = false;
    private final String key;
    private final JarAccountSettingType type;
    private final T defaultValue;
    private T value;

    public JarAccountSetting(String key, T defaultValue, JarAccountSettingType type, Flag... flags) {
        this(key, defaultValue, type);

        for (Flag flag : flags) {
            switch (flag) {

                case DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER:
                    doNotSaveOnServer = true;
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

    public void loadFromServer(Account accountIO, String username, String signonToken) {
        if (!doNotSaveOnServer) {
            value = accountIO.getAccountInfo(this, username, signonToken);
        }
    }

    public void saveToServer(Account accountIO, String username, String signonToken) throws IOException {
        if (!doNotSaveOnServer) {
            accountIO.saveAccountInfo(this, username, signonToken);
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
        DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER
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
