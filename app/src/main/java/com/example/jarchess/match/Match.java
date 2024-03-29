package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.chessboard.Chessboard;
import com.example.jarchess.match.clock.ClockSyncException;
import com.example.jarchess.match.clock.MatchClock;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventListener;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Bishop;
import com.example.jarchess.match.pieces.Knight;
import com.example.jarchess.match.pieces.Pawn;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.pieces.Queen;
import com.example.jarchess.match.pieces.Rook;
import com.example.jarchess.match.result.CheckmateResult;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.result.ExceptionResult;
import com.example.jarchess.match.result.FlagFallResult;
import com.example.jarchess.match.result.InvalidTurnReceivedResult;
import com.example.jarchess.match.result.RepetitionRuleDrawResult;
import com.example.jarchess.match.result.StalemateDrawResult;
import com.example.jarchess.match.result.XMoveRuleDrawResult;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.view.MatchView;

import java.util.Collection;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

//TODO javadocs
public abstract class Match implements MatchEndingEventListener {
    private static final String TAG = "Match";
    protected final MatchHistory matchHistory;
    protected final MatchParticipant blackPlayer;
    protected final MatchParticipant whitePlayer;
    protected final Chessboard chessboard;
    protected final MoveExpert moveExpert;
    protected final MatchClock matchClock;
    protected final MatchActivity matchActivity;
    protected ChessMatchResult chessMatchResult = null;
    private Thread matchThread;
    protected String gameToken;

    public Match(@NonNull MatchParticipant participant1, @NonNull MatchParticipant participant2, MatchClockChoice matchClockChoice, MatchActivity matchActivity) {
        this.matchActivity = matchActivity;
        chessboard = new Chessboard();
        chessboard.reset();
        this.blackPlayer = participant1.getColor() == BLACK ? participant1 : participant2;
        this.whitePlayer = participant1.getColor() == WHITE ? participant1 : participant2;

        matchHistory = new MatchHistory(whitePlayer, blackPlayer);
        moveExpert = MoveExpert.getInstance();
        matchClock = matchClockChoice.makeMatchClock();
        MatchEndingEventManager.getInstance().add(this);

    }

    private Piece capture(Coordinate destination) {
        return chessboard.remove(destination);
    }

    private void checkForGameEnd(ChessColor nextTurnColor) {

        if (chessMatchResult == null) {

            checkForTimeout();
            if (chessMatchResult == null) {


                if (matchClock.flagHasFallen()) {
                    ChessColor colorOfWinner;
                    if (matchClock.getDisplayedTimeMillis(WHITE) <= 0L) {
                        colorOfWinner = BLACK;
                    } else if (matchClock.getDisplayedTimeMillis(BLACK) <= 0L) {
                        colorOfWinner = WHITE;
                    } else {
                        String msg = "checkForGameEnd: clock flag has fallen, but neither color is at 0";
                        Log.wtf(TAG, msg);
                        throw new IllegalStateException(msg);
                    }
                    setMatchChessMatchResult(new FlagFallResult(colorOfWinner), matchHistory);
                } else if (!moveExpert.hasMoves(nextTurnColor, matchHistory)) {
                    if (moveExpert.isInCheck(nextTurnColor, matchHistory)) {
                        setMatchChessMatchResult(new CheckmateResult(ChessColor.getOther(nextTurnColor)), matchHistory);
                    } else {
                        setMatchChessMatchResult(new StalemateDrawResult(), matchHistory);
                    }
                } else if (matchHistory.getMovesSinceCaptureOrPawnMovement(nextTurnColor) >= XMoveRuleDrawResult.FORCED_DRAW_AMOUNT) {
                    setMatchChessMatchResult(new XMoveRuleDrawResult(matchHistory.getMovesSinceCaptureOrPawnMovement(nextTurnColor)), matchHistory);

                } else if (matchHistory.getRepetitions() >= RepetitionRuleDrawResult.FORCED_DRAW_AMOUNT) {
                    setMatchChessMatchResult(new RepetitionRuleDrawResult(matchHistory.getRepetitions()), matchHistory);
                }
            }
        }
    }

    private void checkForTimeout() {
        if (matchClock.flagHasFallen()) {
            ChessColor colorOfWinner;
            colorOfWinner = matchClock.getColorOfFallenFlag();
            setChessMatchResult(new FlagFallResult(colorOfWinner));
        }
    }

