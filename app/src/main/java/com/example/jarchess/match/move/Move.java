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

public class Move implements Collection<PieceMovement>, JSONConvertible<Move> {
    public static final String JSON_PROPERTY_NAME_MOVEMENTS = "movements";
    public static final MoveJSONConverter JSON_CONVERTER = MoveJSONConverter.getInstance();
    private static final int MAX_MOVEMENT_COUNT = 2;
    private static final int MIN_MOVEMENT_COUNT = 1;
    private final Collection<PieceMovement> movements;
    private static final String TAG = "Move";

    /**
     * Creates a Move with a single movement from a origin and destination <code>Coordinate</code>
     *
     * @param origin      the origin of the Move's only piece movement
     * @param destination the destination of the move's only piece movement
     */
    public Move(@NonNull Coordinate origin, @NonNull Coordinate destination) {
        movements = new LinkedList<PieceMovement>();
        add(new PieceMovement(origin, destination));
    }

    public Move(@NonNull PieceMovement pieceMovement) {
        movements = new LinkedList<PieceMovement>();
        add(pieceMovement);
    }

    public Move(Collection<PieceMovement> pieceMovements) {
        if (pieceMovements.size() < MIN_MOVEMENT_COUNT || pieceMovements.size() > 2) {
            throw new IllegalArgumentException("Expected 1 or 2 piece movements but got " + pieceMovements.size());
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
        try{
            return movements.add(pieceMovement);
        } finally {
            if (size() > MAX_MOVEMENT_COUNT) {
                throw new RuntimeException("addAll resulted in a Move with more than "+MAX_MOVEMENT_COUNT+" PieceMovements");
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        try {
            return movements.remove(o);
        } finally {
            if (size() < MIN_MOVEMENT_COUNT) {
                throw new RuntimeException("Removing the following resulted in a Move with less than "+MIN_MOVEMENT_COUNT+" PieceMovement: " + o.toString());
            }
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return movements.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends PieceMovement> c) {
        try {
            return movements.addAll(c);
        } finally {
            if (size() > MAX_MOVEMENT_COUNT) {
                throw new RuntimeException("addAll resulted in a Move with more than "+MAX_MOVEMENT_COUNT+" PieceMovements");
            }
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        try {
            return movements.removeAll(c);
        } finally {
            if (size() < MIN_MOVEMENT_COUNT) {
                throw new RuntimeException("RemoveAll resulted in a Move with less than "+MIN_MOVEMENT_COUNT+" PieceMovement");
            }
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        try {
            return movements.retainAll(c);
        } finally {
            if (size() < MIN_MOVEMENT_COUNT) {
                throw new RuntimeException("retainAll resulted in a Move with less than "+MIN_MOVEMENT_COUNT+" PieceMovement");
            }
        }
    }

    @Override
    @Deprecated
    public void clear() {
        if(MIN_MOVEMENT_COUNT > 0) {
            throw new RuntimeException("clear results in a Move with less than "+MIN_MOVEMENT_COUNT+" PieceMovement");
        }
    }

    public static class MoveJSONConverter extends JSONConverter<Move> {

        private static MoveJSONConverter instance;

        /**
         * Creates an instance of <code>MoveJSONConverter</code> to construct a singleton instance
         */
        private MoveJSONConverter() {

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
