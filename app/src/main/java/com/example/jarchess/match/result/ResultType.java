package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

public enum ResultType implements JSONConvertible<ResultType> {
    CHECKMATE(0, CheckmateResult.JSON_CONVERTER),
    RESIGNATION(1, ResignationResult.JSON_CONVERTER),
    FLAG_FALL(2, FlagFallResult.JSON_CONVERTER),
    INVALID_TURN_RECEIVED(3, InvalidTurnReceivedResult.JSON_CONVERTER),
    EXCEPTION(4, ExceptionResult.JSON_CONVERTER),
    AGREED_UPON_DRAW(5, AgreedUponDrawResult.JSON_CONVERTER),
    REPETITION_RULE_DRAW(6, RepetitionRuleDrawResult.JSON_CONVERTER),
    STALEMATE_DRAW(7, StalemateDrawResult.JSON_CONVERTER),
    X_MOVE_RULE_DRAW(8, XMoveRuleDrawResult.JSON_CONVERTER);

    public static final JSONConverter<ResultType> JSON_CONVERTER = ResultTypeJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_NAME = "name";
    public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
    private final int intValue;
    private final JSONConverter<? extends ChessMatchResult> JSONConverter;

    ResultType(int i, JSONConverter<? extends ChessMatchResult> converter) {
        intValue = i;
        JSONConverter = converter;
    }

    public int getIntValue() {
        return intValue;
    }

    public JSONConverter getJSONConverter() {
        return JSONConverter;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_NAME, this.toString());
        jsonObject.put(JSON_PROPERTY_NAME_INT_VALUE, getIntValue());
        return jsonObject;
    }

    public static class ResultTypeJSONConverter extends JSONConverter<ResultType> {

        private static ResultTypeJSONConverter instance;

        /**
         * Creates an instance of <code>ResultTypeJSONConverter</code> to construct a singleton instance
         */
        private ResultTypeJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static ResultTypeJSONConverter getInstance() {
            if (instance == null) {
                instance = new ResultTypeJSONConverter();
            }

            return instance;
        }

        @Override
        public ResultType convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            int intValue = jsonObject.getInt(JSON_PROPERTY_NAME_INT_VALUE);
            return ResultType.values()[intValue];
        }
    }


}
