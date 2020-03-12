package com.example.jarchess.match.activity;

import com.example.jarchess.match.Coordinate;

/**
 * A square click listener is something that listens for square clicks and reacts when observing them.
 *
 * @author Joshua Zierman
 */
public interface SquareClickHandler {


    /**
     * Observes a square click.
     *
     * @param coordinateClicked the clicked coordinate
     */
    void observeSquareClick(Coordinate coordinateClicked);
}
