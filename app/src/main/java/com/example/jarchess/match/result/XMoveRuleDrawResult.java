package com.example.jarchess.match.result;

public class XMoveRuleDrawResult extends DrawResult {
    public final static int FORCED_DRAW_AMOUNT = 75;
    public final static int APPROVED_DRAW_AMOUNT = 50;
    private final int turnsSinceCaptureOrPawnMovement;

    public XMoveRuleDrawResult(int turnsSinceCaptureOrPawnMovement) {
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
}
