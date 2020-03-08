package com.example.jarchess.online.move;

import com.example.jarchess.match.datapackage.Datapackege;

import org.json.JSONObject;

public class MoveFormatter {

    public MoveFormatter(){

    }

    //todo User signon stuff here
    public JSONObject dataPkgToJson(Datapackege datapackege) {

//        Coordinate origin = new Coordinate();
//        Coordinate destination = new Coordinate();
//
//        Move move = datapackege.getMove();
//        long elapsedTime = datapackege.getElapsedTime();
//        Datapackege.DatapackageType type = datapackege.getDatapackageType();
//
//        Log.i("movetype", moveType);
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
//        jsonObject.put("move_type","MakeMove");
//        jsonObject.put("piece_origin",strOrigin);
//        jsonObject.put("piece_destination",strDestination);
//        jsonObject.put("piece_type","");
//
//        return jsonObject;
//

        return null;
    }

    public Datapackege jsonObjToDataPkg(JSONObject jsonObject){
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
//        Coordinate origin = Coordinate.getByFileAndRank(originFile, originRank);
//        Coordinate destination = Coordinate.getByFileAndRank(destFile, destRank);
//
//        //Move move = new StandardMove(origin, destination);
//        //Turn turn = new Turn(null, move , null);
//        //Datapackege datapackege = new Datapackege(turn);
//        return datapackage;


        return null;

    }
}
