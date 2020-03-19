package com.example.jarchess.online.move;

import com.example.jarchess.online.datapackage.UnsignedDatapackage;

import org.json.JSONException;
import org.json.JSONObject;

public class DatapackageFormatter {

    public DatapackageFormatter() {

    }

    //todo User signon stuff here
    public JSONObject dataPkgToJson(UnsignedDatapackage unsignedDatapackage) {

//        Coordinate origin = new Coordinate();
//        Coordinate destination = new Coordinate();
//
//        Move move = unsignedDatapackage.getDatapackage();
//        long elapsedTime = unsignedDatapackage.getElapsedTime();
//        UnsignedDatapackage.DatapackageType type = unsignedDatapackage.getDatapackageType();
//
//        Log.i("datapackageType ", type.toString());
//
//        Object[] pieceMovements = move.toArray();
//
//        PieceMovement movement1 = (PieceMovement) pieceMovements[0];
//        Coordinate originObj1 = movement1.getOrigin();
//        Coordinate destinationObj1 = movement1.getDestination();
//        Coordinate originObj2 = null;
//        Coordinate destinationObj2 = null;
//        if (pieceMovements.length > 1) {
//
//            PieceMovement movement2 = (PieceMovement) pieceMovements[0];
//
//            originObj2 = movement2.getOrigin();
//            destinationObj2 = movement2.getDestination();
//        }
//
//
//        char originFile = originObj1.getFile();
//        int originRank = originObj1.getRank();
//
//        char destinationFile = destination.getFile();
//        int destinationRank = destination.getRank();
//
//        String strOrigin = ""+ originFile + originRank;
//        String strDestination = "" + destinationFile + destinationRank;
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("requestType","MakeMove");
//        jsonObject.put("username","MakeMove");
//        jsonObject.put("signon_token","MakeMove");
//        jsonObject.put("game_token","MakeMove");
//        jsonObject.put("datapackage_type",strType);
//        jsonObject.put("piece_origin",strOrigin);
//        jsonObject.put("piece_destination",strDestination);
//        jsonObject.put("piece_type","");
//
//        return jsonObject;

        JSONObject jsonObject = null;
        try {
            jsonObject = UnsignedDatapackage.JSON_CONVERTER.convertToJSONObject(unsignedDatapackage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public UnsignedDatapackage jsonObjToDataPkg(JSONObject jsonObject) {
//
//        String strOrigin = null;
//        String strDestination = null;
//        try {
//            strOrigin = jsonObject.getString("piece_origin");
//            strDestination = jsonObject.getString("piece_destination");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        char originFile = strOrigin.charAt(0);
//        char originRank = strOrigin.charAt(1);
//        int originRankInt = Character.getNumericValue(originRank);
//        int originRow = 8 - originRankInt;
//        int originColumn = originFile - 'a';
//
//        char destFile = strDestination.charAt(0);
//        char destRank = strDestination.charAt(1);
//        int destRankInt = Character.getNumericValue(destRank);
//        int destRow = 8 - destRankInt;
//        int destColumn = destFile - 'a';
//
//        Coordinate origin1 = Coordinate.getByFileAndRank(originFile1, originRank1);
//        Coordinate destination1 = Coordinate.getByFileAndRank(destFile1, destRank1);
//        Coordinate origin2 = Coordinate.getByFileAndRank(originFile2, originRank2);
//        Coordinate destination2 = Coordinate.getByFileAndRank(destFile2, destRank2);
//
//        PieceMovement movement1 = new PieceMovement(origin1, destination1);
//        PieceMovement movement2 = new PieceMovement(origin2, destination2);
//        Turn turn = new Turn(null, move , null);
//        UnsignedDatapackage unsignedDatapackage = new UnsignedDatapackage(turn);
//        return unsignedDatapackage;
//
        UnsignedDatapackage unsignedDatapackage = null;
        try {
            unsignedDatapackage = UnsignedDatapackage.JSON_CONVERTER.convertFromJSONObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return unsignedDatapackage;

    }
}
