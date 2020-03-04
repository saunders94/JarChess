package com.example.jarchess.match.gui;

import com.example.jarchess.match.Coordinate;

/**
 * A square click listener is something that listens for square clicks and reacts when observing them.
 */
interface SquareClickListener {


    /**
     * Observes a square click.
     * @param coordinateClicked the clicked coordinate
     */
    void observeSquareClick(Coordinate coordinateClicked);
}
