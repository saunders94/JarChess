package com.example.jarchess.match;

/**
 * @author Joshua Zierman
 */
public class Coordinate {

    private static final char MIN_FILE = 'a';
    private static final char MAX_FILE = 'h';
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 8;
    private final char file;
    private final int rank;
    private final int row;
    private final int column;


    public Coordinate(char file, int rank) {

        file = Character.toLowerCase(file);
        if(file < MIN_FILE || file > MAX_FILE){
            String exceptionMessage = "Coordinate constructor expected file of 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' (case insensitive) but got '" + file + "' (" + (int)file + "). ";
            throw new IllegalArgumentException(exceptionMessage);
        }

        if(rank < MIN_RANK || rank > MAX_RANK){
            String exceptionMessage = "Coordinate constructor expected rank in range [0, 8] but got " + rank +". ";
            throw new IllegalArgumentException(exceptionMessage);
        }

        this.file = file;
        this.rank = rank;
        this.row = 8 - rank;
        this.column = file - 'a';

    }

    /**
     * Gets the file, where a file is a letter 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' representing
     * the columns where 'a' is the furthest to white's queenside (at starting position).
     *
     * @return the file
     */
    public int getFile() {
        return file;
    }

    /**
     * Gets the rank, where rank is a number in the range [1, 8] where 1 is the furthest portion
     * away from white's starting position.
     *
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets the index of the row where the row at rank 8 has index 0. In other words row 0 is the
     * row nearest to the black player.
     *
     * @return the index of the row
     */
    public int getRow() {
        return row;
    }

    /**
     * gets the index of the column where the furthest column to white's
     * queenside (at starting position) is index 0.
     *
     * @return the index of the column
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "" + file  + rank;
    }
}