    private void execute(Turn turn) {
        Piece capturedPiece = null;
        Coordinate capturedPieceCoordinate = null;
        Log.v(TAG, "execute is running on thread: " + Thread.currentThread().getName());
        Move move = turn.getMove();
        for (PieceMovement movement : move) {

            Coordinate origin = movement.getOrigin();
            Coordinate destination = movement.getDestination();

            if (getPieceAt(destination) != null) {
                //perform normal capture
                capturedPieceCoordinate = destination;
                capturedPiece = capture(capturedPieceCoordinate);
                matchActivity.getMatchView().addCapturedPiece(capturedPiece);
            } else if (matchHistory.getEnPassantVulnerableCoordinate() == destination && getPieceAt(origin) instanceof Pawn) {
                // perform en passant capture
                capturedPieceCoordinate = matchHistory.getEnPassantRiskedPieceLocation();
                capturedPiece = capture(capturedPieceCoordinate);
                matchActivity.getMatchView().addCapturedPiece(capturedPiece);
                matchActivity.getMatchView().updatePiece(matchHistory.getEnPassantRiskedPieceLocation());
            }
            move(movement.getOrigin(), movement.getDestination());
            PromotionChoice choice = turn.getPromotionChoice();
            if (choice != null) {
                promote(destination, choice);
            }
        }
        matchHistory.add(turn);
        checkForGameEnd(ChessColor.getOther(turn.getColor()));
    }

