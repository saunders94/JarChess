package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;

import static com.example.jarchess.jaraccount.JarAccountSettingType.BOOLEAN;

public class JarAccountBooleanSetting extends JarAccountSetting<Boolean> {
    public JarAccountBooleanSetting(String key, Boolean defaultValue) {
        super(key, defaultValue, BOOLEAN);
    }

    @Override
    public synchronized void loadFromLocal(SharedPreferences sharedPreferences) {
        setValue(sharedPreferences.getBoolean(getKey(), getValue()));

    }

    @Override
    public synchronized boolean saveToLocal(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit().putBoolean(getKey(), getValue()).commit();
    }
}
