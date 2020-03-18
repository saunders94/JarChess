package com.example.jarchess.match.datapackage;

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
public class Datapackage implements JSONConvertible<Datapackage> {
    public static final DatapackageJSONConverter JSON_CONVERTER = DatapackageJSONConverter.getInstance();
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

    public Move getMove() {
        return turn.getMove();
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

    public enum DatapackageType implements JSONConvertible<DatapackageType> {
        TURN(0),
        RESIGNATION(1),
        PAUSE_REQUEST(2),
        PAUSE_ACCEPT(3),
        PAUSE_REJECT(4);


        public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
        public static final String JSON_PROPERTY_NAME_NAME = "name";
        private static final DatapackageTypeJSONConverter JSON_CONVERTER = DatapackageTypeJSONConverter.getInstance();
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

        public static final class DatapackageTypeJSONConverter extends JSONConverter<DatapackageType> {

            private static DatapackageTypeJSONConverter instance;

            /**
             * Creates an instance of <code>DatapackageTypeJSONConverter</code> to construct a singleton instance
             */
            private DatapackageTypeJSONConverter() {
            }

            /**
             * Gets the instance.
             *
             * @return the instance.
             */
            public static DatapackageTypeJSONConverter getInstance() {
                if (instance == null) {
                    instance = new DatapackageTypeJSONConverter();
                }

                return instance;
            }

            @Override
            public DatapackageType convertFromJSONObject(JSONObject jsonObject) throws JSONException {
                return DatapackageType.getFromInt(jsonObject.getInt(JSON_PROPERTY_NAME_INT_VALUE));
            }
        }
    }

    public static class DatapackageJSONConverter extends JSONConverter<Datapackage> {


        private static DatapackageJSONConverter instance;

        /**
         * Creates an instance of <code>DatapackageJSONConverter</code> to construct a singleton instance
         */
        private DatapackageJSONConverter() {
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static DatapackageJSONConverter getInstance() {
            if (instance == null) {
                instance = new DatapackageJSONConverter();
            }

            return instance;
        }
        @Override
        public Datapackage convertFromJSONObject(JSONObject jsonObject) throws JSONException {

            DatapackageType type = DatapackageType.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE));

            Turn turn = null;
            if (!jsonObject.isNull(JSON_PROPERTY_NAME_TURN)) {
                turn = Turn.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN));
            }

            switch (type) {

                case TURN:
                    return new Datapackage(turn);

                default:
                    return new Datapackage(type);
            }
        }
    }


}
