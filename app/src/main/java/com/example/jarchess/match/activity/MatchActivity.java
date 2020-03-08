package com.example.jarchess.match.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationListener;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.view.MatchView;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A match activity is an activity where two participants play a chess match with each other.
 *
 * @author Joshua Zierman
 */
public abstract class MatchActivity extends AppCompatActivity
        implements LocalParticipantController,
        SquareClickHandler,
        CommitButtonClickHandler,
        ResignationListener{

    private final Collection <Coordinate> possibleDestinations;
    // these are volatile, but need more robust synchronization
    private volatile ChessColor waitingForMove;
    private volatile Move move;
    private volatile ResignationEvent resignationDetected;
    private Match match;
    private Coordinate origin;
    private Coordinate destination;
    private MatchView matchView;

    public MatchActivity() {
        this.possibleDestinations = new LinkedList<Coordinate>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set match to the current match
        match = createMatch();
        match.setLocalParticipantController(this);
        match.addResignationListener(this);

        setContentView(R.layout.activity_match);
        matchView = new MatchView(match, this);




        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playGame();
            }
        };
        new Thread(runnable, "MatchRunnableThread").start();

        Log.d("MatchActivity", "created");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void observeSquareClick(Coordinate coordinateClicked) {
        // log the click
        log("clicked " + coordinateClicked);


        Piece piece = match.getPieceAt(coordinateClicked);
        if (waitingForMove != null) {
            // Than one of the participants is waiting for a move.

            if (piece != null && piece.getColor() == waitingForMove && origin != coordinateClicked) {
                // If the square has a piece and it is the color of the waiting participant
                // and it is not already the current origin,
                //
                // than we assume that the click indicates that the user intends to set that square as the new origin of the move

                // set the origin
                setOrigin(coordinateClicked);

                // clear any garbage destination.
                clearDestination();


            } else if (origin != null && possibleDestinations.contains(coordinateClicked)) {
                // If the origin was already set
                // and the square was empty or had a piece that was a different color than the participant waiting for a move to be input,
                // and the square is in the set of possible destinations
                //
                // than we assume that the click indicates that the user intends to set that square as the destination of the move.

                setDestination(coordinateClicked);

            }
        }

    }

    /**
     * clears the origin
     */
    private void clearOrigin() {

        if(origin == null){
            return;
        }
        matchView.clearOriginSelectionIndicator(origin);
        for(Coordinate possibleDestination : possibleDestinations){
            matchView.clearPossibleDestinationIndicator(possibleDestination);
        }

        this.origin = null;
        possibleDestinations.clear();
        log("cleared origin");
    }

    /**
     * clears the destination
     */
    private void clearDestination() {

        if(destination == null){
            return;
        }

        matchView.clearDestinationSelectionIndicator(destination);

        this.destination = null;
        log("cleared destination");
    }

    /**
     * Sets the origin.
     *
     * @param origin the origin coordinate to set to, not null
     */
    private void setOrigin(@NonNull Coordinate origin) {

        clearOrigin();
        clearDestination();
        this.origin = origin;
        possibleDestinations.addAll(match.getPossibleMoves(origin));
        matchView.setOriginSelectionIndicator(origin);
        for(Coordinate possibleDestination: possibleDestinations){
            matchView.setPossibleDestinationIndicator(possibleDestination);
        }
        log("set origin to " + this.origin);

    }

    /**
     * Sets the destination.
     *
     * @param destination the destination coordinate to set to, not null
     */
    private void setDestination(@NonNull Coordinate destination) {

        if(this.destination != null){
            matchView.setPossibleDestinationIndicator(this.destination);
        }
        this.destination = destination;
        matchView.setDestinationSelectionIndicator(destination);
        log("set destination to " + this.destination);

    }

    /**
     * Logs a message as a debug message.
     *
     * @param msg the message to log
     */
    private void log(String msg) {
        Log.d("MatchActivity on " + Thread.currentThread().getName() + ": ", msg);
    }

////    TODO remove this if not needed
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public synchronized void requestMove(ChessColor color) {
//        log("move was requested by " + color.toString());
//        waitingForMove = color;
//    }

    @Override
    public synchronized Move getMove(ChessColor color) throws InterruptedException, ResignationException {

        log(color + " is waiting for move");

        // clear all of the move related fields to make sure we start fresh.
        move = null;
        clearOrigin();
        clearDestination();

        // set the color that is waiting for move
        waitingForMove = color;

        // wait until move is made
        waitForMove();

        log( color + " should receive move " + move);
        // return the move
        return move;
    }

    private synchronized void waitForMove() throws InterruptedException, ResignationException {
        while(move == null){
            wait();
            if(resignationDetected != null){
                throw new ResignationException(resignationDetected);
            }
        }
    }

    @Override
    public synchronized void observeResignationEvent(ResignationEvent resignationEvent) {
        resignationDetected = resignationDetected;
        this.notifyAll();
    }



    private void playGame() {
        Turn turn;


        try {
            turn = match.getFirstTurn();
            validate(turn);
            execute(turn);
            updateView(turn);

            while (!match.isDone()) {
                turn = match.getTurn(turn);
                validate(turn);
                execute(turn);
                updateView(turn);
            }
        } catch (ResignationException e) {
            match.setIsDone(true);
        } catch (InterruptedException e2){
            match.setIsDone(true);
        }
    }

    private void updateView(Turn turn) {
        Move move = turn.getMove();
        for (PieceMovement movement : move) {
            matchView.updatePiece(movement.getOrigin());
            matchView.updatePiece(movement.getDestination());
        }
    }


    private void execute(Turn turn) {

        Move move = turn.getMove();
        for (PieceMovement movement : move) {
            Coordinate destination = movement.getDestination();
            if (match.getPieceAt(destination) != null) {
                Piece capturedPiece = match.capture(destination);
                matchView.addCapturedPiece(capturedPiece);
            }
            match.move(movement.getOrigin(), movement.getDestination());
        }
    }

    private void validate(Turn turn) {
        //TODO
    }

    public abstract Match createMatch();

    @Override
    public synchronized void handleCommitButtonClick() {
            if(origin != null && destination != null){
                move = new Move(origin, destination);
                log("handle click move = " + move);
            waitingForMove = null;
            this.notifyAll();
            clearDestination();
            clearOrigin();
        }
    }
}
