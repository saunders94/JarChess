package com.example.jarchess.match.move;

import com.example.jarchess.match.Coordinate;

public class PieceMovement {
    private final Coordinate origin;
    private final Coordinate destination;

    public PieceMovement(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public Coordinate getDestination() {
        return destination;
    }
}
