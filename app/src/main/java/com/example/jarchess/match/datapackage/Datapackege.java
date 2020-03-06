package com.example.jarchess.match.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.CastleMove;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.StandardMove;
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
        if (move instanceof CastleMove) {
            datapackageType = DatapackageType.CASTLE_MOVE_TURN;
        } else if (move instanceof StandardMove) {
            datapackageType = DatapackageType.STANDARD_MOVE_TURN;
        } else {
            throw new IllegalArgumentException("Unexpected move type in turn: " + move.getClass());
        }
    }

    private final Turn turn;

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
        STANDARD_MOVE_TURN(0), // Do we need or want both of these Turn types or should we just have one called TURN? TODO
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
