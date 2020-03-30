package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Move implements Iterable<PieceMovement>, JSONConvertible<Move> {
    public static final String JSON_PROPERTY_NAME_MOVEMENTS = "movements";
    public static final MoveJSONConverter JSON_CONVERTER = MoveJSONConverter.getInstance();
    private final Collection<PieceMovement> movements;

    public Move(@NonNull Coordinate origin, @NonNull Coordinate destination) {
        movements = new LinkedList<PieceMovement>();
        add(new PieceMovement(origin, destination));
    }

    public Move(@NonNull PieceMovement pieceMovement) {
        movements = new LinkedList<PieceMovement>();
        add(pieceMovement);
    }

    public Move(@NonNull Collection<PieceMovement> pieceMovements) {
        if(pieceMovements.size() < 1 || pieceMovements.size() > 2){
            throw new IllegalArgumentException("expected 1 or 2 piece movements but got " + pieceMovements.size());
        }
        movements = new LinkedList<PieceMovement>();
        for (PieceMovement movement : pieceMovements) {
            add(movement);
        }
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (PieceMovement movement : movements) {
            jsonArray.put(movement.getJSONObject());
        }

        jsonObject.put(JSON_PROPERTY_NAME_MOVEMENTS, jsonArray);

        return jsonObject;
    }

    @Override
    public int hashCode() {
        return movements.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass() != this.getClass()){
            return false;
        }
        return movements.equals(((Move)o).movements);
    }

    @NonNull
    @Override
    public String toString() {
        String s = "";
        boolean first = true;
        for (PieceMovement movement : movements) {
            if (first) {
                first = false;
            } else {
                s += ", ";
            }
            s += movement.toString();
        }
        return s;
    }


    public int size() {
        return movements.size();
    }


    public boolean isEmpty() {
        return movements.isEmpty();
    }

    @Override
    public Iterator<PieceMovement> iterator() {
        return movements.iterator();
    }


    public boolean add(PieceMovement pieceMovement) {
        if(size() > 1){
            throw new RuntimeException("trying to add a third movement to a move");
        }
        return movements.add(pieceMovement);
    }




    public static class MoveJSONConverter extends JSONConverter<Move> {

        private static MoveJSONConverter instance;

        /**
         * Creates an instance of <code>MoveJSONConverter</code> to construct a singleton instance
         */
        private MoveJSONConverter() {
            //TODO
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static MoveJSONConverter getInstance() {
            if (instance == null) {
                instance = new MoveJSONConverter();
            }

            return instance;
        }

        @Override
        public Move convertFromJSONObject(JSONObject jsonObject) throws JSONException {

            Collection<PieceMovement> movements = new LinkedList<PieceMovement>();
            JSONArray movementsJSON = jsonObject.getJSONArray(JSON_PROPERTY_NAME_MOVEMENTS);

            for (int i = 0; i < movementsJSON.length(); i++) {
                movements.add(PieceMovement.JSON_CONVERTER.convertFromJSONObject(movementsJSON.getJSONObject(i)));
            }

            return new Move(movements);
        }
    }
}
