package com.example.jarchess.match.move;

import androidx.annotation.NonNull;

import com.example.jarchess.match.Coordinate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Move implements Collection<PieceMovement> {
    private final Collection<PieceMovement> movements;

    public Move(Coordinate origin, Coordinate destination) {
        movements = new LinkedList<PieceMovement>();
        add(new PieceMovement(origin, destination));
    }

    public Move(PieceMovement pieceMovement) {
        movements = new LinkedList<PieceMovement>();
        add(pieceMovement);
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

    @Override
    public boolean equals(Object o) {
        return movements.equals(o);
    }

    @Override
    public int hashCode() {
        return movements.hashCode();
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
}
