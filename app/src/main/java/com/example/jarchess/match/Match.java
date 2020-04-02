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
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
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
import com.example.jarchess.match.result.RepetitionRuleDrawResult;
import com.example.jarchess.match.result.StalemateDrawResult;
import com.example.jarchess.match.result.XMoveRuleDrawResult;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.view.MatchView;

import java.util.Collection;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

//TODO javadocs
public abstract class Match implements MatchEndingEventListener {
    private final MatchHistory matchHistory;
    private final MatchParticipant blackPlayer;
    private final MatchParticipant whitePlayer;
    private final Chessboard chessboard;
    private final MoveExpert moveExpert;
    private final MatchClock matchClock;
    private final MatchActivity matchActivity;
    private ChessMatchResult matchChessMatchResult = null;
    private String gameToken;

    public Match(@NonNull MatchParticipant participant1, @NonNull MatchParticipant participant2, MatchClockChoice matchClockChoice, MatchActivity matchActivity) {
        this.matchActivity = matchActivity;
        chessboard = new Chessboard();
        chessboard.reset();
        this.blackPlayer = participant1.getColor() == BLACK ? participant1 : participant2;
        this.whitePlayer = participant1.getColor() == WHITE ? participant1 : participant2;

        matchHistory = new MatchHistory(whitePlayer, blackPlayer);
        moveExpert = MoveExpert.getInstance();
        moveExpert.setMatchHistory(matchHistory);
        matchClock = matchClockChoice.makeMatchClock();
        MatchEndingEventManager.getInstance().add(this);

    }

    public Piece capture(Coordinate destination) {
        return chessboard.remove(destination);
    }

    public void checkForGameEnd(ChessColor nextTurnColor) {

        if (matchChessMatchResult == null) {

            checkForTimeout();
            if (matchChessMatchResult == null) {


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
                    setMatchChessMatchResult(new FlagFallResult(colorOfWinner));
                } else if (!moveExpert.hasMoves(nextTurnColor, chessboard)) {
                    if (moveExpert.isInCheck(nextTurnColor, chessboard)) {
                        setMatchChessMatchResult(new CheckmateResult(ChessColor.getOther(nextTurnColor)));
                    } else {
                        setMatchChessMatchResult(new StalemateDrawResult());
                    }
                } else if (matchHistory.getMovesSinceCaptureOrPawnMovement(nextTurnColor) >= XMoveRuleDrawResult.FORCED_DRAW_AMOUNT) {
                    setMatchChessMatchResult(new XMoveRuleDrawResult(matchHistory.getMovesSinceCaptureOrPawnMovement(nextTurnColor)));

                } else if (matchHistory.getRepititons() >= RepetitionRuleDrawResult.FORCED_DRAW_AMOUNT) {
                    setMatchChessMatchResult(new RepetitionRuleDrawResult(matchHistory.getRepititons()));
                }
            }
        }
    }

    public void checkForTimeout() {
        if (matchClock.flagHasFallen()) {
            ChessColor colorOfWinner;
            colorOfWinner = matchClock.getColorOfFallenFlag();
            setMatchChessMatchResult(new FlagFallResult(colorOfWinner));
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
                capturedPiece = capture(matchHistory.getEnPassantRiskedPieceLocation());
                matchActivity.getMatchView().addCapturedPiece(capturedPiece);
                matchActivity.getMatchView().updatePiece(matchHistory.getEnPassantRiskedPieceLocation());
            }
            move(movement.getOrigin(), movement.getDestination());
            PromotionChoice choice = turn.getPromotionChoice();
            if (choice != null) {
                promote(destination, choice);
            }
        }
        matchHistory.add(turn, chessboard, capturedPiece, capturedPieceCoordinate);
        checkForGameEnd(ChessColor.getOther(turn.getColor()));
    }

    public ChessMatchResult getMatchChessMatchResult() {
        return matchChessMatchResult;
    }

    public void forceEndMatch(String msg) {
        MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ExceptionResult(getForceExitWinningColor(), msg, new Exception("Match end was forced"))));
    }

    public MatchParticipant getBlackParticipant() {
        return blackPlayer;
    }


    public abstract ChessColor getForceExitWinningColor();

    public Collection<? extends PieceMovement> getLegalCastleMovements(Coordinate origin, Coordinate destination) {
        return moveExpert.getLegalCastleMovements(origin, destination, chessboard);
    }

    private synchronized void setMatchChessMatchResult(ChessMatchResult matchChessMatchResult) {
        Log.d(TAG, "setMatchResult() called with: matchResult = [" + matchChessMatchResult + "]");
        Log.d(TAG, "setMatchResult is running on thread: " + Thread.currentThread().getName());
        if (this.matchChessMatchResult == null) {
            Log.i(TAG, "setMatchResult: " + matchChessMatchResult);
            this.matchChessMatchResult = matchChessMatchResult;
            notifyAll();
            MatchResultIsInEventManager.getInstance().notifyAllListeners(new MatchResultIsInEvent(matchChessMatchResult));
        }
    }

    public boolean isDone() {
        return matchChessMatchResult != null;
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
        return moveExpert.getLegalDestinations(origin, chessboard);
    }

    public Turn getTurn(@NonNull Turn turn) throws MatchActivity.MatchOverException, InterruptedException {
        return getParticipant(ChessColor.getOther(turn.getColor())).getNextTurn(turn);
    }

    public MatchParticipant getWhiteParticipant() {
        return whitePlayer;
    }

    private void playMatch() {
        Turn turn;

        MatchView matchView = matchActivity.getMatchView();

        try {
            try {
                matchClock.start();
                matchActivity.setCurrentControllerColorIfNeeded();
                turn = getWhiteParticipant().getFirstTurn();
                matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                validate(turn);
                execute(turn);
                matchView.updateViewAfter(turn);

                while (!isDone()) {
                    matchActivity.changeCurrentControllerColorIfNeeded();
                    turn = getTurn(turn);
                    matchClock.syncEnd(turn.getColor(), turn.getElapsedTime());
                    validate(turn);
                    execute(turn);
                    matchView.updateViewAfter(turn);
                }
            } catch (InterruptedException e) {
                forceEndMatch("Thread was Interrupted");
                throw new MatchActivity.MatchOverException(new ExceptionResult(getForceExitWinningColor(), "The thread was interrupted", e));
            } catch (ClockSyncException e1) {
                // match ends due to clock sync exception
                MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(new ExceptionResult(ChessColor.getOther(e1.getColorOutOfSync()), "reported time out of tolerance", e1)));
            }
        } catch (MatchActivity.MatchOverException e2) {
            // the match is over... just continue
        }

        matchClock.stop();

        matchActivity.showMatchResult();
    }

    public void move(Coordinate origin, Coordinate destination) {
        chessboard.move(origin, destination);
    }

    @Override
    public void observe(MatchEndingEvent matchEndingEvent) {

        setMatchChessMatchResult(matchEndingEvent.getResult());


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

    public void start() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playMatch();
            }
        };
        new LoggedThread(TAG, runnable, "MatchThread").start();

    }

    private void validate(Turn turn) {
        //TODO
    }
}
