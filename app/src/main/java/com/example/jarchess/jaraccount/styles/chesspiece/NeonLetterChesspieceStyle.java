package com.example.jarchess.jaraccount.styles.chesspiece;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.pieces.Piece;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.pieces.Piece.Type.BISHOP;
import static com.example.jarchess.match.pieces.Piece.Type.KING;
import static com.example.jarchess.match.pieces.Piece.Type.KNIGHT;
import static com.example.jarchess.match.pieces.Piece.Type.PAWN;
import static com.example.jarchess.match.pieces.Piece.Type.QUEEN;
import static com.example.jarchess.match.pieces.Piece.Type.ROOK;

/**
 * A neon letter chesspiece style is a basic chesspiece style with green and black letters representing
 * black pieces and pink and white letters representing white pieces.
 * <p>
 * <ul>
 * <li> "K" for Kings
 * <li> "Q" for Queens
 * <li> "B" for Bishops
 * <li> "N" for Knights
 * <li> "R" for Rooks
 * <li> "P" for Pawns
 * </ul>
 *
 * @author Joshua Zierman
 */
public class NeonLetterChesspieceStyle extends BasicChesspieceStyle {

    private static NeonLetterChesspieceStyle instance;
    private final int[][] resourceID = new int[ChessColor.values().length][Piece.Type.values().length];

    /**
     * Creates the neon letter chesspiece style
     */
    private NeonLetterChesspieceStyle() {
        super();
        resourceID[BLACK.getIntValue()][PAWN.getIntValue()] = R.drawable.piece_pawn_green_black_letter_img;
        resourceID[BLACK.getIntValue()][ROOK.getIntValue()] = R.drawable.piece_rook_green_black_letter_img;
        resourceID[BLACK.getIntValue()][KNIGHT.getIntValue()] = R.drawable.piece_knight_green_black_letter_img;
        resourceID[BLACK.getIntValue()][BISHOP.getIntValue()] = R.drawable.piece_bishop_green_black_letter_img;
        resourceID[BLACK.getIntValue()][QUEEN.getIntValue()] = R.drawable.piece_queen_green_black_letter_img;
        resourceID[BLACK.getIntValue()][KING.getIntValue()] = R.drawable.piece_king_green_black_letter_img;

        resourceID[WHITE.getIntValue()][PAWN.getIntValue()] = R.drawable.piece_pawn_pink_white_letter_img;
        resourceID[WHITE.getIntValue()][ROOK.getIntValue()] = R.drawable.piece_rook_pink_white_letter_img;
        resourceID[WHITE.getIntValue()][KNIGHT.getIntValue()] = R.drawable.piece_knight_pink_white_letter_img;
        resourceID[WHITE.getIntValue()][BISHOP.getIntValue()] = R.drawable.piece_bishop_pink_white_letter_img;
        resourceID[WHITE.getIntValue()][QUEEN.getIntValue()] = R.drawable.piece_queen_pink_white_letter_img;
        resourceID[WHITE.getIntValue()][KING.getIntValue()] = R.drawable.piece_king_pink_white_letter_img;
    }

    public static NeonLetterChesspieceStyle getInstance() {
        if (instance == null) {
            instance = new NeonLetterChesspieceStyle();
        }

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    int helperGetResourceID(@NonNull Piece piece) {
        return resourceID[piece.getColor().getIntValue()][piece.getType().getIntValue()];
    }
}
