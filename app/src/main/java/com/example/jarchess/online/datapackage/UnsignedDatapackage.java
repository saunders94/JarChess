package com.example.jarchess.online.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.resignation.Resignation;
import com.example.jarchess.match.turn.Turn;

import org.json.JSONException;
import org.json.JSONObject;


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
public class UnsignedDatapackage implements Datapackage<UnsignedDatapackage> {
    public static final UnsignedDatapackageJSONConverter JSON_CONVERTER = UnsignedDatapackageJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_TYPE = SignedDatapackage.JSON_PROPERTY_NAME_TYPE;
    public static final String JSON_PROPERTY_NAME_TURN = SignedDatapackage.JSON_PROPERTY_NAME_TURN;
    private static final Resignation RESIGNATION = new Resignation();
    private final DatapackageType datapackageType;
    private final Turn turn;


    /**
     * Creates a UnsignedDatapackage from a Turn object.
     *
     * @param turn the turn to be packaged
     */
    public UnsignedDatapackage(@NonNull Turn turn) {
        this.turn = turn;

        if (turn.getMove() == null) {
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        datapackageType = DatapackageType.TURN;
    }


    /**
     * Creates a UnsignedDatapackage from a DatapackageType object.
     *
     * @param type the type of datapackage
     */
    public UnsignedDatapackage(@NonNull DatapackageType type) {
        this.turn = null;
        datapackageType = type;
    }

    @Override
    public DatapackageType getDatapackageType() {
        return datapackageType;
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

    @Nullable
    public Move getMove() {
        return turn != null ? turn.getMove() : null;
    }

    public Resignation getResignation() {
        return RESIGNATION;
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

    public static class UnsignedDatapackageJSONConverter extends JSONConverter<UnsignedDatapackage> {


        private static UnsignedDatapackageJSONConverter instance;

        /**
         * Creates an instance of <code>UnsignedDatapackageJSONConverter</code> to construct a singleton instance
         */
        private UnsignedDatapackageJSONConverter() {
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static UnsignedDatapackageJSONConverter getInstance() {
            if (instance == null) {
                instance = new UnsignedDatapackageJSONConverter();
            }

            return instance;
        }

        @Override
        public UnsignedDatapackage convertFromJSONObject(JSONObject jsonObject) throws JSONException {

            DatapackageType type = DatapackageType.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE));

            Turn turn = null;
            if (!jsonObject.isNull(JSON_PROPERTY_NAME_TURN)) {
                turn = Turn.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN));
            }

            switch (type) {

                case TURN:
                    return new UnsignedDatapackage(turn);

                default:
                    return new UnsignedDatapackage(type);
            }
        }
    }


}
