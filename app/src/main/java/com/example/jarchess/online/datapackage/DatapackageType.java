package com.example.jarchess.online.datapackage;

import org.json.JSONException;
import org.json.JSONObject;

public enum DatapackageType implements JSONConvertible<DatapackageType> {
    TURN(0),
    SERVER_EXCEPTION(1),
    PAUSE_REQUEST(2),
    PAUSE_ACCEPT(3),
    PAUSE_REJECT(4),
    MATCH_RESULT(5),
    DRAW_REQUEST(6),
    DRAW_ACCEPT(7),
    DRAW_REJECT(8),
    RESUME_REQUEST(9);


    public static final String JSON_PROPERTY_NAME_INT_VALUE = "intValue";
    public static final String JSON_PROPERTY_NAME_NAME = "name";
    public static final DatapackageTypeJSONConverter JSON_CONVERTER = DatapackageTypeJSONConverter.getInstance();
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
