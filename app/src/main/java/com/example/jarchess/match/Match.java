package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.LocalPartipant;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationEventManager;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.resignation.ResignationListener;
import com.example.jarchess.match.turn.Turn;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

//TODO javadocs
public abstract class Match implements ResignationListener {
    private final ResignationEventManager resignationEventManager;
    private final MatchHistory matchHistory;
    private final MatchParticipant blackPlayer;
    private final MatchParticipant whitePlayer;
    private final Gameboard gameboard;

    private ChessColor resigningColor;
    private ChessColor winner;
    private boolean done;
    private Result matchResult = null;
    private LocalParticipantController localParticipantController;

    public Match(@NonNull MatchParticipant participant1, @NonNull MatchParticipant participant2) {

        resignationEventManager = new ResignationEventManager();
        resignationEventManager.addListener(this);
        resignationEventManager.addListener(participant1);
        resignationEventManager.addListener(participant2);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                playGame();
            }
        };


        gameboard = Gameboard.getInstance();
        gameboard.reset();
        this.blackPlayer = participant1.getColor() == BLACK ? participant1 : participant2;
        this.whitePlayer = participant1.getColor() == WHITE ? participant1 : participant2;

        matchHistory = new MatchHistory();

        new Thread(runnable, "MatchRunnableThread").start();
    }


    public void setWinner(ChessColor color) {

        if (winner == null) {

            winner = color;
            notifyAll();
        }

    }

    public void playGame() {
        Turn turn;

        try {
            turn = blackPlayer.takeFirstTurn();
            validate(turn);
            execute(turn);

            while (!done) {
                turn = whitePlayer.takeTurn(turn);
                validate(turn);
                execute(turn);

                if (!done) {
                    turn = blackPlayer.takeTurn(turn);
                    validate(turn);
                    execute(turn);
                }
            }
        } catch (ResignationException e) {
            done = true;
        }

        matchResult = new Result(this);
    }

    private void execute(Turn turn) {
        //TODO

        //make changes to gameboard

//        matchHistory.add(turn);
//
//        handleChecksAndCheckMates();
//        handleDraws();
    }

    private void validate(Turn turn) {
        //TODO
    }

    public LocalParticipantController getLocalParticipantController() {
        return localParticipantController;
    }

    public void setLocalParticipantController(LocalParticipantController localParticipantController) {
        if (blackPlayer instanceof LocalPartipant) {
            ((LocalPartipant) blackPlayer).setController(localParticipantController);
        }
        if (whitePlayer instanceof LocalPartipant) {
            ((LocalPartipant) whitePlayer).setController(localParticipantController);
        }
    }

    private boolean isDone() {
        return done;
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
        return gameboard.getPieceAt(coordinate);
    }

    public class Result {
        private final MatchParticipant blackParticipant;
        private final MatchParticipant whiteParticipant;
        private final MatchParticipant winner = null;

        public Result(Match match) {
            if (!match.isDone()) {
                throw new IllegalArgumentException("Cannot getRandom result of an in-progress match");
            }
            if (match.matchResult != null) {
                throw new IllegalArgumentException("Cannot make a match result for a match who already has a result");
            }
            this.blackParticipant = match.blackPlayer;
            this.whiteParticipant = match.whitePlayer;
        }

        public MatchParticipant getBlackParticipant() {
            return blackParticipant;
        }

        public MatchParticipant getWhiteParticipant() {
            return whiteParticipant;
        }

        public boolean wasDraw() {
            return winner == null;
        }
    }
}
