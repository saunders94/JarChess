package com.example.jarchess.match.result;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jarchess.match.result.ResultType.X_MOVE_RULE_DRAW;

public class XMoveRuleDrawResult extends DrawResult {
    public static final JSONConverter<ChessMatchResult> JSON_CONVERTER = XMoveRuleDrawResult.XMoveRuleDrawResultJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_TURNS_SINCE_CAP_OR_PAWN_MOVE = "turnsSinceCaptureOrPawnMovement";
    public final static int FORCED_DRAW_AMOUNT = 75;
    public final static int APPROVED_DRAW_AMOUNT = 50;
    private final int turnsSinceCaptureOrPawnMovement;

    public XMoveRuleDrawResult(int turnsSinceCaptureOrPawnMovement) {
        super(X_MOVE_RULE_DRAW);
        this.turnsSinceCaptureOrPawnMovement = turnsSinceCaptureOrPawnMovement;
    }

    @Override
    protected String getDrawTypeString() {
        if (turnsSinceCaptureOrPawnMovement < APPROVED_DRAW_AMOUNT) {
            throw new IllegalArgumentException("expecting at least " + APPROVED_DRAW_AMOUNT + " turns, but got " + turnsSinceCaptureOrPawnMovement);
        } else if (turnsSinceCaptureOrPawnMovement >= APPROVED_DRAW_AMOUNT && turnsSinceCaptureOrPawnMovement < FORCED_DRAW_AMOUNT) {
            return APPROVED_DRAW_AMOUNT + " move rule";
        } else {
            return FORCED_DRAW_AMOUNT + " move rule";
        }
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        return super.getJSONObject().put(JSON_PROPERTY_NAME_TURNS_SINCE_CAP_OR_PAWN_MOVE, turnsSinceCaptureOrPawnMovement);
    }

    public static class XMoveRuleDrawResultJSONConverter extends JSONConverter<ChessMatchResult> {

        private static final ResultType EXPECTED_TYPE = X_MOVE_RULE_DRAW;


        private static XMoveRuleDrawResult.XMoveRuleDrawResultJSONConverter instance;

        /**
         * Creates an instance of <code>XMoveRuleDrawResultJSONConverter</code> to construct a singleton instance
         */
        private XMoveRuleDrawResultJSONConverter() {
            super();
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static XMoveRuleDrawResult.XMoveRuleDrawResultJSONConverter getInstance() {
            if (instance == null) {
                instance = new XMoveRuleDrawResult.XMoveRuleDrawResultJSONConverter();
            }

            return instance;
        }

        @Override
        public XMoveRuleDrawResult convertFromJSONObject(@NonNull JSONObject jsonObject) throws JSONException {
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            ResultType type = ResultType.JSON_CONVERTER.convertFromJSONObject(typeJSON);

            int turns = jsonObject.getInt(JSON_PROPERTY_NAME_TURNS_SINCE_CAP_OR_PAWN_MOVE);
            if (type == EXPECTED_TYPE) {
                return new XMoveRuleDrawResult(turns);
            } else {
                throw new JSONException("expected type to be " + EXPECTED_TYPE);
            }
        }
    }
}
