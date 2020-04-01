package com.example.jarchess.match.events;

import com.example.jarchess.match.Coordinate;

public class SquareClickEvent {
    private final Coordinate coordinateClicked;

    public SquareClickEvent(Coordinate coordinateClicked) {
        this.coordinateClicked = coordinateClicked;
    }

    public Coordinate getCoordinateClicked() {
        return coordinateClicked;
    }
}
