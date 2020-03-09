package com.example.jarchess.match.datapackage;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONConverter<T extends JSONConvertable<T>> {

    public abstract T convertFromJSONObject(JSONObject jsonObject) throws JSONException;


    public JSONObject convertToJSONObject(T jsonConvertable) throws JSONException {
        return jsonConvertable.getJSONObject();
    }
}
