package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;

import static com.example.jarchess.jaraccount.JarAccountSettingType.STRING;

public class JarAccountStringSetting extends JarAccountSetting<String> {
    public JarAccountStringSetting(String key, String defaultValue) {
        super(key, defaultValue, STRING);
    }

    public JarAccountStringSetting(String key, String defaultValue, Flag... flags) {
        super(key, defaultValue, STRING, flags);
    }


    @Override
    public synchronized void loadFromLocal(SharedPreferences sharedPreferences) {
        setValue(sharedPreferences.getString(getKey(), getValue()));
    }

    @Override
    public synchronized boolean saveToLocal(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit().putString(getKey(), getValue()).commit();
    }
}
