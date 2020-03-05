package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;

/**
 * a standard move is a move with only one moving piece
 *
 * @author Joshua Zierman
 */
public class StandardMove implements Move {


    private final Coordinate origin;
    private final Coordinate destination;

    /**
     * Creates a standard move.
     * <p>
     *
     * @param origin      the orginin of the piece being moved
     * @param destination the destination that the piece is being moved to
     */
    public StandardMove(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate getOrigin() {
        return origin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Coordinate getDestination() {
        return destination;
    }

    @NonNull
    @Override
    public String toString() {
        return origin.toString() + "->" + destination.toString();
    }
}
