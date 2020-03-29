package com.example.jarchess.match.participant;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.events.MatchResultIsInEventManager;
import com.example.jarchess.match.styles.AIAvatarStyle;
import com.example.jarchess.match.styles.AvatarStyle;

/**
 * An AI opponent is a match participant that is controlled by an algorithm.
 *
 * @author Joshua Zierman
 */
public abstract class AIOpponent implements MatchParticipant {//TODO write unit tests
    private final ChessColor color;
    private final String name;

    /**
     * Creates an AI opponent.
     *
     * @param color the color of the AI opponent
     * @param name  the name of the AI opponent
     */
    public AIOpponent(ChessColor color, String name) {
        this.color = color;
        this.name = name;

        MatchResultIsInEventManager.getInstance().add(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resign() {
        //TODO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChessColor getColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public AvatarStyle getAvatarStyle() {
        return AIAvatarStyle.getInstance();
    }
}
