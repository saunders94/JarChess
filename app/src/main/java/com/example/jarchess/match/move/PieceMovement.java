package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.datapackage.JSONConvertable;

import org.json.JSONException;
import org.json.JSONObject;

public class PieceMovement implements JSONConvertable<PieceMovement> {
    public static final String JSON_PROPERTY_NAME_ORIGIN = "origin";
    public static final String JSON_PROPERTY_NAME_DESTINATION = "destination";
    private final Coordinate origin;
    private final Coordinate destination;

    public PieceMovement(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_PROPERTY_NAME_ORIGIN, origin.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_DESTINATION, destination.getJSONObject());

        return jsonObject;
    }

    @NonNull
    @Override
    public String toString() {
        return origin.toString() + "->" + destination.toString();
    }
}
