package com.example.jarchess.online.datapackage;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONConverter<T extends JSONConvertible<T>> {

    public abstract T convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException;


    public JSONObject convertToJSONObject(@NonNull T jsonConvertable) throws JSONException {
        return jsonConvertable.getJSONObject();
    }
}
