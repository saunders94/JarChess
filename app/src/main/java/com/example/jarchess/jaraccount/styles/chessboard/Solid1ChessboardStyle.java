package com.example.jarchess.jaraccount.styles.chessboard;

import com.example.jarchess.R;
import com.example.jarchess.match.Coordinate;

/**
 * A marble chessboard style is a chessboard style that has alternating black and white marble patterned squares
 *
 * @author Joshua Zierman
 */
public class Solid1ChessboardStyle implements ChessboardStyle {

    private static final int CHESSBOARD_SQUARE_DARK = R.color.solid1_dark;
    private static final int CHESSBOARD_SQUARE_LIGHT = R.color.solid1_light;
    private static Solid1ChessboardStyle instance;


    /**
     * Creates an instance of <code>MarbleChessboardStyle</code> to construct a singleton instance
     */
    private Solid1ChessboardStyle() {

    }

    /**
     * Gets the instance.
     *
     * @return the instance.
     */
    public static Solid1ChessboardStyle getInstance() {
        if (instance == null) {
            instance = new Solid1ChessboardStyle();
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
