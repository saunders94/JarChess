package com.example.jarchess.match.participant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationEventManager;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.CastleMove;
import com.example.jarchess.match.turn.Move;
import com.example.jarchess.match.turn.StandardMove;
import com.example.jarchess.match.turn.Turn;

import java.nio.ByteBuffer;
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

        // the smallest signal lencth in bytes that should be expected.
        private static final int TURN_SIGNAL_MIN_LENGTH = 3;

        // constant byte patterns that can be used to flag different types of signals.
        private static final byte RESIGNATION_SIGNAL_INDICATOR_BYTE = (byte) 0x00;
        private static final byte CASTLE_TURN_SIGNAL_INDICATOR_BYTE = (byte) 0x01;
        private static final byte STANDARD_TURN_SIGNAL_INDICATOR_BYTE = (byte) 0x02;


        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (alive) {
                synchronized (lock) {

                    byte[] signal = getSignal();
                    if (signalIsResignation(signal)) {
                        resign();
                        alive = false;
                    } else {
                        recievedTurns.add(toTurn(signal));
                    }

                }
            }
        }

        /**
         * Converts a byte array to a turn.
         *
         * @param bytes the bytes that represent a turn
         * @return the turn represented by the bytes
         */
        private Turn toTurn(byte[] bytes) { // TODO finish
            if (bytes.length < TURN_SIGNAL_MIN_LENGTH) {
                throw new IllegalArgumentException("signal too short to be a turn");
            }
            final long elapsedTime;
            Move move = null;

            ByteBuffer longByteBuffer = ByteBuffer.allocate(Long.BYTES);
            longByteBuffer.put(bytes, 1, Long.BYTES);
            elapsedTime = longByteBuffer.getLong();

            int moveByteBufferSize = bytes.length - 1 - Long.BYTES;
            ByteBuffer moveByteBuffer = ByteBuffer.allocate(moveByteBufferSize);
            moveByteBuffer.put(bytes, 1 + Long.BYTES, moveByteBufferSize);

            byte signal_indicator = bytes[0];

            switch (signal_indicator) {
                case CASTLE_TURN_SIGNAL_INDICATOR_BYTE:
//                    move = new CastleMove(moveByteBuffer.array());//FIXME
                    break;
                case STANDARD_TURN_SIGNAL_INDICATOR_BYTE:
                    move = new StandardMove(null, null, null, null);//FIXME
                    break;
                default:
                    throw new IllegalStateException("Unexpected signal indicator value: " + signal_indicator);
            }
            return new Turn(color, move, elapsedTime);
        }

        /**
         * Checks to see if a signal is a resignation signal.
         *
         * @param signal the signal from the remote participant
         * @return true if the signal is a resignation signal
         */
        private boolean signalIsResignation(byte[] signal) {
            return false;//FIXME
        }

        /**
         * Gets the next signal
         *
         * @return the signal
         */
        private byte[] getSignal() {
            return null;//FIXME
        }
    }


}
