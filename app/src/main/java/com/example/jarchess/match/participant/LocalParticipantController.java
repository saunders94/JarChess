package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.move.Move;

/**
 * A local participant controller allows a local participant to be controlled by a UI
 *
 * @author Joshua Zierman
 */
public interface LocalParticipantController {
    Move getMove(ChessColor color) throws InterruptedException, ResignationException;
}
