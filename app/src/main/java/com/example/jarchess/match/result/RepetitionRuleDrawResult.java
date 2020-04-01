package com.example.jarchess.match.result;

public class RepetitionRuleDrawResult extends DrawResult {
    public final static int FORCED_DRAW_AMOUNT = 5;
    public final static int APPROVED_DRAW_AMOUNT = 3;
    private final int repetitions;

    public RepetitionRuleDrawResult(int repetitions) {
        this.repetitions = repetitions;
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
}
