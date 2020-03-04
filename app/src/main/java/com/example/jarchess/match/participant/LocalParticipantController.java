package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;

/**
 * A local participant controller allows a local participant to be controlled by a UI
 *
 * @author Joshua Zierman
 */
public interface LocalParticipantController {
    void requestMove(ChessColor color);
}
