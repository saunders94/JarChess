package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

public enum ResultType implements JSONConvertible<ResultType> {
    CHECKMATE(0),
    RESIGNATION(1),
    FLAG_FALL(2),
    INVALID_TURN_RECEIVED(3),
    EXCEPTION(4),
    AGREED_UPON_DRAW(5),
    REPETITION_RULE_DRAW(6),
    STALEMATE_DRAW(7),
    X_MOVE_RULE_DRAW(8),
    SERVER_ERROR(9);

    private static final JSONConverter[] jsonConverters = new JSONConverter[values().length];
    private static boolean convertersNeedToBeSetUp = true;
    public static final JSONConverter<ResultType> JSON_CONVERTER = ResultTypeJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_NAME = "name";
    public static final String JSON_PROPERTY_NAME_INT_VALUE = "int_value";
    private final int intValue;

    ResultType(int i) {
        intValue = i;
    }

    public int getIntValue() {
        return intValue;
    }

    public JSONConverter getJSONConverter() {
        if (convertersNeedToBeSetUp) {
            jsonConverters[CHECKMATE.intValue] = CheckmateResult.JSON_CONVERTER;
            jsonConverters[RESIGNATION.intValue] = ResignationResult.JSON_CONVERTER;
            jsonConverters[FLAG_FALL.intValue] = FlagFallResult.JSON_CONVERTER;
            jsonConverters[INVALID_TURN_RECEIVED.intValue] = InvalidTurnReceivedResult.JSON_CONVERTER;
            jsonConverters[EXCEPTION.intValue] = ExceptionResult.JSON_CONVERTER;
            jsonConverters[AGREED_UPON_DRAW.intValue] = AgreedUponDrawResult.JSON_CONVERTER;
            jsonConverters[REPETITION_RULE_DRAW.intValue] = RepetitionRuleDrawResult.JSON_CONVERTER;
            jsonConverters[STALEMATE_DRAW.intValue] = StalemateDrawResult.JSON_CONVERTER;
            jsonConverters[X_MOVE_RULE_DRAW.intValue] = XMoveRuleDrawResult.JSON_CONVERTER;
            jsonConverters[SERVER_ERROR.intValue] = ServerErrorResult.JSON_CONVERTER;
        }
        return jsonConverters[intValue];
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

    @Override
    public String toString() {
        switch (this) {
            case INVALID_TURN_RECEIVED:
            case EXCEPTION:
            case SERVER_ERROR:
                return "ERROR";
            default:
                break;
        }

        return super.toString();
    }

    //    public static void main(String[] args) {
//        for (ResultType t :ResultType.values()) {
//            System.out.println("\"" + t.toString() + "\"");
//        }
//    }
}
