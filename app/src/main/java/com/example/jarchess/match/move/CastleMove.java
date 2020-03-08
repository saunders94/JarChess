package com.example.jarchess.match.move;

import com.example.jarchess.match.Coordinate;

public class CastleMove extends Move {
    public CastleMove(Coordinate origin1, Coordinate destination1, Coordinate origin2, Coordinate destination2) {
        super(origin1, destination1);
        add(new PieceMovement(origin2, destination2));
    }

    public CastleMove(PieceMovement pieceMovement1, PieceMovement pieceMovement2) {
        super(pieceMovement1);
        add(pieceMovement2);
    }
}
