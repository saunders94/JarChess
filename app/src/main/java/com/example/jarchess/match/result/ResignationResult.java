package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.RESIGNATION;

public class ResignationResult extends WinResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = ResignationResult.ResignationResultJSONConverter.getInstance();

    public ResignationResult(ChessColor winnerColor) {
        super(winnerColor, RESIGNATION);
    }

    @Override
    protected String getWinTypeString() {
        return "resignation";
    }


    public static class ResignationResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = RESIGNATION;


        private static ResignationResult.ResignationResultJSONConverter instance;

        /**
         * Creates an instance of <code>ResignationResultJSONConverter</code> to construct a singleton instance
         */
        private ResignationResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static ResignationResult.ResignationResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new ResignationResult.ResignationResultJSONConverter();
            }

            return instance;
        }

        @Override
        public ResignationResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            JSONObject colorJSON = jsonObject.has(JSON_PROPERTY_NAME_WINNING_COLOR) ? jsonObject.getJSONObject(JSON_PROPERTY_NAME_WINNING_COLOR) : null;
            ChessColor winningColor = ChessColor.JSON_CONVERTER.convertFromJSONObject(colorJSON);
            if (type == EXPECTED_TYPE) {
                return new ResignationResult(winningColor);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
