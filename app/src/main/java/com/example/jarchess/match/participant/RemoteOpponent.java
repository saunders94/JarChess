package com.example.jarchess.match.participant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MatchNetworkIO;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.PauseResponse;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.result.ExceptionResult;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
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

    private static final String TAG = "RemoteOpponent";
    private final RemoteOpponentController remoteOpponentController;
    private final AvatarStyle avatarStyle;
    private final ChessColor color;
    //    private final Object lock; //FIXME
    private final String name;
    private final ChessColor colorOfLocalParticipant;
    private final MatchNetworkIO.Sender sender;
    private final Queue<Turn> recievedTurns = new ConcurrentLinkedQueue<Turn>();
    private final MatchNetworkIO.Receiver receiver;
    private boolean alive;

    private DrawResponse drawResponseFromNetwork = null;
    private PauseResponse pauseResponseFromNetwork = null;

    /**
     * Creates a remote opponent.
     *
     * @param color                    the remote opponent's color
     * @param remoteOpponentInfoBundle the remote opponent's account
     * @param remoteOpponentController
     */
    public RemoteOpponent(@NonNull ChessColor color, @NonNull RemoteOpponentInfoBundle remoteOpponentInfoBundle, DatapackageSender sender, DatapackageReceiver receiver, RemoteOpponentController remoteOpponentController) {

        this.name = remoteOpponentInfoBundle.getName();
        this.avatarStyle = remoteOpponentInfoBundle.getAvatarStyle();
        this.color = color;
        this.remoteOpponentController = remoteOpponentController;
        MatchNetworkIO.Sender mNIOSender = new MatchNetworkIO.Sender(sender, remoteOpponentInfoBundle.getIP(), remoteOpponentInfoBundle.getPort());
        MatchNetworkIO.Receiver mNIOReceiver = new MatchNetworkIO.Receiver(receiver, this);
        this.sender = mNIOSender;
        this.receiver = mNIOReceiver;
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

    public void acceptPauseRequest() {
        sender.send(PauseResponse.ACCEPT);
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

    public DrawResponse getDrawResponse() throws MatchOverException {

        sender.sendDrawRequest();

        try {
            Log.i(TAG, "getDrawResponse: Waiting for drawResponse from receiver");
            DrawResponse drawResponse = receiver.receiveNextDrawResponse();
            Log.d(TAG, "getDrawResponse() returned: " + drawResponse);
            return drawResponse;
        } catch (InterruptedException e) {
            throw new MatchOverException(new ExceptionResult(colorOfLocalParticipant, "InterruptedException", e));
        }
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
    public Turn getFirstTurn(MatchHistory matchHistory) {
        try {
            return receiver.receiveNextTurn();
        } catch (InterruptedException e) {
            // just get out
        }
        return null;
    }

    @Override
    public DrawResponse getDrawResponse(MatchHistory matchHistory) {

        //FIXME
        return null;
    }

    @Override
    public Turn getNextTurn(MatchHistory matchHistory) throws MatchOverException {

        sender.send(matchHistory.getLastTurn());

        Log.i(TAG, "getNextTurn: Turn was sent to sender!");

        try {
            Log.i(TAG, "getNextTurn: Waiting for turn from receiver");
            Turn turn = receiver.receiveNextTurn();
            Log.d(TAG, "getNextTurn() returned: " + turn);
            return turn;
        } catch (InterruptedException e) {
            throw new MatchOverException(new ExceptionResult(colorOfLocalParticipant, "InterruptedException", e));
        }
    }

    public PauseResponse getPauseResponse() throws MatchOverException {

        sender.sendPauseRequest();

        try {
            Log.i(TAG, "getPauseResponse: Waiting for pauseResponse from receiver");
            PauseResponse pauseResponse = receiver.receiveNextPauseResponse();
            Log.d(TAG, "getPauseResponse() returned: " + pauseResponse);
            return pauseResponse;
        } catch (InterruptedException e) {
            throw new MatchOverException(new ExceptionResult(colorOfLocalParticipant, "InterruptedException", e));
        }
    }

    @Override
    public void observe(MatchResultIsInEvent event) {

        // send the match result
        sender.send(event.getMatchChessMatchResult());
    }

    public void processRemoteDrawRequest() {
        Log.d(TAG, "processRemoteDrawRequest() called");
        Log.d(TAG, "processRemoteDrawRequest is running on thread: " + Thread.currentThread().getName());
        try {
            // get response from the controller
            DrawResponse drawResponse = remoteOpponentController.getDrawRequestResponseForRemoteOpponent();
            Log.i(TAG, "processRemoteDrawRequest: sending " + drawResponse);

            // send the response
            sender.send(drawResponse);
        } catch (MatchOverException e) {
            Log.e(TAG, "processRemoteDrawRequest: ", e);
            Log.i(TAG, "processRemoteDrawRequest: sending " + DrawResponse.REJECT);
            sender.send(DrawResponse.REJECT);
        } catch (InterruptedException e) {
            Log.e(TAG, "processRemoteDrawRequest: ", e);
            Log.i(TAG, "processRemoteDrawRequest: sending " + DrawResponse.REJECT);
            sender.send(DrawResponse.REJECT);
        }
        Log.d(TAG, "processRemoteDrawRequest() returned: ");
    }

    public synchronized void processRemoteDrawResponse(DrawResponse drawResponseFromNetework) {
        if (drawResponseFromNetework != null && this.drawResponseFromNetwork == null) {
            this.drawResponseFromNetwork = drawResponseFromNetework;
            notifyAll();
        }
    }

    public void processRemotePauseRequest() {
        Log.d(TAG, "processRemotePauseRequest() called");
        Log.d(TAG, "processRemotePauseRequest is running on thread: " + Thread.currentThread().getName());
        try {
            // get response from the controller
            PauseResponse pauseResponse = remoteOpponentController.getPauseRequestResponseForRemoteOpponent();
            Log.i(TAG, "processRemotePauseRequest: sending " + pauseResponse);

            // send the response
            sender.send(pauseResponse);
        } catch (MatchOverException e) {
            Log.e(TAG, "processRemotePauseRequest: ", e);
            Log.i(TAG, "processRemotePauseRequest: sending " + PauseResponse.REJECT);
            sender.send(PauseResponse.REJECT);
        } catch (InterruptedException e) {
            Log.e(TAG, "processRemotePauseRequest: ", e);
            Log.i(TAG, "processRemotePauseRequest: sending " + PauseResponse.REJECT);
            sender.send(PauseResponse.REJECT);
        }
        Log.d(TAG, "processRemotePauseRequest() returned: ");
    }

    public synchronized void processRemotePauseResponse(PauseResponse pauseResponseFromNetwork) {
        if (pauseResponseFromNetwork != null && this.pauseResponseFromNetwork == null) {
            this.pauseResponseFromNetwork = pauseResponseFromNetwork;
            notifyAll();
        }
    }

    public void rejectPauseRequest() {
        sender.send(PauseResponse.REJECT);
    }
}
