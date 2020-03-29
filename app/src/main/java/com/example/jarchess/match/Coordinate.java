package com.example.jarchess.match;

import androidx.annotation.NonNull;

import com.example.jarchess.online.datapackage.JSONConverter;
import com.example.jarchess.online.datapackage.JSONConvertible;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * A coordinate represents a positon on a chessboard.
 * <p>
 * The descriptions that follow assume that a chessboard is shown with the black player's starting row
 * positioned at the top, and the white player's starting row at the bottom. King starting positions
 * are on the right side of queen starting positions in this orientation.
 * <p>
 * Files are a way to describe the columns on the chessboard. They are represented with the letters
 * a, b, c, d, e, f, g, and h. The furthest file to the left is a and the furthest file to the right is h.
 * <p>
 * Ranks are a way to describe the row on the chessboard. They are represented with the numbers
 * 1, 2, 3, 4, 5, 6, 7, and 8. The furthest rank to the bottom on the chessboard is rank 1 and the
 * furthest to the top is 8.
 * <p>
 * Rows and Columns are used like indexes in a 2D array so that (column 0, row 0) represents the top
 * left square on the chessboard. Valid rows are 0, 1, 2, 3, 4, 5, 6, and 7. Valid columns are
 * 0, 1, 2, 3, 4, 5, 6, and 7. The furthest row to the top on the chessboard is 0, and the furthest
 * to the bottom is 7. The furthest column to the left is 0, and the furthest to the right is 7.
 *
 * @author Joshua Zierman
 */
public class Coordinate implements JSONConvertible<Coordinate> {

    // File Constants
    /**
     * The lowest valued file.
     * <p>
     * This is the furthest to the left file when black's starting position is at the top.
     */
    public static final char MIN_FILE = 'a';

    /**
     * The highest valued file.
     * <p>
     * This is the furthest to the right file when black's starting position is at the top.
     */
    public static final char MAX_FILE = 'h';

