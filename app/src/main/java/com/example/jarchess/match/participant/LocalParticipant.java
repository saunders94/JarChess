package com.example.jarchess.match.participant;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.DrawResponse;
import com.example.jarchess.match.MatchOverException;
import com.example.jarchess.match.events.MatchResultIsInEvent;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.history.MatchHistory;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;

public abstract class LocalParticipant implements MatchParticipant {
    private final String name;
    private final ChessColor color;
    private final AvatarStyle avatarStyle;
    private final LocalParticipantController controller;
    private static final String TAG = "LocalParticipant";

    /**
     * Creates a local participant.
     *  @param name        the name of the participant
     * @param color       the color of the participant
     * @param avatarStyle the style of the avatar for this participant
     * @param controller
     */
    public LocalParticipant(String name, ChessColor color, AvatarStyle avatarStyle, LocalParticipantController controller) {
        this.name = name;
        this.color = color;
        this.avatarStyle = avatarStyle;
        this.controller = controller;
        MatchResultIsInEventManager.getInstance().add(this);
    }

    /**
     * Gets the name of this participant.
     *
     * @return the name of this participant
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Turn getFirstTurn(MatchHistory matchHistory) throws MatchOverException, InterruptedException {
        return getTurnFromInput();
    }

    @Override
    public Turn getNextTurn(MatchHistory lastTurnFromOtherParticipant) throws MatchOverException, InterruptedException {
        return getTurnFromInput();
    }

    /**
     * Gets the avatar resource id for this participant
     *
     * @return the avatar resource id for this participant
     */
    @Override
    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    /**
     * Gets the color of this participant.
     *
     * @return the color of this participant
     */
    @Override
    public ChessColor getColor() {
        return color;
    }


    @Override
    public DrawResponse respondToDrawRequest(MatchHistory matchHistory) {
        //TODO implement this
        return new DrawResponse(false);
    }

    /**
     * Takes a turn.
     *
     * @return the turn that this participant takes
     * @throws InterruptedException if the thread is interupted during the turn
     */
    private Turn getTurnFromInput() throws MatchOverException, InterruptedException {
        long start, end, elapsed;
        Move move;

        if (controller == null) {
            throw new IllegalStateException("controller is null when takeTurn is called");
        }

        start = TestableCurrentTime.currentTimeMillis();
        move = controller.getMoveInput(color);

        PromotionChoice promotionChoice = controller.getPromotionChoice(move);

        end = TestableCurrentTime.currentTimeMillis();

        elapsed = end - start;

        return new Turn(this.color, move, elapsed, promotionChoice);
    }

    @Override
    public void observe(MatchResultIsInEvent event) {
        Log.d(TAG, "observe() called with: event = [" + event + "]");
        Log.d(TAG, "observe is running on thread: " + Thread.currentThread().getName());
        controller.cancelInput();
        Log.d(TAG, "observe() returned: ");
    }
}
