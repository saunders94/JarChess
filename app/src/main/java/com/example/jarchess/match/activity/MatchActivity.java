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
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.PauseResponse;
import com.example.jarchess.match.PlayerMatch;
import com.example.jarchess.match.clock.HiddenCasualMatchClock;
import com.example.jarchess.match.clock.MatchClock;
import com.example.jarchess.match.events.MatchClearableManager;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.events.PauseButtonPressedEvent;
import com.example.jarchess.match.events.PauseButtonPressedEventListener;
import com.example.jarchess.match.events.PauseButtonPressedEventManager;
import com.example.jarchess.match.events.RequestDrawButtonPressedEvent;
import com.example.jarchess.match.events.RequestDrawButtonPressedEventListener;
import com.example.jarchess.match.events.RequestDrawButtonPressedEventManager;
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
import com.example.jarchess.match.result.AgreedUponDrawResult;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.result.ResignationResult;
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
        CommitButtonClickObserver,
        PauseButtonPressedEventListener,
        RequestDrawButtonPressedEventListener {
    private static final String TAG = "MatchActivity";
    private final Collection<Coordinate> possibleDestinations;
    protected Match match;
    protected MatchView matchView;
    protected MatchClock matchClock;
    private ChessColor waitingForMove;
    private volatile Move move;
    private volatile Coordinate observedSquareClickCoordinate = null;
    private volatile boolean commitButtonHasBeenPressed = false;
    private Coordinate originInput;
    private Coordinate destinationInput;
    private volatile PromotionChoice choice = null;
    private boolean resultWasShown = false;
    private boolean inputRequestWasCanceled;
    private ChessColor currentControllerColor;
    private DrawResponse drawResponse = null;
    private PauseResponse pauseResponse = null;

    public MatchActivity() {
        this.possibleDestinations = new LinkedList<Coordinate>();
        PauseButtonPressedEventManager.getInstance().add(this);
        SquareClickEventManager.getInstance().add(this);
        RequestDrawButtonPressedEventManager.getInstance().add(this);
    }

    @Override
    public synchronized Move getMoveInput(ChessColor color) throws InterruptedException, MatchOverException {
        Log.d(TAG, "getMoveInput() called with: color = [" + color + "]");
        Log.d(TAG, "getMoveInput is running on thread: " + Thread.currentThread().getName());

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
        Log.d(TAG, "getPromotionChoice() called with: move = [" + move + "]");
        Log.d(TAG, "getPromotionChoice is running on thread: " + Thread.currentThread().getName());
        if (choice != null) {
            choice = null;
            this.notifyAll();// promotionChoiceInput has been changed
        }

        Piece p;
        for (PieceMovement movement : move) {
            p = match.getPieceAt(movement.getOrigin());


            if (p instanceof Pawn && (movement.getDestination().getRank() == 1 || movement.getDestination().getRank() == 8)) {

                if (JarAccount.getInstance().isAutomaticQueening()) {
                    return PromotionChoice.PROMOTE_TO_QUEEN;
                }
                matchView.setPromotionIndicator(movement.getDestination());
                matchView.showPawnPromotionChoiceDialog();
                while (choice == null) {
                    myWait();
                }
                matchView.clearPromotionIndicator(movement);
            }
        }

        return choice;
    }

    private synchronized void conditionallyThrowMatchOverException() throws MatchOverException {
        Log.d(TAG, "conditionallyThrowMatchOverException() called");
        Log.d(TAG, "conditionallyThrowMatchOverException is running on thread: " + Thread.currentThread().getName());
        Log.d(TAG, "conditionallyThrowMatchOverException: match is done = " + match.isDone());
        Log.d(TAG, "conditionallyThrowMatchOverException: inputRequestWasCanceled" + inputRequestWasCanceled);
        if (match.isDone() || inputRequestWasCanceled) {
            throw new MatchOverException(match.getChessMatchResult());
        }
    }

    private synchronized void exitActivityHelper() {
        Log.d(TAG, "exitActivityHelper() called");
        Log.d(TAG, "exitActivityHelper is running on thread: " + Thread.currentThread().getName());

        LoggedThread.logAllThreads();
        LoggedThread.clear();
        super.finish();

    }

    public void changeCurrentControllerColorIfNeeded() {
        if (currentControllerColor != null) {
            ChessColor nextColor = ChessColor.getOther(currentControllerColor);
            if (match.getParticipant(nextColor) instanceof LocalParticipant) {
                currentControllerColor = nextColor;
            }
        }
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

    @Override
    public synchronized DrawResponse getDrawRequestResponse() throws InterruptedException, MatchOverException {
        Log.d(TAG, "getDrawRequestResponse() called");
        Log.d(TAG, "getDrawRequestResponse is running on thread: " + Thread.currentThread().getName());
        try {
            matchView.showDrawRequestResponseDialog();
            while (drawResponse == null) {
                wait();
            }
            Log.d(TAG, "getDrawRequestResponse() returned: " + drawResponse);
            return drawResponse;
        } finally {
            drawResponse = null;
        }
    }

    protected abstract Match createMatch();

    private synchronized void exitActivity() {
        matchClock.kill(); // stops the clock
        if (!match.isDone()) {
            match.forceEndMatch("Activity was exited");
        }
        MatchClearableManager.clearAll();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                exitActivityHelper();
            }
        });
    }

    @Override
    public synchronized PauseResponse getPauseRequestResponse() throws InterruptedException, MatchOverException {
        Log.d(TAG, "getPauseRequestResponse() called");
        Log.d(TAG, "getPauseRequestResponse is running on thread: " + Thread.currentThread().getName());
        if (JarAccount.getInstance().isPausingDisabled()) {
            Log.d(TAG, "getPauseRequestResponse: pausing is disabled");
            Log.i(TAG, "getPauseRequestResponse: automatically reject pause request");
            return PauseResponse.REJECT;
        }
        try {
            Log.d(TAG, "getPauseRequestResponse: showing the dialog");
            matchView.showPauseRequestResponseDialog();
            Log.d(TAG, "getPauseRequestResponse: waiting for response to be input");
            while (pauseResponse == null) {
                wait();
            }

            if (pauseResponse.isAccepted()) {
                Log.i(TAG, "getPauseRequestResponse: Accpeted");
                matchClock.stop();
                matchView.showAcceptedPauseDialog();
                matchView.hidePauseRequestResponseDialog();
            } else {
                Log.i(TAG, "getPauseRequestResponse: Rejected");
                matchView.hidePauseRequestResponseDialog();
            }

            Log.d(TAG, "getPauseRequestResponse() returned: " + pauseResponse);
            return pauseResponse;

        } finally {
            pauseResponse = null;
        }
    }

    public synchronized ChessColor getCurrentControllerColor() {
        return currentControllerColor;
    }

    public MatchView getMatchView() {
        return matchView;
    }

    public synchronized void handleResumeButtonClick() {
        Log.d(TAG, "handleResumeButtonClick() called");
        Log.d(TAG, "handleResumeButtonClick is running on thread: " + Thread.currentThread().getName());
        if (match instanceof PlayerMatch) {
            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        ((PlayerMatch) match).handlePlayerResumeRequest();
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            }, "resumeButtonEventHandlingThread").start();
        } else {
            throw new IllegalStateException("handling resign button click on unexpected match type");
        }
    }

    private void handleSquareClick(Coordinate coordinateClicked) {
        // log the click
        Log.v(TAG, "observeSquareClick() called with: coordinateClicked = " + coordinateClicked);

        if (!matchClock.isRunning()) {
            Log.i(TAG, "handleSquareClick: Click is ignored because match clock is not running");
        } else {


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
    public synchronized void observe(RequestDrawButtonPressedEvent event) {

        final MatchActivity activity = this;
        final Thread t = new LoggedThread(TAG, new Runnable() {
            @Override
            public void run() {
                try {
                    if (activity instanceof LocalMultiplayerMatchActivity) {
                        // we don't need to check for agreement
                        MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new AgreedUponDrawResult()));

                        exitActivity();
                    } else if (match instanceof PlayerMatch) {

                        matchView.showPendingDrawDialog();
                        ((PlayerMatch) match).handlePlayerDrawRequest();
                    }

                    matchView.hidePendingDrawDialog();
                    matchView.makeDrawButtonClickable();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    LoggedThread.inputThreads.remove(Thread.currentThread());
                }
            }
        }, "drawButtonEventHandlingThread");
        LoggedThread.inputThreads.add(t);
        t.start();


    }

    @Override
    public synchronized void observe(PauseButtonPressedEvent event) {
        if (match instanceof PlayerMatch) {
            final Thread t = new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        ((PlayerMatch) match).handlePlayerPauseRequest();
                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {

                        LoggedThread.inputThreads.remove(Thread.currentThread());
                    }
                }
            }, "pauseButtonEventHandlingThread");
            LoggedThread.inputThreads.add(t);
            t.start();



        } else {
            throw new IllegalStateException("unexpected match type for pause button observation");
        }
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

    @Override
    public synchronized void observeCommitButtonClick() {

        // won't process if there is a pending square click to be processed.

        if (observedSquareClickCoordinate == null && !commitButtonHasBeenPressed)
            commitButtonHasBeenPressed = true;
        notifyAll();
    }

    public void observeResignButtonClick() {
        Log.d(TAG, "observeResignButtonClick() called");
        Log.d(TAG, "observeResignButtonClick is running on thread: " + Thread.currentThread().getName());

        LoggedThread.inputThreads.interruptAll();

        // do anything we need to do before match activity ends
        synchronized (this) {
            if (currentControllerColor != null) {
                MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ResignationResult(ChessColor.getOther(currentControllerColor))));
            }
        }

        exitActivity();
    }

    public void observeResultAcknowledgement() {
        // do anything we need to do before match activity ends

        exitActivity();
    }

    @Override
    public void onBackPressed() {

        if (resultWasShown) {
            exitActivity();
        } else {
            matchView.showLeaveMatchDialog();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // set match to the current match
        match = createMatch();

        //GUI Drawn
        matchView = new MatchView(match, this);

        matchClock = match.getMatchClock();

        if (matchClock instanceof HiddenCasualMatchClock) {
            matchView.makeClockDisappear();
            matchView.hidePauseButton();
        }
        match.start();
    }

    private synchronized void processNextInput() throws MatchOverException, InterruptedException {
        Log.d(TAG, "processNextInput() called");
        Log.d(TAG, "processNextInput is running on thread: " + Thread.currentThread().getName());
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

    public void setCurrentControllerColorIfNeeded() {

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

    /**
     * Sets the destinationInput.
     *
     * @param destination the destinationInput coordinate to set to, not null
     */
    private void setDestinationInput(@NonNull Coordinate destination) {
        Log.v(TAG, "setDestinationInput() called with: destinationInput = [" + destinationInput + "]");

        if (destinationInput != null) {
            matchView.setPossibleDestinationIndicators(possibleDestinations);
        }
        destinationInput = destination;
        matchView.setDestinationSelectionIndicator(destination);

    }

    public synchronized void setDrawResponse(DrawResponse drawResponse) {
        if (drawResponse != null && this.drawResponse == null) {
            Log.d(TAG, "setDrawResponse() called with: drawResponse = [" + drawResponse + "]");
            Log.d(TAG, "setDrawResponse is running on thread: " + Thread.currentThread().getName());
            this.drawResponse = drawResponse;
            notifyAll();
        }
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
        matchView.setOriginSelectionIndicator(origin);

    }

    public void setPauseResponse(PauseResponse pauseResponse) {
        LoggedThread.logThreadHoldingLock(this, "MatchActivity_lock");
        synchronized (this) {
            if (pauseResponse != null && this.pauseResponse == null) {
                Log.d(TAG, "setPauseResponse() called with: pauseResponse = [" + pauseResponse + "]");
                Log.d(TAG, "setPauseResponse is running on thread: " + Thread.currentThread().getName());
                this.pauseResponse = pauseResponse;
                notifyAll();
            }
        }
    }

    public synchronized void setPromotionChoiceInput(PromotionChoice promoteToRook) {
        choice = promoteToRook;
        this.notifyAll();
    }

    public synchronized void showMatchResult() {
        ChessMatchResult r = match.getChessMatchResult();
        Log.v(TAG, "showMatchResult() called");
        Log.v(TAG, "showMatchResult: " + match.getChessMatchResult());
        resultWasShown = true;


        if (r instanceof ResignationResult) {
            ChessColor resigningColor = ((ResignationResult) r).getLoserColor();
            if (match.getParticipant(resigningColor) instanceof LocalParticipant) {
                return; // without showing the result dialog because a local participant resigned
            }
        }

        if (r instanceof AgreedUponDrawResult) {
            if (this instanceof LocalMultiplayerMatchActivity) {
                return; // without showing the result dialog because the local participant agreed to draw
            }
        }

        matchView.showMatchResultDialog(match.getChessMatchResult());
    }

    private void updatePossibleInputDestinations(@NonNull Coordinate originInput) {
        matchView.clearPossibleDestinationIndicators(possibleDestinations);
        possibleDestinations.addAll(match.getPossibleMoves(originInput));
        matchView.setPossibleDestinationIndicators(possibleDestinations);
    }


}
