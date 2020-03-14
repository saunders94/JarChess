package com.example.jarchess.match;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.turn.Turn;

import java.util.Iterator;
import java.util.LinkedList;

import static androidx.constraintlayout.widget.Constraints.TAG;

//TODO javadocs
//TODO finish
public class MatchHistory implements Iterable<Turn> {

    private final LinkedList<Turn> turnList = new LinkedList<>();

    public void add(Turn turn) {
        Log.d(TAG, "add is running on thread: " + Thread.currentThread().getName());
        turnList.addLast(turn);
    }

    public Move getLastMove() {
        Log.d(TAG, "getLastMove is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast().getMove();
    }

    public Turn getlastTurn() {
        Log.d(TAG, "getlastTurn is running on thread: " + Thread.currentThread().getName());
        return turnList.peekLast();
    }

    @NonNull
    @Override
    public Iterator<Turn> iterator() {
        Log.d(TAG, "iterator is running on thread: " + Thread.currentThread().getName());
        return turnList.iterator();
    }
}
