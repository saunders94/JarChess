package com.example.jarchess.match.events;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.result.FlagFallResult;

public class FlagFallEvent extends MatchEndingEvent {

    private final ChessColor colorOfFallingFlag;

    public FlagFallEvent(ChessColor colorOfFallingFlag) {
        super(new FlagFallResult(getWinningColorFromColorOfFallingFlag(colorOfFallingFlag)));
        this.colorOfFallingFlag = colorOfFallingFlag;
    }

    private static ChessColor getWinningColorFromColorOfFallingFlag(ChessColor colorOfFallingFlag) {
        return ChessColor.getOther(colorOfFallingFlag);
    }

    public final ChessColor getColorOfFallingFlag() {
        return colorOfFallingFlag;
    }
}
