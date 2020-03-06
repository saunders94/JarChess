package com.example.jarchess.match.datapackage;

import androidx.annotation.NonNull;

import com.example.jarchess.match.move.CastleMove;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.StandardMove;
import com.example.jarchess.match.turn.Turn;

public class Datapackege {
    private final DatapackageType datapackageType;

    public Datapackege(@NonNull Turn turn) {
        this.turn = turn;

        if(turn.getMove() == null){
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        if (move instanceof CastleMove) {
            datapackageType = DatapackageType.CASTLE_MOVE_TURN;
        } else if (move instanceof StandardMove) {
            datapackageType = DatapackageType.STANDARD_MOVE_TURN;
        } else {
            throw new IllegalArgumentException("Unexpected move type in turn: " + move.getClass());
        }
    }

    private final Turn turn;

    public Datapackege(DatapackageType datapackageType) {
        this.datapackageType = datapackageType;
        this.turn = null;
    }

    public DatapackageType getDatapackageType() {
        return datapackageType;
    }

    public Turn getTurn() {
        return turn;
    }

    public enum DatapackageType {
        STANDARD_MOVE_TURN(0),
        CASTLE_MOVE_TURN(1),
        RESIGNATION(2),
        PAUSE_REQUEST(3),
        PAUSE_ACCEPT(4),
        PAUSE_REJECT(5);

        private final int intValue;

        DatapackageType(int i) {
            intValue = i;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
