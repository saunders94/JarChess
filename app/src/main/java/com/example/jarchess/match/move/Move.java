package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.datapackage.JSONConvertable;
import com.example.jarchess.match.datapackage.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Move implements Collection<PieceMovement>, JSONConvertable<Move> {
    public static final String JSON_PROPERTY_NAME_MOVEMENTS = "movements";
    public static final MoveJSONConverter JSON_CONVERTER = MoveJSONConverter.getInstance();
    private final Collection<PieceMovement> movements;

    public Move(Coordinate origin, Coordinate destination) {
        movements = new LinkedList<PieceMovement>();
        add(new PieceMovement(origin, destination));
    }

    public Move(PieceMovement pieceMovement) {
        movements = new LinkedList<PieceMovement>();
        add(pieceMovement);
    }

    public Move(Collection<PieceMovement> pieceMovements) {
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
        return movements.equals(o);
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

    @Override
    public int size() {
        return movements.size();
    }

    @Override
    public boolean isEmpty() {
        return movements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return movements.contains(o);
    }

    @Override
    public Iterator<PieceMovement> iterator() {
        return movements.iterator();
    }

    @Override
    public Object[] toArray() {
        return movements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return movements.toArray(a);
    }

    @Override
    public boolean add(PieceMovement pieceMovement) {
        return movements.add(pieceMovement);
    }

    @Override
    public boolean remove(Object o) {
        return movements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return movements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PieceMovement> c) {
        return movements.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return movements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return movements.retainAll(c);
    }

    @Override
    public void clear() {
        movements.clear();
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
