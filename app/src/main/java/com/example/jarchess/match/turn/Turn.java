package com.example.jarchess.match.turn;

import androidx.annotation.NonNull;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

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
public class Turn implements JSONConvertible<Turn> {
    public static final TurnJSONConverter JSON_CONVERTER = TurnJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_COLOR = "color";
    public static final String JSON_PROPERTY_NAME_MOVE = "move";
    public static final String JSON_PROPERTY_NAME_ELAPSED_TIME = "elapsedTime";
    public static final String JSON_PROPERTY_NAME_PROMOTION_CHOICE = "promotionChoice";
    private final ChessColor color;
    private final Move move;
    private final long elapsedTime;
    private final Piece.PromotionChoice promotionChoice;


    public Turn(ChessColor color, Move move, long elapsedTime, Piece.PromotionChoice promotionChoice) {
        this.color = color;
        this.move = move;
        this.elapsedTime = elapsedTime;
        this.promotionChoice = promotionChoice;
    }

    /**
     * Creates a turn.
     *
     * @param color       the color of the moving participant
     * @param move        the move being made
     * @param elapsedTime the time in milliseconds that passed during the turn
     */
    public Turn(ChessColor color, Move move, long elapsedTime) {
        this(color, move, elapsedTime, null);
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
        jsonObject.put(JSON_PROPERTY_NAME_COLOR, color == null ? JSONObject.NULL : color.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_MOVE, move == null ? JSONObject.NULL : move.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_ELAPSED_TIME, elapsedTime);
        jsonObject.put(JSON_PROPERTY_NAME_PROMOTION_CHOICE, promotionChoice == null ? JSONObject.NULL : promotionChoice.getJSONObject());


        return jsonObject;
    }

    @NonNull
    @Override
    public String toString() {
        try {
            return getJSONObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.getClass().getSimpleName() + ".toString() failed";
    }

    /**
     * Gets the move that was made during this turn.
     *
     * @return the move that was made during this turn.
     */
    public Move getMove() {
        return move;
    }

    public Piece.PromotionChoice getPromotionChoice() {
        return promotionChoice;
    }

    public static class TurnJSONConverter extends JSONConverter<Turn> {

        private static TurnJSONConverter instance;

        /**
         * Creates an instance of <code>TurnJSONConverter</code> to construct a singleton instance
         */
        private TurnJSONConverter() {
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static TurnJSONConverter getInstance() {
            if (instance == null) {
                instance = new TurnJSONConverter();
            }

            return instance;
        }

        @Override
        public Turn convertFromJSONObject(JSONObject jsonObject) throws JSONException {
            ChessColor color = ChessColor.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_COLOR));
            Move move = Move.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_MOVE));
            long elapsedTime = jsonObject.getLong(JSON_PROPERTY_NAME_ELAPSED_TIME);

            Piece.PromotionChoice promotionChoice = null;
            if (!jsonObject.isNull(JSON_PROPERTY_NAME_PROMOTION_CHOICE)) {
                JSONObject promotionChoiceJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_PROMOTION_CHOICE);
                promotionChoice = Piece.PromotionChoice.JSON_CONVERTER.convertFromJSONObject(promotionChoiceJSON);
            }
            return new Turn(color, move, elapsedTime, promotionChoice);
        }

    }
}
