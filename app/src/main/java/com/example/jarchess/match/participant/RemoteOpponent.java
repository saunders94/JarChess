package com.example.jarchess.match.participant;

import androidx.annotation.NonNull;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.resignation.ResignationReciever;
import com.example.jarchess.match.resignation.ResignationSender;
import com.example.jarchess.match.result.ResignationResult;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.turn.TurnReceiver;
import com.example.jarchess.match.turn.TurnSender;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;
import com.example.jarchess.online.datapackage.MatchNetworkIO;

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

    /**
     * Creates a remote opponent.
     *
     * @param color                   the remote opponent's color
     * @param remoteOpponentInfoBundle the remote opponent's account
     */
    public RemoteOpponent(@NonNull ChessColor color, @NonNull RemoteOpponentInfoBundle remoteOpponentInfoBundle, DatapackageSender sender, DatapackageReceiver receiver) {

        this.name = remoteOpponentInfoBundle.getName();
        this.avatarStyle = remoteOpponentInfoBundle.getAvatarStyle();
        this.color = color;
        MatchNetworkIO.Sender mNIOSender = new MatchNetworkIO.Sender(sender, remoteOpponentInfoBundle.getIP(), remoteOpponentInfoBundle.getPort());
        MatchNetworkIO.Receiver mNIOReceiver = new MatchNetworkIO.Receiver(receiver);
        this.turnSender = mNIOSender;
        this.turnReceiver = mNIOReceiver;
        this.resignationSender = mNIOSender;
        this.resignationReciever = mNIOReceiver;
        MatchResultIsInEventManager.getInstance().add(this);


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

    @Override
    public void observe(MatchResultIsInEvent event) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign() {
        resignationSender.send(new ResignationResult(colorOfOtherParticipant));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws MatchActivity.MatchOverException {

        turnSender.send(lastTurnFromOtherParticipant);

        try {
            return turnReceiver.receiveNextTurn();
        } catch (InterruptedException e) {
            // just get out
        }

        return null;

    }




}
