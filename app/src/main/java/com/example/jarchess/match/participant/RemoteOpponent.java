package com.example.jarchess.match.participant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationEventManager;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

/**
 * A remote opponent is a match participant that is controlled by a participant on another device.
 *
 * @author Joshua Zierman
 */
public class RemoteOpponent implements MatchParticipant {//TODO write unit tests

    private final ResignationEventManager resignationEventManager;
    private final ChessColor color;
    private final ChessColor colorOfOtherParticipant;
    private final Object lock;
    private final String name;
    private final Queue<Turn> recievedTurns = new ConcurrentLinkedQueue<Turn>();

    private boolean alive;
    private ResignationEvent resignationDetected = null;

    /**
     * Creates a remote opponent.
     *
     * @param color                   the remote opponent's color
     * @param name                    the remote opponent's name
     * @param lockObject              is a object used for multi-thread synchronizing
     * @param resignationEventManager is used to notify listeners when this participant resigns
     */
    public RemoteOpponent(@NonNull ChessColor color, @NonNull String name, @Nullable Object lockObject, @NonNull ResignationEventManager resignationEventManager) {

        this.name = name;
        this.color = color;
        this.resignationEventManager = resignationEventManager;

        // setup the lock object
        if (lockObject == null) {
            lockObject = new Object();
        }
        lock = lockObject;


        switch (color) {
            case BLACK:
                colorOfOtherParticipant = WHITE;
                break;
            case WHITE:
                colorOfOtherParticipant = BLACK;
                break;
            default:
                throw new IllegalStateException("Unexpected color value: " + color);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void observeResignationEvent(ResignationEvent resignationEvent) {
        resignationDetected = resignationEvent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn takeFirstTurn() {
        return null;//FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn takeTurn(Turn lastTurnFromOtherParticipant) throws ResignationException {
        Turn turn;


        send(lastTurnFromOtherParticipant);

        try {
            turn = recieveNextTurn();
        } catch (InterruptedException e) {
            return null;
        }

        return turn;

    }

    /**
     * Sends the turn that was just taken by the other participant on this device to the remote participant on another device.
     *
     * @param lastTurnFromOtherParticipant the turn that was just taken by the other participant on this device
     */
    private void send(Turn lastTurnFromOtherParticipant) {
        //TODO
    }

    /**
     * Sends a byte array signal to the remote participant on another device.
     *
     * @param signal the byte array signal to be sent
     */
    private void send(byte[] signal) {
        //TODO
    }

    /**
     * Recieve the next turn taken by the remote participant on another device.
     *
     * @return the turn that is received
     * @throws InterruptedException if the thread is interrupted during the wait.
     * @throws ResignationException if a resignation is detected.
     */
    private Turn recieveNextTurn() throws InterruptedException, ResignationException {
        Turn turn = null;

//        synchronized (lock) {
//            resignationCheck();
//            waitForTurn();
//            turn = recievedTurn;
//            recievedTurn = null;
//            notifyAll();
        return turn; // FIXME
//        }
    }

    /**
     * Checks for resignation
     *
     * @throws ResignationException if resignation is detected
     */
    private void resignationCheck() throws ResignationException {
        if (resignationDetected != null) {
            throw new ResignationException(resignationDetected);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign() {
        resignationEventManager.resign(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessColor getColor() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public AvatarStyle getAvatarStyle() {
        return null;//FIXME
    }

    /**
     * A signal receiver is a runnable that receives signals from the remote participant.
     * <p>
     * The signals can represent turns taken by the remote participant and resignations.
     * <p>
     * //TODO update this when we add pause request functionality.
     */
    private class SignalReciever implements Runnable {


        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (alive) {
                synchronized (lock) {

                    //TODO recieve Datapackages

                }
            }
        }
    }


}
