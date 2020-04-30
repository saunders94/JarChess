package com.example.jarchess.match.participant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.RemoteOpponentInfoBundle;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MatchNetworkIO;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.PauseResponse;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;

import java.io.IOException;
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
    private final String name;
    private final ChessColor colorOfLocalParticipant;
    private final MatchNetworkIO.Sender sender;
    private final Queue<Turn> recievedTurns = new ConcurrentLinkedQueue<Turn>();
    private final MatchNetworkIO.Receiver receiver;
    private boolean resultWasSent = false;

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


    public void acceptDrawRequest() {
        Log.d(TAG, "acceptDrawRequest() called");
        Log.d(TAG, "acceptDrawRequest is running on thread: " + Thread.currentThread().getName());
        sender.send(DrawResponse.ACCEPT);
    }

    public void acceptPauseRequest() {
        Log.d(TAG, "acceptPauseRequest() called");
        Log.d(TAG, "acceptPauseRequest is running on thread: " + Thread.currentThread().getName());
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Turn getFirstTurn(MatchHistory matchHistory) throws InterruptedException {

        return receiver.receiveNextTurn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public DrawResponse getDrawResponse(MatchHistory matchHistory) throws InterruptedException {
        return getDrawResponse();
    }

    @Override
    public Turn getNextTurn(MatchHistory matchHistory) throws InterruptedException {

        sender.send(matchHistory.getLastTurn());

        Log.i(TAG, "getNextTurn: Turn was sent to sender!");

        Log.i(TAG, "getNextTurn: Waiting for turn from receiver");
        Turn turn = receiver.receiveNextTurn();
        Log.d(TAG, "getNextTurn() returned: " + turn);
        return turn;
    }

    private DrawResponse getDrawResponse() throws InterruptedException {

        Log.d(TAG, "getDrawResponse() called");
        Log.d(TAG, "getDrawResponse is running on thread: " + Thread.currentThread().getName());

        Log.i(TAG, "getDrawResponse: sending draw request");
        sender.sendDrawRequest();

        Log.i(TAG, "getDrawResponse: Waiting for drawResponse from receiver");
        DrawResponse drawResponse = receiver.receiveNextDrawResponse();
        Log.d(TAG, "getDrawResponse() returned: " + drawResponse);
        return drawResponse;
    }

    public PauseResponse getPauseResponse() throws InterruptedException {

        sender.sendPauseRequest();


        Log.i(TAG, "getPauseResponse: Waiting for pauseResponse from receiver");
        PauseResponse pauseResponse = receiver.receiveNextPauseResponse();
        Log.d(TAG, "getPauseResponse() returned: " + pauseResponse);
        return pauseResponse;

    }

    public void notifyAndWaitForResume() throws InterruptedException {
        Log.d(TAG, "notifyAndWaitForResume() called");
        Log.d(TAG, "notifyAndWaitForResume is running on thread: " + Thread.currentThread().getName());
        sender.sendResumeRequest();
        receiver.receiveNextResumeRequest();

        Log.d(TAG, "notifyAndWaitForResume() returned");
    }

    public void processRemoteDrawRequest() throws MatchOverException, InterruptedException {
        Log.d(TAG, "processRemoteDrawRequest() called");
        Log.d(TAG, "processRemoteDrawRequest is running on thread: " + Thread.currentThread().getName());

        // get response from the controller
        DrawResponse drawResponse = remoteOpponentController.getDrawRequestResponseForRemoteOpponent();
        Log.i(TAG, "processRemoteDrawRequest: sending " + drawResponse);

        // send the response
        sender.send(drawResponse);
        Log.d(TAG, "processRemoteDrawRequest() returned: ");
    }

    public void processRemotePauseRequest() throws MatchOverException, InterruptedException {
        Log.d(TAG, "processRemotePauseRequest() called");
        Log.d(TAG, "processRemotePauseRequest is running on thread: " + Thread.currentThread().getName());

        // get response from the controller
        PauseResponse pauseResponse = remoteOpponentController.getPauseRequestResponseForRemoteOpponent();
        Log.i(TAG, "processRemotePauseRequest: sending " + pauseResponse);

        // send the response
        sender.send(pauseResponse);
        Log.d(TAG, "processRemotePauseRequest() returned: ");
    }

    public void rejectDrawRequest() {
        Log.d(TAG, "rejectDrawRequest() called");
        Log.d(TAG, "rejectDrawRequest is running on thread: " + Thread.currentThread().getName());
        sender.send(PauseResponse.REJECT);
    }

    public void rejectPauseRequest() {
        Log.d(TAG, "rejectPauseRequest() called");
        Log.d(TAG, "rejectPauseRequest is running on thread: " + Thread.currentThread().getName());
        sender.send(PauseResponse.REJECT);
    }

    public void send(ChessMatchResult result) {
        synchronized (this) {
            if (!resultWasSent) {
                Log.d(TAG, "send() called with: result = [" + result + "]");
                Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());

                // send the match result
                Log.i(TAG, "send: Sending Match Result!");
                sender.send(result);
                try {
                    sender.close();
                } catch (IOException e) {
                    Log.e(TAG, "send: ", e);
                }
                try {
                    receiver.close();
                } catch (IOException e) {
                    Log.e(TAG, "send: ", e);
                }
                resultWasSent = true;
                Log.d(TAG, "send() returned");
            }
        }
    }

    public void sendLastTurn(MatchHistory matchHistory) {
        sender.send(matchHistory.getLastTurn());
    }

}