    public void forceEndMatch(String msg) {
        MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ExceptionResult(getForceExitWinningColor(), msg, new Exception("Match end was forced"))));
    }

    public MatchParticipant getBlackParticipant() {
        return blackPlayer;
    }

    public abstract ChessColor getForceExitWinningColor();

    public Collection<? extends PieceMovement> getLegalCastleMovements(Coordinate origin, Coordinate destination) {
        return moveExpert.getLegalCastleMovements(origin, destination, matchHistory);
    }

    public ChessMatchResult getChessMatchResult() {
        return chessMatchResult;
    }

    protected void setChessMatchResult(ChessMatchResult chessMatchResult) {
        Log.d(TAG, "setMatchResult() called with: matchResult = [" + chessMatchResult + "]");
        Log.d(TAG, "setMatchResult is running on thread: " + Thread.currentThread().getName());

        boolean wasSet;
        Log.d(TAG, "setMatchChessMatchResult: waiting for lock");
        synchronized (this) {
            Log.d(TAG, "setMatchChessMatchResult: got lock");
            wasSet = this.chessMatchResult == null;

            if (wasSet) {
                Log.i(TAG, "setMatchResult: " + chessMatchResult);
                this.chessMatchResult = chessMatchResult;
                notifyAll();
            }
        }
        if (wasSet) {
//            MatchResultIsInEventManager.getInstance().notifyAllListeners(new MatchResultIsInEvent(matchChessMatchResult)); TODO remove all of the MatchResultIsInStuff
            matchActivity.showMatchResult();
            LoggedThread.inputThreads.interruptAll();
        }
    }

    public MatchClock getMatchClock() {
        return matchClock;
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    public MatchParticipant getParticipant(ChessColor color) {
        switch (color) {

            case BLACK:
                return blackPlayer;
            case WHITE:
                return whitePlayer;
            default:
                throw new IllegalStateException("Unexpected color value: " + color);
        }
    }

    public Piece getPieceAt(@NonNull Coordinate coordinate) {
        return chessboard.getPieceAt(coordinate);
    }

    public Collection<Coordinate> getPossibleMoves(Coordinate origin) {
        return moveExpert.getLegalDestinations(origin, matchHistory);
    }

    private Turn getTurn() throws MatchOverException, InterruptedException {
        if (matchHistory.getLastTurn() == null) {
            return getParticipant(WHITE).getFirstTurn(matchHistory);
        }
        return getParticipant(matchHistory.getNextTurnColor()).getNextTurn(matchHistory);
    }

    public MatchParticipant getWhiteParticipant() {
        return whitePlayer;
    }

    public boolean isDone() {
        return chessMatchResult != null;
    }

    public void move(Coordinate origin, Coordinate destination) {
        chessboard.move(origin, destination);
    }

    @Override
    public void observe(MatchEndingEvent matchEndingEvent) {
        setChessMatchResult(matchEndingEvent.getResult());
    }

    private void playMatch() {
        Turn turn;

        MatchView matchView = matchActivity.getMatchView();

        try {
            try {
                Log.d(TAG, "playMatch: start");
                matchClock.start();
                Log.d(TAG, "playMatch: set controller color if needed");
                matchActivity.setCurrentControllerColorIfNeeded();
                Log.d(TAG, "playMatch: get first turn");
                turn = getTurn();
                Log.d(TAG, "playMatch: syncEnd on clock");
                matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                Log.d(TAG, "playMatch: validate turn");
                validate(turn);
                Log.d(TAG, "playMatch: execute turn");
                execute(turn);
                Log.d(TAG, "playMatch: update view");
                matchView.updateViewAfter(turn);

                while (!isDone()) {
                    Log.d(TAG, "playMatch: change controller color if needed");
                    matchActivity.changeCurrentControllerColorIfNeeded();
                    Log.d(TAG, "playMatch: get next turn");
                    turn = getTurn();
                    Log.d(TAG, "playMatch: match clock syncEnd");
                    matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                    Log.d(TAG, "playMatch: validate turn");
                    validate(turn);
                    Log.d(TAG, "playMatch: execute turn");
                    execute(turn);
                    Log.d(TAG, "playMatch: update view");
                    matchView.updateViewAfter(turn);
                }

                Log.d(TAG, "playMatch: isDone() is true");

//                if (this instanceof OnlineMatch) {
//                    Log.d(TAG, "playMatch: send last turn");
//                    ((OnlineMatch) this).getOpponent().sendLastTurn(matchHistory);
//                }

            } catch (InterruptedException e) {
                Log.e(TAG, "playMatch: ", e);
                forceEndMatch("Thread was Interrupted");
                throw new MatchOverException(new ExceptionResult(getForceExitWinningColor(), "The thread was interrupted", e));
            } catch (ClockSyncException e) {
                Log.e(TAG, "playMatch: ", e);
                // match ends due to clock sync exception
                MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ExceptionResult(ChessColor.getOther(e.getColorOutOfSync()), "reported time out of tolerance", e)));
            }
        } catch (MatchOverException e) {
            Log.e(TAG, "playMatch: ", e);
            // the match is over... just continue
        }

        Log.i(TAG, "playMatch: Match should be over... stopping clock");
        matchClock.stop();
        if (this instanceof OnlineMatch) {

            Log.i(TAG, "playMatch: trying to send the match result");
            if (chessMatchResult != null) {
                ((OnlineMatch) this).getOpponent().send(chessMatchResult);
            } else {
                Log.d(TAG, "playMatch: sending a no match result exception match result");
                ((OnlineMatch) this).getOpponent().send(new ExceptionResult(null, "no match result", new IllegalStateException("no match result to send")));
            }

        }

        Log.i(TAG, "playMatch: showing match result");
        matchActivity.showMatchResult();

    }

    public void promote(Coordinate coordinate, PromotionChoice choice) {
        Log.d(TAG, "promote is running on thread: " + Thread.currentThread().getName());
        Log.d(TAG, "promote() called with: coordinate = [" + coordinate + "], choice = [" + choice + "]");
        Piece oldPiece = chessboard.getPieceAt(coordinate);

        if (oldPiece instanceof Pawn) {
            Pawn pawn = (Pawn) oldPiece;
            Piece newPiece;

            switch (choice.getPieceType()) {
                case ROOK:
                    newPiece = new Rook(pawn);
                    break;
                case KNIGHT:
                    newPiece = new Knight(pawn);
                    break;
                case BISHOP:
                    newPiece = new Bishop(pawn);
                    break;
                case QUEEN:
                    newPiece = new Queen(pawn);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value piece type from promotion choice: " + choice.getPieceType());
            }

            chessboard.remove(coordinate);
            chessboard.add(newPiece, coordinate);
        }
    }

    protected void setMatchChessMatchResult(ChessMatchResult chessMatchResult, MatchHistory matchHistory) {
        setChessMatchResult(chessMatchResult);
    }

    public void start() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playMatch();
                LoggedThread.inputThreads.remove(Thread.currentThread());
            }
        };
        matchThread = new LoggedThread(TAG, runnable, "MatchThread");
        LoggedThread.inputThreads.add(matchThread);
        matchThread.start();

    }

    private void validate(Turn turn) throws MatchOverException {
        if (!moveExpert.isLegalMove(turn.getMove(), matchHistory)) {
            ChessColor winningColor = ChessColor.getOther(turn.getColor());
            ChessMatchResult result = new InvalidTurnReceivedResult(winningColor);
            throw new MatchOverException(result);
        }
    }
}
