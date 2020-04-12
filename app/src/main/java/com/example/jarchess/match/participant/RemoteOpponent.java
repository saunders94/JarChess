package com.example.jarchess.match.participant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.MatchNetworkIO;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.resignation.ResignationReciever;
import com.example.jarchess.match.resignation.ResignationSender;
import com.example.jarchess.match.result.ExceptionResult;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.turn.TurnReceiver;
import com.example.jarchess.match.turn.TurnSender;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

/**
 * A remote opponent is a match participant that is controlled by a participant on another device.
 *
 * @author Joshua Zierman
 */
public class RemoteOpponent implements MatchParticipant {

    private final AvatarStyle avatarStyle;
    private final ChessColor color;
    //    private final Object lock; //FIXME
    private final String name;
    private final ChessColor colorOfLocalParticipant;
    private final TurnSender turnSender;
    private final Queue<Turn> recievedTurns = new ConcurrentLinkedQueue<Turn>();
    private final TurnReceiver turnReceiver;
    private final ResignationSender matchResultSender;
    private final ResignationReciever resignationReciever;
    private boolean alive;
    private static final String TAG = "RemoteOpponent";

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
        this.matchResultSender = mNIOSender;
        this.resignationReciever = mNIOReceiver;
        MatchResultIsInEventManager.getInstance().add(this);


        switch (color) {
            case BLACK:
                colorOfLocalParticipant = WHITE;
                break;
            case WHITE:
                colorOfLocalParticipant = BLACK;
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
    public Turn getFirstTurn(MatchHistory matchHistory) {
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
//
//        // send the match result
//        matchResultSender.send();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign() {
        //
    }

    @Override
    public Turn getNextTurn(MatchHistory matchHistory) throws MatchOverException {

        turnSender.send(matchHistory.getLastTurn());

        Log.i(TAG, "getNextTurn: Turn was sent to sender!");

        try {
            Log.i(TAG, "getNextTurn: Waiting for turn from receiver");
            Turn turn = turnReceiver.receiveNextTurn();
            Log.d(TAG, "getNextTurn() returned: " + turn);
            return turn;
        } catch (InterruptedException e) {
            throw new MatchOverException(new ExceptionResult(colorOfLocalParticipant, "InterruptedException", e));
        }
    }




}
