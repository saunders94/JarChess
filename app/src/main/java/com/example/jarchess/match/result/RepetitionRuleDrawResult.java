package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.REPETITION_RULE_DRAW;

public class RepetitionRuleDrawResult extends DrawResult {

    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = RepetitionRuleDrawResult.RepetitionRuleDrawResultJSONConverter.getInstance();

    public final static int FORCED_DRAW_AMOUNT = 5;
    public final static int APPROVED_DRAW_AMOUNT = 3;
    public static final String JSON_PROPERTY_NAME_REPETITIONS = "repetitions";
    private final int repetitions;

    public RepetitionRuleDrawResult(int repetitions) {
        super(REPETITION_RULE_DRAW);
        this.repetitions = repetitions;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        return super.getJSONObject().put(JSON_PROPERTY_NAME_REPETITIONS, repetitions);
    }

    @Override
    protected String getDrawTypeString() {
        if (repetitions < APPROVED_DRAW_AMOUNT) {
            throw new IllegalArgumentException("expecting at least " + APPROVED_DRAW_AMOUNT + " turns, but got " + repetitions);
        } else if (repetitions >= APPROVED_DRAW_AMOUNT && repetitions < FORCED_DRAW_AMOUNT) {
            return APPROVED_DRAW_AMOUNT + "x repetition rule";
        } else {
            return FORCED_DRAW_AMOUNT + "x repetition rule";
        }
    }


    public static class RepetitionRuleDrawResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = REPETITION_RULE_DRAW;


        private static RepetitionRuleDrawResult.RepetitionRuleDrawResultJSONConverter instance;

        /**
         * Creates an instance of <code>RepetitionRuleDrawResultJSONConverter</code> to construct a singleton instance
         */
        private RepetitionRuleDrawResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static RepetitionRuleDrawResult.RepetitionRuleDrawResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new RepetitionRuleDrawResult.RepetitionRuleDrawResultJSONConverter();
            }

            return instance;
        }

        @Override
        public RepetitionRuleDrawResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            int repetitions = jsonObject.getInt(JSON_PROPERTY_NAME_REPETITIONS);

            if (type == EXPECTED_TYPE) {
                return new RepetitionRuleDrawResult(repetitions);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
