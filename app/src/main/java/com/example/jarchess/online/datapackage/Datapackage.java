package com.example.jarchess.online.datapackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.result.ChessMatchResult;
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
    public static final String JSON_PROPERTY_NAME_MATCH_RESULT = "match_result";
    private final DatapackageType datapackageType;
    private final Turn turn;
    private final ChessMatchResult matchResult;
    private final String errorMsg;


    /**
     * Creates a Datapackage from a Turn object.
     *
     * @param turn            the turn to be packaged
     */
    public Datapackage(@NonNull Turn turn) {
        this.turn = turn;
        this.matchResult = null;
        this.errorMsg = null;

        if (turn.getMove() == null) {
            throw new IllegalStateException("unexpected null move contained in a turn");
        }

        Move move = turn.getMove();
        datapackageType = DatapackageType.TURN;
    }

    /**
     * Creates a Datapackage from a DatapackageType object.
     */
    public Datapackage(DatapackageType type) {
        if (type == DatapackageType.TURN) {
            throw new IllegalArgumentException("Creating a datapackage with the type of TURN requires providing the turn as an argument");
        }
        if (type == DatapackageType.MATCH_RESULT) {
            throw new IllegalArgumentException("Creating a datapackage with the type of MATCH_RESULT requires providing the matchResult as an argument");
        }
        if (type == DatapackageType.ERROR) {
            throw new IllegalArgumentException("Creating a datapackage with the type of ERROR requires providing the errorMsg as an argument");
        }
        this.turn = null;
        this.matchResult = null;
        this.errorMsg = null;
        datapackageType = type;
    }

    public Datapackage(ChessMatchResult matchResult) {
        this.turn = null;
        this.matchResult = matchResult;
        this.errorMsg = null;
        datapackageType = DatapackageType.MATCH_RESULT;
    }

    public Datapackage(String errorMsg) {
        this.turn = null;
        this.matchResult = null;
        this.errorMsg = errorMsg;
        datapackageType = DatapackageType.ERROR;
    }

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

        if (turn != null) {
            jsonObject.put(JSON_PROPERTY_NAME_TURN, turn.getJSONObject());
        }

        if (matchResult != null) {
            jsonObject.put(JSON_PROPERTY_NAME_MATCH_RESULT, matchResult.getJSONObject());
        }

        return jsonObject;
    }

    public ChessMatchResult getMatchResult() {
        return matchResult;
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


        try {
            return getJSONObject().toString();
        } catch (JSONException e) {
            return e.toString();
        }

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

            JSONObject obj = jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE);

            DatapackageType type = DatapackageType.JSON_CONVERTER.convertFromJSONObject(obj);


            switch (type) {

                case TURN:
                    Turn turn = Turn.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN));
                    return new Datapackage(turn);

                case MATCH_RESULT:
                    ChessMatchResult result = ChessMatchResult.ResultJSONConverter.getInstance().convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_MATCH_RESULT));
                    return new Datapackage(result);

                default:
                    return new Datapackage(type);
            }
        }
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
