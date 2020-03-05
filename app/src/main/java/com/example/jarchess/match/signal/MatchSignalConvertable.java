package com.example.jarchess.match.signal;

import androidx.annotation.Nullable;

import com.example.jarchess.match.turn.Turn;

interface MatchSignalConvertable {
    MatchSignal.SignalType getSignalType();

    @Nullable
    Turn getTurn();
}
