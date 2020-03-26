package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;

public abstract class LocalParticipant implements MatchParticipant {
    private final String name;
    private final ChessColor color;
    private final AvatarStyle avatarStyle;
    private final LocalParticipantController controller;

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
    public Turn getFirstTurn() throws MatchActivity.MatchOverException, InterruptedException {
        return getTurnFromInput();
    }

    @Override
    public Turn getNextTurn(Turn lastTurnFromOtherParticipant) throws MatchActivity.MatchOverException, InterruptedException {
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

    /**
     * Resigns from the match.
     */
    @Override
    public void resign() {

        //TODO
    }

    /**
     * Takes a turn.
     *
     * @return the turn that this participant takes
     * @throws InterruptedException if the thread is interupted during the turn
     */
    private Turn getTurnFromInput() throws MatchActivity.MatchOverException, InterruptedException {
        long start, end, elapsed;
        Move move;

        if (controller == null) {
            throw new IllegalStateException("controller is null when takeTurn is called");
        }

        start = TestableCurrentTime.currentTimeMillis();
        move = controller.getMoveInput(color);

        Piece.PromotionChoice promotionChoice = controller.getPromotionChoice(move);

        end = TestableCurrentTime.currentTimeMillis();

        elapsed = end - start;

        return new Turn(this.color, move, elapsed, promotionChoice);
    }
}
