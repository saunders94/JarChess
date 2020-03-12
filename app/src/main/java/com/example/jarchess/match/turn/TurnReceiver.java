package com.example.jarchess.match.turn;

public interface TurnReceiver {

    Turn receiveNextTurn() throws InterruptedException;
}
