package com.example.jarchess.match.turn;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.datapackage.JSONConvertable;
import com.example.jarchess.match.move.Move;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A turn is the encasilation of all basic elemnts of a turn in chess.
 * <p>
 * <ul>
 * <li>It has a color representing the color of the player making a move on that turn.
 * <li>It has a move that was made during that turn.
 * <li>It has an elapsed time that passed during that turn.
 * </ul>
 *
 * @author Joshua Zierman
 */
public class Turn implements JSONConvertable<Turn> {
    public static final String JSON_PROPERTY_NAME_COLOR = "color";
    public static final String JSON_PROPERTY_NAME_MOVE = "move";
    public static final String JSON_PROPERTY_NAME_ELAPSED_TIME = "elapsedTime";
    private final ChessColor color;
    private final Move move;
    private final long elapsedTime;

    /**
     * Creates a turn.
     *
     * @param color       the color of the moving participant
     * @param move        the move being made
     * @param elapsedTime the time in milliseconds that passed during the turn
     */
    public Turn(ChessColor color, Move move, long elapsedTime) {
        this.color = color;
        this.move = move;
        this.elapsedTime = elapsedTime;
    }

    /**
     * Gets the color of the participant making a move on this turn.
     *
     * @return the color of the participant making a move on this turn.
     */
    public ChessColor getColor() {
        return color;
    }

    /**
     * Gets the move that was made during this turn.
     *
     * @return the move that was made during this turn.
     */
    public Move getMove() {
        return move;
    }

    /**
     * Gets the amount of time that elapsed during this turn in milliseconds.
     *
     * @return the amount of milliseconds that passed during this turn.
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_COLOR, color.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_MOVE, move.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_ELAPSED_TIME, elapsedTime);


        return jsonObject;
    }
}
