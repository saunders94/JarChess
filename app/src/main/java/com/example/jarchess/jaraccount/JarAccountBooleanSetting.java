package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.jarchess.online.usermanagement.Account;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.jaraccount.JarAccountSettingType.BOOLEAN;

public class JarAccountBooleanSetting extends JarAccountSetting<Boolean> {
    private static final String TAG = "JarAccountBooleanSettin";

    public JarAccountBooleanSetting(String key, Boolean defaultValue) {
        super(key, defaultValue, BOOLEAN);
    }

    public JarAccountBooleanSetting(String key, Boolean defaultValue, Flag... flags) {
        super(key, defaultValue, BOOLEAN, flags);
    }

    @Override
    public void loadFromJson(JSONObject jsonObject, Account accountIO, String name, String signonToken) throws JSONException {


        if (!doNotLoadFromServer) {
            Object o = jsonObject.get(key);

            if (o instanceof Integer) {
                value = o.equals(1);
            } else if (o instanceof String) {
                value = Boolean.valueOf((String) o);
            } else {
                value = (Boolean) o;
            }
        } else {
            Log.d(TAG, "loadFromJson: not going to load " + key);
        }

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
