package com.example.jarchess.match.participant;

import androidx.annotation.NonNull;

import com.example.jarchess.RemoteOpponentAccount;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.datapackage.DatapackageReceiver;
import com.example.jarchess.match.datapackage.DatapackageSender;
import com.example.jarchess.match.datapackage.MatchNetworkIO;
import com.example.jarchess.match.resignation.Resignation;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationEventManager;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationReciever;
import com.example.jarchess.match.resignation.ResignationSender;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.turn.TurnReceiver;
import com.example.jarchess.match.turn.TurnSender;

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

    private final AvatarStyle avatarStyle;
    private final ChessColor color;
    //    private final Object lock; //FIXME
    private final String name;
    private final ChessColor colorOfOtherParticipant;
    private final TurnSender turnSender;
    private final Queue<Turn> recievedTurns = new ConcurrentLinkedQueue<Turn>();
    private final TurnReceiver turnReceiver;
    private final ResignationSender resignationSender;
    private final ResignationReciever resignationReciever;
    private boolean alive;
    private ResignationEvent resignationDetected = null;
    private ResignationEventManager resignationEventManager;

    /**
     * Creates a remote opponent.
     *
     * @param color                   the remote opponent's color
     * @param remoteOpponentAccount the remote opponent's account
     */
    public RemoteOpponent(@NonNull ChessColor color, @NonNull RemoteOpponentAccount remoteOpponentAccount, DatapackageSender sender, DatapackageReceiver receiver) {

        this.name = remoteOpponentAccount.getName();
        this.avatarStyle = remoteOpponentAccount.getAvatarStyle();
        this.color = color;
        MatchNetworkIO.Sender mNIOSender = new MatchNetworkIO.Sender(sender);
        MatchNetworkIO.Reciever mNIOReciever = new MatchNetworkIO.Reciever(receiver);
        this.turnSender = mNIOSender;
        this.turnReceiver = mNIOReciever;
        this.resignationSender = mNIOSender;
        this.resignationReciever = mNIOReciever;


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
     *
     * @return
     */
    @Override
    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessColor getColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn() {
        try {
            return turnReceiver.receiveNextTurn();
        } catch (InterruptedException e) {
            // just get out
        }
        return null;
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
    public void resign() {
        resignationSender.send(new Resignation());
        resignationEventManager.resign(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws ResignationException {

        turnSender.send(lastTurnFromOtherParticipant);

        try {
            return turnReceiver.receiveNextTurn();
        } catch (InterruptedException e) {
            // just get out
        }

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void observeResignationEvent(ResignationEvent resignationEvent) {
        resignationDetected = resignationEvent;
    }



}
