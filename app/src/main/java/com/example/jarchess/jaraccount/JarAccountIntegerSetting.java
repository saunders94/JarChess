package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;

import static com.example.jarchess.jaraccount.JarAccountSettingType.INTEGER;

public class JarAccountIntegerSetting extends JarAccountSetting<Integer> {
    public JarAccountIntegerSetting(String key, Integer defaultValue) {
        super(key, defaultValue, INTEGER);
    }

    @Override
    public synchronized void loadFromLocal(SharedPreferences sharedPreferences) {
        setValue(sharedPreferences.getInt(getKey(), getValue()));
    }

    @Override
    public synchronized boolean saveToLocal(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit().putInt(getKey(), getValue()).commit();
    }
}
