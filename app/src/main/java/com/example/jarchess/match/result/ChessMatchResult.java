package com.example.jarchess.match.result;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ChessMatchResult implements JSONConvertible<ChessMatchResult> {

    public static final String JSON_PROPERTY_NAME_WINNING_COLOR = "winning_color";
    public static final String JSON_PROPERTY_NAME_TYPE = "type";
    final ChessColor winnerColor;
    private final ResultType type;
    private static final String TAG = "ChessMatchResult";

    public ChessMatchResult(ChessColor winnerColor, ResultType type) {
        this.winnerColor = winnerColor;
        this.type = type;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_TYPE, type.getJSONObject());
        if (winnerColor != null) {
            jsonObject.put(JSON_PROPERTY_NAME_WINNING_COLOR, winnerColor.getJSONObject());
        } else {
            jsonObject.put(JSON_PROPERTY_NAME_WINNING_COLOR, JSONObject.NULL);
        }

        return jsonObject;
    }

    protected abstract String getMessage();

    public ResultType getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return getMessage();
    }

    public boolean wasDraw() {
        return winnerColor == null;
    }


    public static class ResultJSONConverter extends JSONConverter<ChessMatchResult> {


        private static ResultJSONConverter instance;

        /**
         * Creates an instance of <code>ResultJSONConverter</code> to construct a singleton instance
         */
        private ResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static ResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new ResultJSONConverter();
            }

            return instance;
        }

        @Override
        public ChessMatchResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            Log.d(TAG, "convertFromJSONObject: typeJSON = " + typeJSON.toString());
            ResultType resultType = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);
            Log.d(TAG, "convertFromJSONObject: resultType = " + resultType.toString());

            JSONConverter converter = resultType.getJSONConverter();
            Log.d(TAG, "convertFromJSONObject: converter = " + converter);
            JSONConvertible tmp = converter.convertFromJSONObject(jsonObject);

            if (tmp instanceof ChessMatchResult) {
                return (ChessMatchResult) tmp;
            } else {
                throw new JSONException("expected ChessMatchResult");
            }
        }
    }
}
