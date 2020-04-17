package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.STALEMATE_DRAW;

/**
 * A Stalemate draw results from a player being unable to make a legal move while not being in check.
 */
public class StalemateDrawResult extends DrawResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = StalemateDrawResult.StalemateDrawResultJSONConverter.getInstance();

    public StalemateDrawResult() {
        super(STALEMATE_DRAW);
    }

    @Override
    protected String getDrawTypeString() {
        return "stalemate";
    }

    public static class StalemateDrawResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = STALEMATE_DRAW;


        private static StalemateDrawResult.StalemateDrawResultJSONConverter instance;

        /**
         * Creates an instance of <code>StalemateDrawResultJSONConverter</code> to construct a singleton instance
         */
        private StalemateDrawResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static StalemateDrawResult.StalemateDrawResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new StalemateDrawResult.StalemateDrawResultJSONConverter();
            }

            return instance;
        }

        @Override
        public StalemateDrawResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            if (type == EXPECTED_TYPE) {
                return new StalemateDrawResult();
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
