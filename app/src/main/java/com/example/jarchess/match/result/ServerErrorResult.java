package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.EXCEPTION;
import static com.example.jarchess.match.result.ResultType.SERVER_ERROR;

public class ServerErrorResult extends DrawResult {
    public static final String JSON_PROPERTY_NAME_MSG = "msg";
    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = ServerErrorResult.ServerErrorResultJSONConverter.getInstance();
    private static final String JSON_PROPERTY_NAME_EXCEPTION_MSG = "eMsg";

    public ServerErrorResult() {
        super(SERVER_ERROR);
    }

    @Override
    protected String getDrawTypeString() {
        return "Server Error";
    }

    public static class ServerErrorResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = EXCEPTION;


        private static ServerErrorResult.ServerErrorResultJSONConverter instance;

        /**
         * Creates an instance of <code>ExceptionResultJSONConverter</code> to construct a singleton instance
         */
        private ServerErrorResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static ServerErrorResult.ServerErrorResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new ServerErrorResult.ServerErrorResultJSONConverter();
            }

            return instance;
        }

        @Override
        public ServerErrorResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            if (type == EXPECTED_TYPE) {
                return new ServerErrorResult();
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
