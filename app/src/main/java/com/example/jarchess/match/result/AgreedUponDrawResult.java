package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.AGREED_UPON_DRAW;

public class AgreedUponDrawResult extends DrawResult {
    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = AgreedUponDrawResultJSONConverter.getInstance();

    public AgreedUponDrawResult() {
        super(AGREED_UPON_DRAW);
    }

    @Override
    protected String getDrawTypeString() {
        return "agreed upon";
    }


    public static class AgreedUponDrawResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = AGREED_UPON_DRAW;


        private static AgreedUponDrawResultJSONConverter instance;

        /**
         * Creates an instance of <code>AgreedUponDrawResultJSONConverter</code> to construct a singleton instance
         */
        private AgreedUponDrawResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static AgreedUponDrawResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new AgreedUponDrawResultJSONConverter();
            }

            return instance;
        }

        @Override
        public AgreedUponDrawResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(ChessMatchResult.JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);
            if (type == EXPECTED_TYPE) {
                return new AgreedUponDrawResult();
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
