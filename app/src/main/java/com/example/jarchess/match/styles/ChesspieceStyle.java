package com.example.jarchess.match.styles;

import androidx.annotation.Nullable;

import com.example.jarchess.match.pieces.Piece;

/**
 * A chesspiece style is a style that allows resource ids for the piece art in the style to be accessed.
 */
public interface ChesspieceStyle {

    /**
     * Gets the resource id of the given piece.
     *
     * @param piece piece, may be null
     * @return the resource id of for the given piece in this style, or <code>R.drawable.chessboard_square_empty</code>
     * if piece was null
     */
    int getResourceID(@Nullable Piece piece);
}
