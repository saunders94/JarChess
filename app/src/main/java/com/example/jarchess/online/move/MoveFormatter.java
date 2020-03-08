package com.example.jarchess.online.move;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.datapackage.Datapackege;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.StandardMove;
import com.example.jarchess.match.turn.Turn;

import org.json.JSONException;
import org.json.JSONObject;

public class MoveFormatter {

    public MoveFormatter(){

    }

    //todo User signon stuff here
    public JSONObject dataPkgToJson(Datapackege datapackege){
        /*
        Coordinate origin = new Coordinate();
        Coordinate destination = new Coordinate();

        Move move = datapackege.getMove();
        long elapsedTime = datapackege.getElapsedTime();
        Datapackege.SignalType moveType = datapackege.getMoveType();

        Log.i("movetype", moveType);

        Coordinate originObj = move.getOrigin();
        Coordinate destinationObj = move.getDestination();
        char originFile = originObj.getFile();
        int originRank = originObj.getRank();

        char destinationFile = destination.getFile();
        int destinationRank = destination.getRank();

        String strOrigin = ""+ originFile + originRank;
        String strDestination = "" + destinationFile + destinationRank;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType","MakeMove");
        jsonObject.put("username","MakeMove");
        jsonObject.put("signon_token","MakeMove");
        jsonObject.put("game_token","MakeMove");
        jsonObject.put("move_type","MakeMove");
        jsonObject.put("piece_origin",strOrigin);
        jsonObject.put("Piece_destination",strDestination);
        jsonObject.put("piece_type","");

        return jsonObject;

         */
        return null;
    }

    public Datapackege jsonObjToDataPkg(JSONObject jsonObject){
        /*
        String strOrigin = null;
        String strDestination = null;
        try {
            strOrigin = jsonObject.getString("piece_origin");
            strDestination = jsonObject.getString("piece_destination");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        char originFile = strOrigin.charAt(0);
        char originRank = strOrigin.charAt(1);
        int originRankInt = Character.getNumericValue(originRank);
        int originRow = 8 - originRankInt;
        int originColumn = originFile - 'a';

        char destFile = strDestination.charAt(0);
        char destRank = strDestination.charAt(1);
        int destRankInt = Character.getNumericValue(destRank);
        int destRow = 8 - destRankInt;
        int destColumn = destFile - 'a';

        //Coordinate origin = new Coordinate(originFile, originRankInt, originColumn, originRow);
        //Coordinate destination = new Coordinate(destFile, destRank, destColumn, destRow);

        //Move move = new StandardMove(origin, destination);
        //Turn turn = new Turn(null, move , null);
        //Datapackege datapackege = new Datapackege(turn);
        return datapackage;

         */
        return null;

    }
}
