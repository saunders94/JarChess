package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.PromotionChoice;

/**
 * A local participant controller allows a local participant to be controlled by a UI
 *
 * @author Joshua Zierman
 */
public interface LocalParticipantController {
    void cancelInput();

    Move getMoveInput(ChessColor color) throws InterruptedException, MatchOverException;

    PromotionChoice getPromotionChoice(Move move) throws InterruptedException, MatchOverException;


}
