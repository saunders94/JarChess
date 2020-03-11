package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.LocalPartipant;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationEventManager;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationListener;
import com.example.jarchess.match.result.CheckmateResult;
import com.example.jarchess.match.result.Result;
import com.example.jarchess.match.result.StalemateDrawResult;
import com.example.jarchess.match.turn.Turn;

import java.util.Collection;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

//TODO javadocs
public abstract class Match implements ResignationListener {
    private final ResignationEventManager resignationEventManager;
    private final MatchHistory matchHistory;
    private final MatchParticipant blackPlayer;
    private final MatchParticipant whitePlayer;
    private final Chessboard chessboard;
    private final MoveExpert moveExpert;
    private ChessColor winner;
    private boolean isDone;
    private Result matchResult = null;
    private LocalParticipantController localParticipantController;

    public Match(@NonNull MatchParticipant participant1, @NonNull MatchParticipant participant2) {

        resignationEventManager = new ResignationEventManager();
        resignationEventManager.addListener(this);
        resignationEventManager.addListener(participant1);
        resignationEventManager.addListener(participant2);


        chessboard = new Chessboard();
        chessboard.reset();
        this.blackPlayer = participant1.getColor() == BLACK ? participant1 : participant2;
        this.whitePlayer = participant1.getColor() == WHITE ? participant1 : participant2;

        matchHistory = new MatchHistory();
        moveExpert = MoveExpert.getInstance();
    }


    public void setWinner(ChessColor color) {

        if (winner == null) {

            winner = color;
            notifyAll();
        }

    }

    public void setLocalParticipantController(LocalParticipantController localParticipantController) {
        if (blackPlayer instanceof LocalPartipant) {
            ((LocalPartipant) blackPlayer).setController(localParticipantController);
        }
        if (whitePlayer instanceof LocalPartipant) {
            ((LocalPartipant) whitePlayer).setController(localParticipantController);
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public MatchHistory getMatchHistory() {
        return matchHistory;
    }

    public MatchParticipant getBlackPlayer() {
        return blackPlayer;
    }

    public MatchParticipant getWhitePlayer() {
        return whitePlayer;
    }

    public Piece getPieceAt(@NonNull Coordinate coordinate) {
        return chessboard.getPieceAt(coordinate);
    }

    public Turn getFirstTurn() throws ResignationException, InterruptedException {
        return whitePlayer.takeFirstTurn();
    }


    public Turn getTurn(@NonNull Turn turn) throws ResignationException, InterruptedException {
        return getParticipant(ChessColor.getOther(turn.getColor())).takeTurn(turn);
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

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public Collection<Coordinate> getPossibleMoves(Coordinate origin) {
        return moveExpert.getLegalDestinations(origin, chessboard);
    }

    public void move(Coordinate origin, Coordinate destination) {
        chessboard.move(origin, destination);
    }

    public Piece capture(Coordinate destination) {
        return chessboard.remove(destination);
    }

    public void checkForGameEnd(ChessColor nextTurnColor) {
        if (!moveExpert.hasMoves(nextTurnColor, chessboard)) {
            if (moveExpert.isInCheck(nextTurnColor, chessboard)) {
                isDone = true;
                matchResult = new CheckmateResult(ChessColor.getOther(nextTurnColor));
            } else {
                isDone = true;
                matchResult = new StalemateDrawResult();
            }
        } else {
            // TODO handle our implementation of repeated board state draw
            // I have a tendency to want to do the 5 time version that requires no

            // TODO handle 50 move draw

            // TODO imposibility of check draw handling

        }
    }

    public Result getMatchResult() {
        return matchResult;
    }
}
