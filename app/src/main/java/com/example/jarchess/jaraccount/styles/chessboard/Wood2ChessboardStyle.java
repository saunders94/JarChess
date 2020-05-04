package com.example.jarchess.jaraccount.styles.chessboard;

import com.example.jarchess.R;
import com.example.jarchess.match.Coordinate;

/**
 * A marble chessboard style is a chessboard style that has alternating black and white marble patterned squares
 *
 * @author Joshua Zierman
 */
public class Wood2ChessboardStyle implements ChessboardStyle {

    private static final int CHESSBOARD_SQUARE_DARK = R.drawable.chessboard_square_wood2_dark;
    private static final int CHESSBOARD_SQUARE_LIGHT = R.drawable.chessboard_square_wood2_light;
    private static Wood2ChessboardStyle instance;


    /**
     * Creates an instance of <code>MarbleChessboardStyle</code> to construct a singleton instance
     */
    private Wood2ChessboardStyle() {

    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static Wood2ChessboardStyle getInstance() {
        if (instance == null) {
            instance = new Wood2ChessboardStyle();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSquareResourceID(Coordinate coordinate) {
        if (coordinate.isWhiteSquare()) {
            return CHESSBOARD_SQUARE_LIGHT;
        } else {
            return CHESSBOARD_SQUARE_DARK;
        }
    }
}
