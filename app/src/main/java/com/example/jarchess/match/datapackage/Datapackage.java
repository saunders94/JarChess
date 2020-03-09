package com.example.jarchess.match.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.resignation.Resignation;
import com.example.jarchess.match.turn.Turn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;


/**
 * A datapackage is a package that contains data to be sent from one remote opponent and recieved by another.
 * <p>
 * They can contain the following:
 * <p><ul>
 * <li>A turn
 * <li>A resignation
 * <li>A pause request
 * <li>A pause approval
 * <li>A pause rejection
 * </ul>
 */
public class Datapackage implements JSONConvertable<Datapackage> {
    public static final DatapackageJSONConverter JSON_CONVERTER = new DatapackageJSONConverter();
    public static final String JSON_PROPERTY_NAME_TYPE = "type";
    public static final String JSON_PROPERTY_NAME_TURN = "turn";
    private static final Resignation RESIGNATION = new Resignation();
    private final DatapackageType datapackageType;
    private final Turn turn;


    /**
     * Creates a Datapackage from a Turn object.
     *
     * @param turn the turn to be packaged
     */
    public Datapackage(@NonNull Turn turn) {
        this.turn = turn;

        if (turn.getMove() == null) {
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        datapackageType = DatapackageType.TURN;
    }


    /**
     * Creates a Datapackage from a DatapackageType object.
     *
     * @param type the type of datapackage
     */
    public Datapackage(@NonNull DatapackageType type) {
        this.turn = null;
        datapackageType = type;
    }

    /**
     * Gets the type of datapackage this is.
     *
     * @return the type of datapackage this is
     */
    public DatapackageType getDatapackageType() {
        return datapackageType;
    }

    /**
     * Gets the turn packaged in this.
     *
     * @return the turn packaged in this, may be null.
     */
    @Nullable
    public Turn getTurn() {
        return turn;
    }

    public Move getMove() {
        return turn.getMove();
    }

    public long getElapsedTime() {
        return turn.getElapsedTime();
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_TYPE, datapackageType.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_TURN, turn.getJSONObject());
        return jsonObject;
    }

    public Resignation getResignation() {
        return RESIGNATION;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "";

        s += turn.getColor().toString();
        s += ", ";
        s += turn.getMove().toString();
        s += ", ";
        s += turn.getElapsedTime();

        return s;

    }

    public enum DatapackageType implements JSONConvertable {
        TURN(0),
        RESIGNATION(1),
        PAUSE_REQUEST(2),
        PAUSE_ACCEPT(3),
        PAUSE_REJECT(4);

        public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
        public static final String JSON_PROPERTY_NAME_NAME = "name";
        private final int intValue;

        DatapackageType(int i) {
            intValue = i;
        }

        public static DatapackageType getFromInt(int i) {
            return DatapackageType.values()[i];
        }

        public int getIntValue() {
            return intValue;
        }


        @Override
        public JSONObject getJSONObject() throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_PROPERTY_NAME_INT_VALUE, intValue);
            jsonObject.put(JSON_PROPERTY_NAME_NAME, this.name());

            return jsonObject;
        }
    }

    public static class DatapackageJSONConverter extends JSONConverter<Datapackage> {
        @Override
        public Datapackage convertFromJSONObject(JSONObject jsonObject) throws JSONException {


            JSONObject turnJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN);
            JSONObject typeJSON = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);
            int datapackageTypeInt = typeJSON.getInt(DatapackageType.JSON_PROPERTY_NAME_INT_VALUE);
            DatapackageType type = DatapackageType.getFromInt(datapackageTypeInt);

            switch (type) {

                case TURN:
                    return getDatapackageFromJSONObjectOfTurn(turnJSON);

                default:
                    return new Datapackage(type);
            }
        }

        private Datapackage getDatapackageFromJSONObjectOfTurn(JSONObject turnJSON) throws JSONException {
            JSONObject colorJSON = turnJSON.getJSONObject(Turn.JSON_PROPERTY_NAME_COLOR);
            JSONObject moveJSON = turnJSON.getJSONObject(Turn.JSON_PROPERTY_NAME_MOVE);
            long elapsedTime = turnJSON.getLong(Turn.JSON_PROPERTY_NAME_ELAPSED_TIME);
            int colorInt = colorJSON.getInt(ChessColor.JSON_PROPERTY_NAME_INT_VALUE);
            ChessColor turnColor = ChessColor.getFromInt(colorInt);

            JSONArray movementsJSON = moveJSON.getJSONArray(Move.JSON_PROPERTY_NAME_MOVEMENTS);

            Queue<PieceMovement> movements = new LinkedList<PieceMovement>();

            for (int i = 0; i < movementsJSON.length(); i++) {
                JSONObject movementJSON = movementsJSON.getJSONObject(i);
                JSONObject originJSON = movementJSON.getJSONObject(PieceMovement.JSON_PROPERTY_NAME_ORIGIN);
                JSONObject destinationJSON = movementJSON.getJSONObject(PieceMovement.JSON_PROPERTY_NAME_DESTINATION);

                int column = originJSON.getInt(Coordinate.JSON_PROPERTY_NAME_COLUMN);
                int row = originJSON.getInt(Coordinate.JSON_PROPERTY_NAME_ROW);

                Coordinate origin = Coordinate.getByColumnAndRow(column, row);


                column = destinationJSON.getInt(Coordinate.JSON_PROPERTY_NAME_COLUMN);
                row = destinationJSON.getInt(Coordinate.JSON_PROPERTY_NAME_ROW);

                Coordinate destination = Coordinate.getByColumnAndRow(column, row);

                movements.add(new PieceMovement(origin, destination));

            }

            Turn turn = new Turn(turnColor, new Move(movements), elapsedTime);
            return new Datapackage(turn);
        }
    }


}
