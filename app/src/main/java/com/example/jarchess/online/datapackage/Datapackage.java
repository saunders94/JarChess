package com.example.jarchess.online.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.Move;
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
    public static final String JSON_PROPERTY_NAME_DESTINATION_IP = "destinationIP";
    public static final String JSON_PROPERTY_NAME_DESTINATION_PORT = "destinationPort";
    private final DatapackageType datapackageType;
    private final Turn turn;
    private final String destinationIP;
    private final int destinationPort;


    /**
     * Creates a Datapackage from a Turn object.
     *
     * @param turn            the turn to be packaged
     * @param destinationIP
     * @param destinationPort
     */
    public Datapackage(@NonNull Turn turn, String destinationIP, int destinationPort) {
        this.turn = turn;
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;

        if (turn.getMove() == null) {
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        datapackageType = DatapackageType.TURN;
    }


    /**
     * Creates a Datapackage from a DatapackageType object.
     *
     * @param type            the type of datapackage
     * @param destinationIP
     * @param destinationPort
     */
    public Datapackage(@NonNull DatapackageType type, String destinationIP, int destinationPort) {
        this.destinationIP = destinationIP;
        this.destinationPort = destinationPort;
        this.turn = null;
        datapackageType = type;
    }

    public DatapackageType getDatapackageType() {
        return datapackageType;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public long getElapsedTime() {
        return turn.getElapsedTime();
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_TYPE, datapackageType.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_TURN, turn.getJSONObject());
        jsonObject.put(JSON_PROPERTY_NAME_DESTINATION_IP, destinationIP);
        jsonObject.put(JSON_PROPERTY_NAME_DESTINATION_PORT, destinationPort);
        return jsonObject;
    }

    @Nullable
    public Move getMove() {
        return turn != null ? turn.getMove() : null;
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

    public static class DatapackageJSONConverter extends JSONConverter<Datapackage> {


        private static DatapackageJSONConverter instance;

        /**
         * Creates an instance of <code>UnsignedDatapackageJSONConverter</code> to construct a singleton instance
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
            String destinationIP = jsonObject.getString(JSON_PROPERTY_NAME_DESTINATION_IP);
            int destinationPort = jsonObject.getInt(JSON_PROPERTY_NAME_DESTINATION_PORT);


            switch (type) {

                case TURN:

                    Turn turn = Turn.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN));

                    return new Datapackage(turn, destinationIP, destinationPort);

                default:
                    return new Datapackage(type, destinationIP, destinationPort);
            }
        }
    }


}
