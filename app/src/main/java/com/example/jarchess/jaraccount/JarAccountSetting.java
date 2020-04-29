package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public abstract class JarAccountSetting<T> {
    private final String key;
    private final JarAccountSettingType type;
    private final T defaultValue;
    private T value;

    public JarAccountSetting(String key, @NonNull T defaultValue, JarAccountSettingType type) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        value = defaultValue;
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
