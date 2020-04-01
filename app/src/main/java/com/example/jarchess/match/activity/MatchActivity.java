package com.example.jarchess.match.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.JarAccount;
import com.example.jarchess.LoggedThread;
import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchHistory;
import com.example.jarchess.match.clock.ClockSyncException;
import com.example.jarchess.match.clock.MatchClock;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.events.SquareClickEvent;
import com.example.jarchess.match.events.SquareClickEventListener;
import com.example.jarchess.match.events.SquareClickEventManager;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.LocalParticipant;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.result.ExceptionResult;
import com.example.jarchess.match.result.InvalidTurnReceivedResult;
import com.example.jarchess.match.result.ResignationResult;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.view.CommitButtonClickObserver;
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
        SquareClickEventListener,
        CommitButtonClickObserver {


    private static final String TAG = "MatchActivity";
    //    private static final long MATCH_CLOCK_TOLERANCE_MILLIS = 1000L;
    private final Collection<Coordinate> possibleDestinations;
    private volatile ChessColor waitingForMove;
    private volatile Move move;
    private volatile PromotionChoice promotionChoiceInput = null;
    private volatile Coordinate observedSquareClickCoordinate = null;
    private volatile boolean commitButtonHasBeenPressed = false;
    private Match match;
    private MatchHistory matchHistory;
    private Coordinate originInput;
    private Coordinate destinationInput;
    private MatchView matchView;
    private boolean resultWasShown = false;
    private MatchClock matchClock;
    private boolean inputRequestWasCanceled;
    private ChessColor currentControllerColor;

    @Override
    public synchronized void cancelInput() {
        Log.d(TAG, "cancelInput() called");
        Log.d(TAG, "cancelInput is running on thread: " + Thread.currentThread().getName());
        inputRequestWasCanceled = true;
        notifyAll();
    }

    @Override
    public synchronized Move getMoveInput(ChessColor color) throws InterruptedException, MatchOverException {
        Log.v(TAG, "getMove() called with: color = [" + color + "]");

        inputRequestWasCanceled = false;

        // clear all of the move related fields to make sure we start fresh.
        clearInputValues();

        // set the color that is waiting for move
        waitingForMove = color;

        // process input until the move is constructed
        while (move == null) {
            processNextInput();
        }

        // return the move
        return move;
    }

    @Override
    public synchronized PromotionChoice getPromotionChoice(Move move) throws InterruptedException, MatchOverException {

        if (promotionChoiceInput != null) {
            promotionChoiceInput = null;
            this.notifyAll();// promotionChoiceInput has been changed
        }

        Piece p;
        for (PieceMovement movement : move) {
            p = match.getPieceAt(movement.getOrigin());


            if (p instanceof Pawn && (movement.getDestination().getRank() == 1 || movement.getDestination().getRank() == 8)) {

                matchView.setPromotionIndicator(movement.getDestination());
                matchView.showPawnPromotionChoiceDialog();
                while (promotionChoiceInput == null) {
                    myWait();
                }
                matchView.clearPromotionIndicator(movement);
            }
        }

        return promotionChoiceInput;
    }

    public MatchActivity() {
        this.possibleDestinations = new LinkedList<Coordinate>();
        SquareClickEventManager.getInstance().add(this);
    }

    private void changeCurrentControllerColorIfNeeded() {
        if (currentControllerColor != null) {
            ChessColor nextColor = ChessColor.getOther(currentControllerColor);
            if (match.getParticipant(nextColor) instanceof LocalParticipant) {
                currentControllerColor = nextColor;
            }
        }
    }

    private synchronized void conditionallyThrowMatchOverException() throws MatchOverException {
        if (match.isDone() || inputRequestWasCanceled) {
            throw new MatchOverException(match.getMatchChessMatchResult());
        }
    }

    private synchronized void exitActivity() {
        matchClock.kill(); // stops the clock
        if (!match.isDone()) {
            match.forceEndMatch("Activity was exited");
        }
        super.onBackPressed();
    }

    /**
     * clears the destinationInput
     */
    private void clearDestinationInput() {
        Log.v(TAG, "clearDestination() called");

        if (destinationInput == null) {
            return;
        }

        matchView.clearDestinationSelectionIndicator(destinationInput);

        this.destinationInput = null;
    }

    private void clearInputValues() {
        move = null;
        clearOriginInput();
        clearDestinationInput();
    }

    /**
     * clears the originInput
     */
    private void clearOriginInput() {
        Log.v(TAG, "clearOrigin() called");

        if (originInput == null) {
            return;
        }
        matchView.clearOriginSelectionIndicator(originInput);
        matchView.clearPossibleDestinationIndicators(possibleDestinations);
        matchView.clearDestinationSelectionIndicator(destinationInput);


        this.originInput = null;
        possibleDestinations.clear();
    }

    private void clearPossibleInputDestinations() {
    }

    private synchronized void commit() {
        if (originInput != null && destinationInput != null) {
            PieceMovement movement = new PieceMovement(originInput, destinationInput);

            LinkedList<PieceMovement> movements = new LinkedList<PieceMovement>(match.getLegalCastleMovements(originInput, destinationInput));

            if (movements.isEmpty()) {
                movements.add(movement);
            }
            move = new Move(movements);
            waitingForMove = null;
            this.notifyAll();

            clearDestinationInput();
            clearOriginInput();

            // update the view to show changes immediately (before match verifies the move is legal)
            for (PieceMovement m : movements) {
                matchView.updateViewBefore(m);
            }
        }
    }

    private boolean commitButtonClickRequired() {
        return JarAccount.getInstance().getCommitButtonClickIsRequired();
    }

    public synchronized ChessColor getCurrentControllerColor() {
        return currentControllerColor;
    }

    protected abstract Match createMatch();

    private void execute(Turn turn) {
        Log.v(TAG, "execute is running on thread: " + Thread.currentThread().getName());
        Move move = turn.getMove();
        for (PieceMovement movement : move) {

            Coordinate origin = movement.getOrigin();
            Coordinate destination = movement.getDestination();

            if (match.getPieceAt(destination) != null) {
                //perform normal capture
                Piece capturedPiece = match.capture(destination);
                matchView.addCapturedPiece(capturedPiece);
            } else if (matchHistory.getEnPassantVulnerableCoordinate() == destination && match.getPieceAt(origin) instanceof Pawn) {
                // perform en passant capture
                Piece capturedPiece = match.capture(matchHistory.getEnPassentRiskedPieceLocation());
                matchView.addCapturedPiece(capturedPiece);
                matchView.updatePiece(matchHistory.getEnPassentRiskedPieceLocation());
            }
            match.move(movement.getOrigin(), movement.getDestination());
            PromotionChoice choice = turn.getPromotionChoice();
            if (choice != null) {
                match.promote(destination, choice);
            }
        }
        matchHistory.add(turn);
        match.checkForGameEnd(ChessColor.getOther(turn.getColor()));
    }

    public synchronized void observeResignButtonClick() {

        // do anything we need to do before match activity ends
        if (currentControllerColor != null) {
            MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ResignationResult(ChessColor.getOther(currentControllerColor))));
        }

        exitActivity();
    }

    private void handleSquareClick(Coordinate coordinateClicked) {
        // log the click
        Log.v(TAG, "observeSquareClick() called with: coordinateClicked = " + coordinateClicked);


        Piece clickedPiece = match.getPieceAt(coordinateClicked);
        if (waitingForMove != null) {
            // Than one of the participants is waiting for a move.

            if (isNewOrginInput(coordinateClicked, clickedPiece)) {
                Log.v(TAG, "handleSquareClick: treating the coordinate as newly selected origin");
                // If the square has a piece and it is the color of the waiting participant
                // and it is not already the current originInput,
                //
                // than we assume that the click indicates that the user intends to set that square as the new originInput of the move

                // set the originInput
                clearOriginInput();
                clearDestinationInput();
                clearPossibleInputDestinations();
                setOriginInput(coordinateClicked);
                updatePossibleInputDestinations(originInput);


            } else if (isNewDestinationInput(coordinateClicked)) {
                Log.v(TAG, "handleSquareClick: Treating the coordinate as newly selected destination");
                // If the originInput was already set
                // and the square was empty or had a piece that was a different color than the participant waiting for a move to be input,
                // and the square is in the set of possible destinations
                //
                // than we assume that the click indicates that the user intends to set that square as the destinationInput of the move.

                setDestinationInput(coordinateClicked);

                if (!commitButtonClickRequired()) {
                    Log.v(TAG, "handleSquareClick: commit button is not required, so commit is immediate");
                    observeCommitButtonClick();
                }

            } else {
                Log.v(TAG, "handleSquareClick: The coordinate is not a new origin or destination");
            }
        }
    }

    private boolean isNewDestinationInput(Coordinate coordinateClicked) {
        return originInput != null && destinationInput != coordinateClicked && possibleDestinations.contains(coordinateClicked);
    }

    private boolean isNewOrginInput(Coordinate coordinateClicked, Piece clickedPiece) {
        return clickedPiece != null && clickedPiece.getColor() == waitingForMove && originInput != coordinateClicked;
    }

    private void myWait() throws InterruptedException, MatchOverException {
        this.wait();
        conditionallyThrowMatchOverException();
    }

    @Override
    public synchronized void observeCommitButtonClick() {

        // won't process if there is a pending square click to be processed.

        if (observedSquareClickCoordinate == null && !commitButtonHasBeenPressed)
            commitButtonHasBeenPressed = true;
        notifyAll();
    }

    @Override
    public void onBackPressed() {

        if (resultWasShown) {
            exitActivity();
        } else {
            matchView.showLeaveMatchDialog();
        }
    }

    public void observeResultAcknowledgement() {
        // do anything we need to do before match activity ends

        exitActivity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void observe(SquareClickEvent event) {

        // won't process if there is a pending commit button click to be processed.
        Coordinate coordinateClicked = event.getCoordinateClicked();
        if (observedSquareClickCoordinate != coordinateClicked && !commitButtonHasBeenPressed) {
            observedSquareClickCoordinate = coordinateClicked;
            Log.v(TAG, "observeSquareClick: at " + coordinateClicked);
            this.notifyAll();
        }

    }

    private void playMatch() {
        Turn turn;


        try {
            try {
                matchClock.start();
                setCurrentControllerColorIfNeeded();
                turn = match.getWhitePlayer().getFirstTurn();
                matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                validate(turn);
                execute(turn);
                matchView.updateViewAfter(turn);

                while (!match.isDone()) {
                    changeCurrentControllerColorIfNeeded();
                    turn = match.getTurn(turn);
                    matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                    validate(turn);
                    execute(turn);
                    matchView.updateViewAfter(turn);
                }
            } catch (InterruptedException e) {
                match.forceEndMatch("Thread was Interrupted");
                throw new MatchOverException(new ExceptionResult(match.getForceExitWinningColor(), "The thread was interrupted", e));
            } catch (ClockSyncException e1) {
                // match ends due to clock sync exception
                MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ExceptionResult(ChessColor.getOther(e1.getColorOutOfSync()), "reported time out of tolerance", e1)));
            }
        } catch (MatchOverException e2) {
            // the match is over... just continue
        }

        matchClock.stop();

        showMatchResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set match to the current match
        match = createMatch();
        matchHistory = match.getMatchHistory();

        //GUI Drawn
        setContentView(R.layout.activity_match);
        matchView = new MatchView(match, this);

        matchClock = match.getMatchClock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playMatch();
            }
        };
        new LoggedThread(TAG, runnable, "MatchThread").start();

        Log.v("MatchActivity", "created");
    }

    private void setCurrentControllerColorIfNeeded() {

        if (currentControllerColor == null) {
            ChessColor nextColor = ChessColor.WHITE;
            if (match.getParticipant(nextColor) instanceof LocalParticipant) {
                currentControllerColor = nextColor;
            }
        }

        if (currentControllerColor == null) {
            ChessColor nextColor = ChessColor.BLACK;
            if (match.getParticipant(nextColor) instanceof LocalParticipant) {
                currentControllerColor = nextColor;
            }
        }
    }

    private void processNextInput() throws MatchOverException, InterruptedException {
        Coordinate coordinateToProcess = null;
        boolean commitButtonPressNeedsProcessing = false;
        synchronized (this) {
            while (observedSquareClickCoordinate == null && !commitButtonHasBeenPressed) {
                myWait();
            }
            if (observedSquareClickCoordinate != null) {
                coordinateToProcess = observedSquareClickCoordinate;
                observedSquareClickCoordinate = null;
                notifyAll();
            }

            if (commitButtonHasBeenPressed) {
                commitButtonPressNeedsProcessing = true;
                commitButtonHasBeenPressed = false;
                notifyAll();
            }
        }
        if (coordinateToProcess != null && commitButtonPressNeedsProcessing) {
            String msg = "processNextInput: Both square click and commit button click needs processing";
            Log.e(TAG, msg);
            throw new IllegalStateException(msg);
        }

        // handle commit
        if (commitButtonPressNeedsProcessing) {
            if (originInput != destinationInput && originInput != null && destinationInput != null) {
                commit();
            }
        } else if (coordinateToProcess != null) {

            handleSquareClick(coordinateToProcess);
        }

    }

    /**
     * Sets the destinationInput.
     *
     * @param destination the destinationInput coordinate to set to, not null
     */
    private void setDestinationInput(@NonNull Coordinate destination) {
        Log.v(TAG, "setDestinationInput() called with: destinationInput = [" + destinationInput + "]");

        if (destinationInput != null) {
            matchView.clearDestinationSelectionIndicator(destinationInput);
        }
        destinationInput = destination;
        matchView.setDestinationSelectionIndicator(destination);

    }

    /**
     * Sets the originInput.
     *
     * @param origin the originInput coordinate to set to, not null
     */
    private void setOriginInput(@NonNull Coordinate origin) {
        Log.v(TAG, "setOriginInput() called with: originInput = [" + origin + "]");

        if (originInput != null) {
            matchView.clearOriginSelectionIndicator(originInput);
        }
        originInput = origin;
        matchView.updateAfterSettingOrigin(origin);

    }

    public synchronized void setPromotionChoiceInput(PromotionChoice promoteToRook) {
        promotionChoiceInput = promoteToRook;
        this.notifyAll();
    }

    private synchronized void showMatchResult() {
        ChessMatchResult r = match.getMatchChessMatchResult();
        Log.v(TAG, "showMatchResult() called");
        Log.v(TAG, "showMatchResult: " + match.getMatchChessMatchResult());
        resultWasShown = true;

        if (r instanceof ResignationResult) {
            ChessColor resigningColor = ((ResignationResult) r).getLoserColor();
            if (match.getParticipant(resigningColor) instanceof LocalParticipant) {
                return; // without showing the result dialog because a local participant resigned
            }
        }

        matchView.showMatchResultDialog(match.getMatchChessMatchResult());
    }

    private void updatePossibleInputDestinations(@NonNull Coordinate originInput) {
        matchView.clearPossibleDestinationIndicators(possibleDestinations);
        possibleDestinations.addAll(match.getPossibleMoves(originInput));
        matchView.updateAfterSettingPossibleDestinations(possibleDestinations);
    }

    private void validate(Turn turn) {
        //TODO
    }

    public static class MatchOverException extends Exception {

        private final ChessMatchResult matchChessMatchResult;

        public MatchOverException(ChessMatchResult matchChessMatchResult) {
            super("MatchOverException with result: " + matchChessMatchResult);
            this.matchChessMatchResult = matchChessMatchResult;
        }

        public ChessMatchResult getMatchChessMatchResult() {
            return matchChessMatchResult;
        }
    }
}
