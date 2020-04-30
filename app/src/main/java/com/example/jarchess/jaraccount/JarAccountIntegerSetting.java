package com.example.jarchess.jaraccount;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.jarchess.online.usermanagement.Account;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.jaraccount.JarAccountSettingType.INTEGER;

public class JarAccountIntegerSetting extends JarAccountSetting<Integer> {
    private static final String TAG = "JarAccountIntegerSettin";
    public JarAccountIntegerSetting(String key, Integer defaultValue) {
        super(key, defaultValue, INTEGER);

    }

    @Override
    public void loadFromJson(JSONObject jsonObject, Account accountIO, String name, String signonToken) throws JSONException {

        if (!doNotLoadFromServer) {
            Object o = jsonObject.get(key);

            Log.e(TAG, "loadFromJson: class is " + o.getClass());
            if (o instanceof String) {
                value = Integer.valueOf((String) o);
            } else {
                value = (Integer) o;
            }
        } else {
            Log.d(TAG, "loadFromJson: not going to load " + key);
        }
    }

    public JarAccountIntegerSetting(String key, Integer defaultValue, Flag... flags) {
        super(key, defaultValue, INTEGER, flags);
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
