package com.example.jarchess.online.datapackage;

import com.example.jarchess.match.turn.Turn;

import org.json.JSONException;
import org.json.JSONObject;

public class SignedDatapackage implements Datapackage<SignedDatapackage> {

    public static final SignedDatapackageJSONConverter JSON_CONVERTER = SignedDatapackageJSONConverter.getInstance();
    public static final String JSON_PROPERTY_NAME_TYPE = "type";
    public static final String JSON_PROPERTY_NAME_TURN = "turn";
    public static final String JSON_PROPERTY_NAME_SIGNING_TOKEN = "signingToken";
    public static final String JSON_PROPERTY_NAME_GAME_TOKEN = "gameToken";
    UnsignedDatapackage unsignedDatapackage;
    Object signingToken;
    Object gameToken;

    public SignedDatapackage(Turn turn, Object signingToken, Object gameToken) {
        unsignedDatapackage = new UnsignedDatapackage(turn);
        this.signingToken = signingToken;
        this.gameToken = gameToken;
    }

    public SignedDatapackage(DatapackageType type, Object signingToken, Object gameToken) {
        unsignedDatapackage = new UnsignedDatapackage(type);
        this.signingToken = signingToken;
        this.gameToken = gameToken;
    }

    public SignedDatapackage(UnsignedDatapackage unsignedDatapackage, Object signingToken, Object gameToken) {
        this.unsignedDatapackage = unsignedDatapackage;
        this.signingToken = signingToken;
        this.gameToken = gameToken;
    }

    @Override
    public DatapackageType getDatapackageType() {
        return unsignedDatapackage.getDatapackageType();
    }

    public UnsignedDatapackage getUnsignedDatapackage() {
        return unsignedDatapackage;
    }


    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = unsignedDatapackage.getJSONObject();
        jsonObject.put(JSON_PROPERTY_NAME_SIGNING_TOKEN, JSON_CONVERTER.getJSONFromSigningToken(signingToken));
        jsonObject.put(JSON_PROPERTY_NAME_GAME_TOKEN, JSON_CONVERTER.getJSONFromGameToken(gameToken));
        return jsonObject;
    }

    public static class SignedDatapackageJSONConverter extends JSONConverter<SignedDatapackage> {


        private static SignedDatapackageJSONConverter instance;

        /**
         * Creates an instance of <code>UnsignedDatapackageJSONConverter</code> to construct a singleton instance
         */
        private SignedDatapackageJSONConverter() {
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static SignedDatapackageJSONConverter getInstance() {
            if (instance == null) {
                instance = new SignedDatapackageJSONConverter();
            }

            return instance;
        }

        @Override
        public SignedDatapackage convertFromJSONObject(JSONObject jsonObject) throws JSONException {

            DatapackageType type = DatapackageType.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TYPE));
            Object signingToken = getSigningTokenFromJSON(jsonObject.getJSONObject(JSON_PROPERTY_NAME_SIGNING_TOKEN));
            Object gameToken = getGameTokenFromJSON(jsonObject.getJSONObject(JSON_PROPERTY_NAME_GAME_TOKEN));

            Turn turn = null;
            if (!jsonObject.isNull(JSON_PROPERTY_NAME_TURN)) {
                turn = Turn.JSON_CONVERTER.convertFromJSONObject(jsonObject.getJSONObject(JSON_PROPERTY_NAME_TURN));
            }


            switch (type) {

                case TURN:
                    return new SignedDatapackage(turn, signingToken, gameToken);

                default:
                    return new SignedDatapackage(type, signingToken, gameToken);
            }
        }

        private Object getGameTokenFromJSON(JSONObject jsonObject) {
            return null;//FIXME
        }

        private JSONObject getJSONFromGameToken(Object gameToken) {
            return null;//FIXME
        }

        private JSONObject getJSONFromSigningToken(Object signingToken) {
            return null;//FIXME
        }

        private Object getSigningTokenFromJSON(JSONObject jsonObject) {
            return null;//FIXME
        }
    }
}