package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.EXCEPTION;

public class ExceptionResult extends WinResult {
    public static final String JSON_PROPERTY_NAME_MSG = "msg";
    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = ExceptionResult.ExceptionResultJSONConverter.getInstance();
    private final String message;
    private final Exception exception;
    private static final String JSON_PROPERTY_NAME_EXCEPTION_MSG = "e_msg";

    public ExceptionResult(ChessColor winnerColor, String message, Exception e) {
        super(winnerColor, EXCEPTION);
        this.message = message;
        this.exception = e;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        return super.getJSONObject().put(JSON_PROPERTY_NAME_MSG, message).put(JSON_PROPERTY_NAME_EXCEPTION_MSG, exception.getMessage());
    }

    public Exception getException() {
        return exception;
    }

    @Override
    protected String getWinTypeString() {
        return message;
    }


    public static class ExceptionResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = EXCEPTION;


        private static ExceptionResult.ExceptionResultJSONConverter instance;

        /**
         * Creates an instance of <code>ExceptionResultJSONConverter</code> to construct a singleton instance
         */
        private ExceptionResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static ExceptionResult.ExceptionResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new ExceptionResult.ExceptionResultJSONConverter();
            }

            return instance;
        }

        @Override
        public ExceptionResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            JSONObject colorJSON = jsonObject.has(JSON_PROPERTY_NAME_WINNING_COLOR) ? jsonObject.getJSONObject(JSON_PROPERTY_NAME_WINNING_COLOR) : null;
            ChessColor winningColor = ChessColor.JSON_CONVERTER.convertFromJSONObject(colorJSON);

            String msg = jsonObject.has(JSON_PROPERTY_NAME_MSG) ? jsonObject.getString(JSON_PROPERTY_NAME_MSG) : null;
            String eMsg = jsonObject.has(JSON_PROPERTY_NAME_EXCEPTION_MSG) ? jsonObject.getString(JSON_PROPERTY_NAME_EXCEPTION_MSG) : null;

            if (type == EXPECTED_TYPE) {
                return new ExceptionResult(winningColor, msg, new Exception(eMsg));
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
