package com.example.jarchess.match.pieces;

public abstract class Piece {
    public enum Color{BLACK, WHITE}

    private final Color color;

    public Piece(Color color) {
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