    /**
     * The array of all possible files.
     */
    public static final char[] FILES = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};


    // Column Constants
    /**
     * The lowest valued column.
     * <p>
     * This is the furthest to the left column when black's starting position is at the top.
     */
    public static final int MIN_COLUMN = 0;

    /**
     * the highest valued column.
     * <p>
     * This is the furthest to the right column when black's starting position is at the top.
     */
    public static final int MAX_COLUMN = 7;

    /**
     * The array of all possible columns.
     */
    public static final int[] COLUMNS = new int[]{0, 1, 2, 3, 4, 5, 6, 7};


    // Rank Constants

    /**
     * The lowest valued rank.
     * <p>
     * This is the furthest to the bottom rank when black's starting position is at the top.
     */
    public static final int MIN_RANK = 1;

    /**
     * The highest valued rank.
     * <p>
     * This is the furthest to the top rank when black's starting position is at the top.
     */
    public static final int MAX_RANK = 8;

    /**
     * The array of all ranks.
     */
    public static final int[] RANKS = new int[]{1, 2, 3, 4, 5, 6, 7, 8};


    // Row Constants

    /**
     * The lowest valued row.
     * <p>
     * This is the furthest to the top row when black's starting position is at the top.
     */
    public static final int MIN_ROW = 0;

    /**
     * The highest valued row.
     * <p>
     * This is the furthest to the bottom row when black's starting position is at the top.
     */
    public static final int MAX_ROW = 7;

    /**
     * The array of all possible row values.
     */
    public static final int[] ROWS = new int[]{0, 1, 2, 3, 4, 5, 6, 7};


    /**
     * The number of valid values
     */
    public static final int VALUE_COUNT = COLUMNS.length * ROWS.length;

    /**
     * The converter that can convert to and from JSON objects
     */
    public final static CoordinateJSONConverter JSON_CONVERTER = CoordinateJSONConverter.getInstance();


    public static final String JSON_PROPERTY_NAME_COLUMN = "column";
    public static final String JSON_PROPERTY_NAME_ROW = "row";
    private static final Coordinate[][] coordinates = new Coordinate[COLUMNS.length][ROWS.length];
    private final char file;
    private final int rank;
    private final int row;
    private final int column;

    /**
     * Creates a coordinate with the provided pre-validated file, rank, column, and row.
     * <p>
     * This private constructor doesn't check the validity of the arguments.
     * It is the responsibility of the caller to provided arguments that are valid and correct.
     *
     * @param file   a file, should be 'a', 'b', 'c', 'd', 'e', 'f', 'g', or 'h'
     * @param rank   a rank, should be in range [1, 8]
     * @param column a column, should be in range [0, 8)
     * @param row    a row, should be in range [0, 8)
     */
    private Coordinate(char file, int rank, int column, int row) {
        this.file = file;
        this.rank = rank;
        this.row = row;
        this.column = column;
    }

    /**
     * Gets a coordinate by column and row.
     *
     * @param column the column of the coordinate where 0 is the furthest column to left when black's starting position is at the top, must be in range [0, 8)
     * @param row    the row of the coordinate where 0 is the furthest row to the top when black's starting position is at the top, must be in range [0, 8)
     * @return the coordinate with matching column and row
     */
    public static Coordinate getByColumnAndRow(int column, int row) {

        if (column < MIN_COLUMN || column > MAX_COLUMN) {
            String exceptionMessage = "expected rank in range [0, 7] but got " + column + ". ";
            throw new IllegalArgumentException(exceptionMessage);
        }
        if (row < MIN_ROW || row > MAX_ROW) {
            String exceptionMessage = "expected rank in range [0, 7] but got " + row + ". ";
            throw new IllegalArgumentException(exceptionMessage);
        }

        if (coordinates[column][row] == null) {

            char file = (char) ('a' + column);
            int rank = 8 - row;

            coordinates[column][row] = new Coordinate(file, rank, column, row);
        }

        return coordinates[column][row];


    }

    /**
     * Gets a coordinate by file and rank.
     *
     * @param file the file of the coordinate where 'a' is the furthest file to left when black's starting position is at the top, must be 'a', 'b', 'c', 'd', 'e', 'f', 'g', or 'h'
     * @param rank the rank of the coordinate where 1 is the furthest rank to bottom when black's starting position is at the top, must be in range [1, 8]
     * @return the coordinate with matching file and rank
     */
    public static Coordinate getByFileAndRank(char file, int rank) {

        file = Character.toLowerCase(file);
        if (file < MIN_FILE || file > MAX_FILE) {
            String exceptionMessage = "expected file of 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' (case insensitive) but got '" + file + "' (with int value of " + (int) file + "). ";
            throw new IllegalArgumentException(exceptionMessage);
        }

        if (rank < MIN_RANK || rank > MAX_RANK) {
            String exceptionMessage = "expected rank in range [1, 8] but got " + rank + ". ";
            throw new IllegalArgumentException(exceptionMessage);
        }


        int row = 8 - rank;
        int column = file - 'a';

        if (coordinates[column][row] == null) {
            coordinates[column][row] = new Coordinate(file, rank, column, row);
        }

        return coordinates[column][row];

    }

    public static Iterable<? extends Coordinate> values() {

        return new Iterable<Coordinate>() {

            @NonNull
            @Override
            public Iterator<Coordinate> iterator() {
                return new Iterator<Coordinate>() {
                    private int row = 0;
                    private int column = 0;

                    @Override
                    public boolean hasNext() {
                        return column < COLUMNS.length && row < ROWS.length;
                    }

                    @Override
                    public Coordinate next() {
                        Coordinate coordinate = getByColumnAndRow(column, row);

                        column = (column + 1) % COLUMNS.length;
                        if (column == 0) {
                            row++;
                        }

                        return coordinate;
                    }
                };
            }
        };
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

    /**
     * Gets the file, where a file is a letter 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' representing
     * the columns where 'a' is the furthest to white's queenside (at starting position).
     *
     * @return the file
     */
    public char getFile() {
        return file;
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_PROPERTY_NAME_COLUMN, column);
        jsonObject.put(JSON_PROPERTY_NAME_ROW, row);

        return jsonObject;
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
     * Checks to see if this coordinate would be for a black square on a standard chessboard.
     *
     * @return true if the square at that coordinate is black
     */
    public boolean isBlackSquare() { //TODO write unit test
        return !isWhiteSquare();
    }

    /**
     * Checks to see if this coordinate would be for a white square on a standard chessboard.
     *
     * @return true if the square at that coordinate is white
     */
    public boolean isWhiteSquare() { //TODO write unit test
        return (row + column) % 2 == 0;
    }

    @Override
    public String toString() {
        return "" + file + rank;
    }

    public static class CoordinateJSONConverter extends JSONConverter<Coordinate> {


        private static CoordinateJSONConverter instance;

        /**
         * Creates an instance of <code>CoordinateJSONConverter</code> to construct a singleton instance
         */
        private CoordinateJSONConverter() {
        }

        /**
         * Gets the instance.
         *
         * @return the instance.
         */
        public static CoordinateJSONConverter getInstance() {
            if (instance == null) {
                instance = new CoordinateJSONConverter();
            }

            return instance;
        }


        @Override
        public Coordinate convertFromJSONObject(JSONObject jsonObject) throws JSONException {

            int column = jsonObject.getInt(JSON_PROPERTY_NAME_COLUMN);
            int row = jsonObject.getInt(JSON_PROPERTY_NAME_ROW);
            return Coordinate.getByColumnAndRow(column, row);
        }
    }
}
