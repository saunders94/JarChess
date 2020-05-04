package com.example.jarchess.jaraccount.styles.chesspiece;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.pieces.Piece;

/**
 * A basic chesspiece style is an abstract chesspiece style that automatically returns a default value for null arguments.
 *
 * @author Joshua Zierman
 */
abstract class BasicChesspieceStyle implements ChesspieceStyle {
    private static final int NO_PIECE_RESOURCE_ID = R.drawable.chessboard_square_empty;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResourceID(Piece piece) {

        if (piece == null) {
            return NO_PIECE_RESOURCE_ID;
        } else {
            return helperGetResourceID(piece);
        }

    }

    /**
     * Gets the resource id for a <code>Piece</code>.
     *
     * @param piece the piece that the resource represents, not null
     * @return the resource id of the resource that represents the piece in this style
     */
    abstract int helperGetResourceID(@NonNull Piece piece);
}
