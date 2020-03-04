package com.example.jarchess.match.turn;

import com.example.jarchess.match.ChessColor;

/**
 * A turn is the encasilation of all basic elemnts of a turn in chess.
 * <p>
 * <ul>
 * <li>It has a color representing the color of the player making a move on that turn.
 * <li>It has a move that was made during that turn.
 * <li>It has an elapsed time that passed during that turn.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Turn {
    private final ChessColor color;
    private final Move move;
    private final long elapsedTime;

    /**
     * Creates a turn.
     *
     * @param color       the color of the moving participant
     * @param move        the move being made
     * @param elapsedTime the time in milliseconds that passed during the turn
     */
    public Turn(ChessColor color, Move move, long elapsedTime) {
        this.color = color;
        this.move = move;
        this.elapsedTime = elapsedTime;
    }

    /**
     * Gets the color of the participant making a move on this turn.
     *
     * @return the color of the participant making a move on this turn.
     */
    public ChessColor getColor() {
        return color;
    }

    /**
     * Gets the move that was made during this turn.
     *
     * @return the move that was made during this turn.
     */
    public Move getMove() {
        return move;
    }

    /**
     * Gets the amount of time that elapsed during this turn in milliseconds.
     *
     * @return the amount of milliseconds that passed during this turn.
     */
    public long getElapsedTime() {
        return elapsedTime;
    }
}
