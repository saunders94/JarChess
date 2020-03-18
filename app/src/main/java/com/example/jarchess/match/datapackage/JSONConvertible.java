package com.example.jarchess.match.datapackage;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONConvertible<T> {

    JSONObject getJSONObject() throws JSONException;
}
