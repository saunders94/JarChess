package com.example.jarchess.match.styles.chessboard;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;


/**
 * A chessboard style is a style that allows resource ids for the chessboard square art in the style to be accessed.
 *
 * @author Joshua Zierman
 */
public interface ChessboardStyle {

    /**
     * Gets the resource id of the square at the given coordinate in this style.
     *
     * @param coordinate the coordinate of the square to get the resource id for in this style, not null
     * @return the resource id of for the given coordinate in this style
     */
    int getSquareResourceID(@NonNull Coordinate coordinate);
}
