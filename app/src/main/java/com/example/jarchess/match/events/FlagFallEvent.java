package com.example.jarchess.match.events;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.result.TimeoutResult;

public class FlagFallEvent extends MatchEndingEvent {

    private final ChessColor colorOfFallingFlag;

    public FlagFallEvent(ChessColor colorOfFallingFlag) {
        super(new TimeoutResult(getWinningColorFromColorOfFallingFlag(colorOfFallingFlag)));
        this.colorOfFallingFlag = colorOfFallingFlag;
    }

    private static ChessColor getWinningColorFromColorOfFallingFlag(ChessColor colorOfFallingFlag) {
        return ChessColor.getOther(colorOfFallingFlag);
    }

    public final ChessColor getColorOfFallingFLag() {
        return colorOfFallingFlag;
    }
}
