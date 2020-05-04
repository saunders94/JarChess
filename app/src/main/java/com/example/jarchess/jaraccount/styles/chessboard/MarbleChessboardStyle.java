package com.example.jarchess.jaraccount.styles.chessboard;

import com.example.jarchess.R;
import com.example.jarchess.match.Coordinate;

/**
 * A marble chessboard style is a chessboard style that has alternating black and white marble patterned squares
 *
 * @author Joshua Zierman
 */
public class MarbleChessboardStyle implements ChessboardStyle {

    private static final int CHESSBOARD_SQUARE_DARK = R.drawable.chessboard_square_dark_marble;
    private static final int CHESSBOARD_SQUARE_LIGHT = R.drawable.chessboard_square_light_marble;
    private static MarbleChessboardStyle instance;


    /**
     * Creates an instance of <code>MarbleChessboardStyle</code> to construct a singleton instance
     */
    private MarbleChessboardStyle() {

    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static MarbleChessboardStyle getInstance() {
        if (instance == null) {
            instance = new MarbleChessboardStyle();
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
