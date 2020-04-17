package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.CHECKMATE;

public class CheckmateResult extends WinResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = CheckmateResult.CheckmateResultJSONConverter.getInstance();
    
    public CheckmateResult(ChessColor winnerColor) {
        super(winnerColor, CHECKMATE);
    }

    @Override
    protected String getWinTypeString() {
        return "checkmate";
    }


    public static class CheckmateResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = CHECKMATE;


        private static CheckmateResult.CheckmateResultJSONConverter instance;

        /**
         * Creates an instance of <code>CheckmateResultJSONConverter</code> to construct a singleton instance
         */
        private CheckmateResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static CheckmateResult.CheckmateResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new CheckmateResult.CheckmateResultJSONConverter();
            }

            return instance;
        }

        @Override
        public CheckmateResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            JSONObject colorJSON = jsonObject.has(JSON_PROPERTY_NAME_WINNING_COLOR) ? jsonObject.getJSONObject(JSON_PROPERTY_NAME_WINNING_COLOR) : null;
            ChessColor winningColor = ChessColor.JSON_CONVERTER.convertFromJSONObject(colorJSON);
            if (type == EXPECTED_TYPE) {
                return new CheckmateResult(winningColor);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
