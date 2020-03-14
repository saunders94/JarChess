package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.resignation.ResignationEvent;
import com.example.jarchess.match.resignation.ResignationException;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.testmode.TestableCurrentTime;

public abstract class LocalPartipant implements MatchParticipant {
    private final String name;
    private final ChessColor color;
    private final AvatarStyle avatarStyle;
    private LocalParticipantController controller;

    /**
     * Creates a local participant.
     *
     * @param name        the name of the participant
     * @param color       the color of the participant
     * @param avatarStyle the style of the avatar for this participant
     */
    public LocalPartipant(String name, ChessColor color, AvatarStyle avatarStyle) {
        this.name = name;
        this.color = color;
        this.avatarStyle = avatarStyle;
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

    /**
     * Takes the first turn from stating position.
     *
     * @return the turn that this participant takes
     * @throws ResignationException if a resignation was detected.
     * @throws InterruptedException if the thread is interrupted during the turn.
     */
    @Override
    public Turn getFirstTurn() throws ResignationException, InterruptedException {
        return getTurn();
    }

    /**
     * Takes a turn in response to the last turn from the other participant.
     *
     * @param lastTurnFromOtherParticipant the turn that happened immediately before by the other participant
     * @return the turn that this participant takes
     * @throws ResignationException if a resignation was detected.
     * @throws InterruptedException if the thread is interrupted during the turn.
     */
    @Override
    public Turn takeTurn(Turn lastTurnFromOtherParticipant) throws ResignationException, InterruptedException {
        return getTurn();
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
     * @throws ResignationException if a resignation is observed during the turn
     * @throws InterruptedException if the thread is interupted during the turn
     */
    private Turn getTurn() throws ResignationException, InterruptedException {
        long start, end, elapsed;
        Move move;

        if (controller == null) {
            throw new IllegalStateException("controller is null when takeTurn is called");
        }

        start = TestableCurrentTime.currentTimeMillis();
        move = controller.getMove(color);
        Piece.PromotionChoice promotionChoice = controller.getPromotionChoice(move);

        end = TestableCurrentTime.currentTimeMillis();

        elapsed = end - start;

        return new Turn(this.color, move, elapsed, promotionChoice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void observeResignationEvent(ResignationEvent resignationEvent) {
        //TODO
    }

    /**
     * sets the controller of this local participant
     *
     * @param controller
     */
    public void setController(LocalParticipantController controller) {
        this.controller = controller;
    }
}
