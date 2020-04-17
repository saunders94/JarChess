package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.FLAG_FALL;

public class FlagFallResult extends WinResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = FlagFallResult.FlagFallResultJSONConverter.getInstance();
    public FlagFallResult(ChessColor winnerColor) {
        super(winnerColor, FLAG_FALL);
    }

    @Override
    protected String getWinTypeString() {
        return "clock flag fall";
    }


    public static class FlagFallResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = FLAG_FALL;


        private static FlagFallResult.FlagFallResultJSONConverter instance;

        /**
         * Creates an instance of <code>FlagFallResultJSONConverter</code> to construct a singleton instance
         */
        private FlagFallResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static FlagFallResult.FlagFallResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new FlagFallResult.FlagFallResultJSONConverter();
            }

            return instance;
        }

        @Override
        public FlagFallResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            JSONObject colorJSON = jsonObject.has(JSON_PROPERTY_NAME_WINNING_COLOR) ? jsonObject.getJSONObject(JSON_PROPERTY_NAME_WINNING_COLOR) : null;
            ChessColor winningColor = ChessColor.JSON_CONVERTER.convertFromJSONObject(colorJSON);
            if (type == EXPECTED_TYPE) {
                return new FlagFallResult(winningColor);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
