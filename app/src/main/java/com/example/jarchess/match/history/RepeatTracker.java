package com.example.jarchess.match.history;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.events.Clearable;
import com.example.jarchess.match.move.PieceMovement;

import java.util.Collection;
import java.util.Stack;

class RepeatTracker implements Clearable {
    private static final String TAG = "RepeatTracker";
    private final Stack<RepeatNode> repeatNodeStack = new Stack<>();
    private int maxRepeat = 0;
    private int lastRepetitionCount;

    public RepeatTracker() {
        super();
    }

    private RepeatTracker(RepeatTracker repeatTracker) {
        repeatNodeStack.addAll(repeatTracker.repeatNodeStack);
        maxRepeat = repeatTracker.maxRepeat;
        lastRepetitionCount = repeatTracker.lastRepetitionCount;
    }

    public int add(ChessColor color, Collection<PieceMovement> possiblePieceMovements) {
        int repeats = 0;
        Stack<RepeatNode> checked = new Stack<>();

        while (!repeatNodeStack.isEmpty()) {
            RepeatNode n = repeatNodeStack.pop();

            if (color == n.color &&
                    n.possiblePieceMovements.size() == possiblePieceMovements.size() &&
                    n.possiblePieceMovements.containsAll(possiblePieceMovements)
            ) {
                Log.i(TAG, "add: Repeat Found!");
                repeats = n.repeats + 1;
                break; // we don't need that found node back in the stack
            }
            checked.push(n);
        }

        while (!checked.isEmpty()) {
            repeatNodeStack.push(checked.pop());
        }

        // update max repeat
        if (repeats > maxRepeat) {
            maxRepeat = repeats;
        }

        // set last repetition count
        lastRepetitionCount = repeats;

        repeatNodeStack.push(new RepeatNode(color, possiblePieceMovements, repeats));

        return repeats;
    }

    @Override
    public void clear() {
        repeatNodeStack.clear();
    }

    public RepeatTracker getCopy() {
        return new RepeatTracker(this);
    }

    public int getLastRepetitionCount() {
        return lastRepetitionCount;
    }

    public int getMaxRepeatCount() {
        return maxRepeat;
    }

    private class RepeatNode {
        final ChessColor color;
        final Collection<PieceMovement> possiblePieceMovements;
        final int repeats;

        public RepeatNode(ChessColor color, Collection<PieceMovement> possiblePieceMovements, int repeats) {
            this.color = color;
            this.possiblePieceMovements = possiblePieceMovements;
            this.repeats = repeats;
        }
    }
}
