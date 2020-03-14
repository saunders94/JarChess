package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.datapackage.JSONConvertable;
import com.example.jarchess.match.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class PieceMovement implements JSONConvertable<PieceMovement> {
    public static final PieceMovementJSONConverter JSON_CONVERTER = PieceMovementJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_ORIGIN = "origin";
    public static final String JSON_PROPERTY_NAME_DESTINATION = "destination";
    private final Coordinate origin;
    private final Coordinate destination;

    public PieceMovement(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
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

    public Coordinate getOrigin() {
        return origin;
    }

    @NonNull
    @Override
    public String toString() {
        return origin.toString() + "->" + destination.toString();
    }

    public static class PieceMovementJSONConverter extends JSONConverter<PieceMovement> {

        private static PieceMovementJSONConverter instance;

        /**
         * Creates an instance of <code>PieceMovementJSONConverter</code> to construct a singleton instance
         */
        private PieceMovementJSONConverter() {
            //TODO
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static PieceMovementJSONConverter getInstance() {
            if (instance == null) {
                instance = new PieceMovementJSONConverter();
            }

            return instance;
        }

        @Override
        public PieceMovement convertFromJSONObject(JSONObject jsonObject) throws JSONException {
            Coordinate origin = Coordinate.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_ORIGIN));
            Coordinate destination = Coordinate.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_DESTINATION));
            return new PieceMovement(origin, destination);
        }
    }
}
