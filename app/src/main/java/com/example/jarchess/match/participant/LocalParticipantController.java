package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationException;

/**
 * A local participant controller allows a local participant to be controlled by a UI
 *
 * @author Joshua Zierman
 */
public interface LocalParticipantController {
    Move getMove(ChessColor color) throws InterruptedException, ResignationException;

    Piece.PromotionChoice getPromotionChoice(Move move) throws InterruptedException;
}
