package com.example.jarchess.match.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.turn.Turn;

/**
 * A datapackage is a package that contains data to be sent from one remote opponent and recieved by another.
 * <p>
 * They can contain the following:
 * <p><ul>
 * <li>A turn
 * <li>A resignation
 * <li>A pause request
 * <li>A pause approval
 * <li>A pause rejection
 * </ul>
 */
public class Datapackege {
    private final DatapackageType datapackageType;
    private final Turn turn;


    /**
     * Creates a Datapackage from a Turn object.
     *
     * @param turn the turn to be packaged
     */
    public Datapackege(@NonNull Turn turn) {
        this.turn = turn;

        if (turn.getMove() == null) {
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        datapackageType = DatapackageType.TURN;
    }

    /**
     * Gets the type of datapackage this is.
     * @return the type of datapackage this is
     */
    public DatapackageType getDatapackageType() {
        return datapackageType;
    }

    /**
     * Gets the turn packaged in this.
     *
     * @return the turn packaged in this, may be null.
     */
    @Nullable
    public Turn getTurn() {
        return turn;
    }

    public enum DatapackageType {
        TURN(0),
        RESIGNATION(1),
        PAUSE_REQUEST(2),
        PAUSE_ACCEPT(3),
        PAUSE_REJECT(4);

        private final int intValue;

        DatapackageType(int i) {
            intValue = i;
        }

        public int getIntValue() {
            return intValue;
        }

        public static DatapackageType getFromInt(int i) {
            return DatapackageType.values()[i];
        }
    }

    public SignalType getMoveType(){
        return this.signalType;
    }

    public Move getMove(){
        return turn.getMove();
    }

    public long getElapsedTime(){
        return turn.getElapsedTime();
    }
}
