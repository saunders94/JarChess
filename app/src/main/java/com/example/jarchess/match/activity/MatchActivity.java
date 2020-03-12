package com.example.jarchess.match.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.JarAccount;
import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationListener;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.view.MatchView;

import java.util.Collection;
import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A match activity is an activity where two participants play a chess match with each other.
 *
 * @author Joshua Zierman
 */
public abstract class MatchActivity extends AppCompatActivity
        implements LocalParticipantController,
        SquareClickHandler,
        CommitButtonClickHandler,
        ResignationListener {

    private final Collection<Coordinate> possibleDestinations;
    // these are volatile, but need more robust synchronization
    private volatile ChessColor waitingForMove;
    private volatile Move move;
    private volatile ResignationEvent resignationDetected;
    private volatile Piece.PromotionChoice promotionChoiceInput = null;
    private Match match;
    private MatchHistory matchHistory;
    private Coordinate originInput;
    private Coordinate destinationInput;
    private MatchView matchView;
    private boolean resultWasShown = false;

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
        matchHistory = match.getMatchHistory();

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

    @Override
    public void onBackPressed() {
        if (resultWasShown) {
            super.onBackPressed();
        } else {
            matchView.showLeaveMatchDialog();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void observeSquareClick(Coordinate coordinateClicked) {
        // log the click
        Log.d(TAG, "observeSquareClick() called with: coordinateClicked = [" + coordinateClicked + "]");


        Piece clickedPiece = match.getPieceAt(coordinateClicked);
        if (waitingForMove != null) {
            // Than one of the participants is waiting for a move.

            if (clickedPiece != null && clickedPiece.getColor() == waitingForMove && originInput != coordinateClicked) {
                // If the square has a piece and it is the color of the waiting participant
                // and it is not already the current originInput,
                //
                // than we assume that the click indicates that the user intends to set that square as the new originInput of the move

                // set the originInput
                setOriginInput(coordinateClicked);

                // clear any garbage destinationInput.
                clearDestination();


            } else if (originInput != null && possibleDestinations.contains(coordinateClicked)) {

                // If the originInput was already set
                // and the square was empty or had a piece that was a different color than the participant waiting for a move to be input,
                // and the square is in the set of possible destinations
                //
                // than we assume that the click indicates that the user intends to set that square as the destinationInput of the move.

                setDestinationInput(coordinateClicked);

                if (!commitButtonClickRequired()) {
                    handleCommitButtonClick();
                }

            }

        }

    }

    private boolean commitButtonClickRequired() {
        return JarAccount.getInstance().getCommitButtonClickIsRequired();
    }

    /**
     * clears the originInput
     */
    private void clearOrigin() {
        Log.d(TAG, "clearOrigin() called");

        if (originInput == null) {
            return;
        }
        matchView.clearOriginSelectionIndicator(originInput);
        for (Coordinate possibleDestination : possibleDestinations) {
            matchView.clearPossibleDestinationIndicator(possibleDestination);
        }

        this.originInput = null;
        possibleDestinations.clear();
    }

    /**
     * clears the destinationInput
     */
    private void clearDestination() {
        Log.d(TAG, "clearDestination() called");

        if (destinationInput == null) {
            return;
        }

        matchView.clearDestinationSelectionIndicator(destinationInput);

        this.destinationInput = null;
    }

    /**
     * Sets the originInput.
     *
     * @param originInput the originInput coordinate to set to, not null
     */
    private void setOriginInput(@NonNull Coordinate originInput) {
        Log.d(TAG, "setOriginInput() called with: originInput = [" + originInput + "]");

        clearOrigin();
        clearDestination();
        this.originInput = originInput;
        possibleDestinations.addAll(match.getPossibleMoves(originInput));
        matchView.setOriginSelectionIndicator(originInput);
        for (Coordinate possibleDestination : possibleDestinations) {
            matchView.setPossibleDestinationIndicator(possibleDestination);
        }

    }

    /**
     * Sets the destinationInput.
     *
     * @param destinationInput the destinationInput coordinate to set to, not null
     */
    private void setDestinationInput(@NonNull Coordinate destinationInput) {
        Log.d(TAG, "setDestinationInput() called with: destinationInput = [" + destinationInput + "]");

        if (this.destinationInput != null) {
            matchView.setPossibleDestinationIndicator(this.destinationInput);
        }
        this.destinationInput = destinationInput;
        matchView.setDestinationSelectionIndicator(destinationInput);

    }


    @Override
    public synchronized Move getMove(ChessColor color) throws InterruptedException, ResignationException {
        Log.d(TAG, "getMove() called with: color = [" + color + "]");

        // clear all of the move related fields to make sure we start fresh.
        move = null;
        clearOrigin();
        clearDestination();

        // set the color that is waiting for move
        waitingForMove = color;

        // wait until move is made
        waitForMove();

        // return the move
        return move;
    }

    private synchronized void waitForMove() throws InterruptedException, ResignationException {
        while (move == null) {
            wait();
            if (resignationDetected != null) {
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
        } catch (InterruptedException e2) {
            match.setIsDone(true);
        }

        showMatchResutl();
    }

    private void showMatchResutl() {
        Log.d(TAG, "showMatchResutl() called");
        Log.d(TAG, "showMatchResutl: " + match.getMatchResult());
        resultWasShown = true;

        matchView.showMatchResultDialog(match.getMatchResult());
    }


    private void updateView(Turn turn) {
        Move move = turn.getMove();
        for (PieceMovement movement : move) {
            matchView.updatePiece(movement.getOrigin());
            matchView.updatePiece(movement.getDestination());
        }
    }


    private void execute(Turn turn) {
        Log.d(TAG, "execute: Thread " + Thread.currentThread());

        Move move = turn.getMove();
        for (PieceMovement movement : move) {
            Coordinate destination = movement.getDestination();
            if (match.getPieceAt(destination) != null) {
                Piece capturedPiece = match.capture(destination);
                matchView.addCapturedPiece(capturedPiece);
            }
            match.move(movement.getOrigin(), movement.getDestination());
            Piece.PromotionChoice choice = turn.getPromotionChoice();
            if (choice != null) {
                match.promote(destination, choice);
            }
        }

        matchHistory.add(turn);
        match.checkForGameEnd(ChessColor.getOther(turn.getColor()));
    }

    private void validate(Turn turn) {
        //TODO
    }

    public abstract Match createMatch();

    private synchronized void commit() {
        if (originInput != null && destinationInput != null) {

            LinkedList<PieceMovement> movements = new LinkedList<PieceMovement>();

            movements.addAll(match.getLegalCastleMovements(originInput, destinationInput));

            if (movements.isEmpty()) {
                movements.add(new PieceMovement(originInput, destinationInput));
            }
            move = new Move(movements);

            waitingForMove = null;
            this.notifyAll();
            clearDestination();
            clearOrigin();
        }
    }

    @Override
    public void handleCommitButtonClick() {
        commit();
    }

    public void observeResignButtonClick() {

        // do anything we need to do before match activity ends

        super.onBackPressed();
    }

    public void observeResultAcknowledgement() {
        // do anything we need to do before match activity ends

        super.onBackPressed();
    }

    @Override
    public synchronized Piece.PromotionChoice getPromotionChoice(Move move) throws InterruptedException {

        promotionChoiceInput = null;

        Piece p;
        for (PieceMovement movement : move) {
            p = match.getPieceAt(movement.getOrigin());


            if (p instanceof Pawn && (movement.getDestination().getRank() == 1 || movement.getDestination().getRank() == 8)) {

                this.notifyAll();
                matchView.showPawnPromotionChoiceDialog();
                while (promotionChoiceInput == null) {
                    this.wait();
                }
            }
        }

        return promotionChoiceInput;
    }

    public synchronized void setPromotionChoiceInput(Piece.PromotionChoice promoteToRook) {
        promotionChoiceInput = promoteToRook;
        this.notifyAll();
    }
}
