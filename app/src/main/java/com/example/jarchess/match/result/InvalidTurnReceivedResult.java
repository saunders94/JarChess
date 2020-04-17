package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.INVALID_TURN_RECEIVED;

public class InvalidTurnReceivedResult extends WinResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = InvalidTurnReceivedResult.InvalidTurnReceivedResultJSONConverter.getInstance();

    public InvalidTurnReceivedResult(ChessColor winnerColor) {
        super(winnerColor, INVALID_TURN_RECEIVED);
    }

    @Override
    protected String getWinTypeString() {
        return "invalid move";
    }


    public static class InvalidTurnReceivedResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = INVALID_TURN_RECEIVED;


        private static InvalidTurnReceivedResult.InvalidTurnReceivedResultJSONConverter instance;

        /**
         * Creates an instance of <code>InvalidTurnReceivedResultJSONConverter</code> to construct a singleton instance
         */
        private InvalidTurnReceivedResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static InvalidTurnReceivedResult.InvalidTurnReceivedResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new InvalidTurnReceivedResult.InvalidTurnReceivedResultJSONConverter();
            }

            return instance;
        }

        @Override
        public InvalidTurnReceivedResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            JSONObject colorJSON = jsonObject.has(JSON_PROPERTY_NAME_WINNING_COLOR) ? jsonObject.getJSONObject(JSON_PROPERTY_NAME_WINNING_COLOR) : null;
            ChessColor winningColor = ChessColor.JSON_CONVERTER.convertFromJSONObject(colorJSON);
            if (type == EXPECTED_TYPE) {
                return new InvalidTurnReceivedResult(winningColor);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
