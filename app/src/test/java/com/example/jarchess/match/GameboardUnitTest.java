package com.example.jarchess.match;

import com.example.jarchess.match.pieces.*;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

@RunWith(Suite.class)
@SuiteClasses({
        GameboardUnitTest.Add.class,
        GameboardUnitTest.Move.class,
//        GameboardUnitTest.RemoveTest.class TODO
})
public class GameboardUnitTest {

    private static final Object[][] coordinateParameterData = new Object[][]{
                //coordinateString, file, rank, column, row
                {"a1", 'a', 1,  0, 7},
                {"a2", 'a', 2,  0, 6},
                {"a3", 'a', 3,  0, 5},
                {"a4", 'a', 4,  0, 4},
                {"a5", 'a', 5,  0, 3},
                {"a6", 'a', 6,  0, 2},
                {"a7", 'a', 7,  0, 1},
                {"a8", 'a', 8,  0, 0},

                {"b1", 'b', 1,  0, 7},
                {"b2", 'b', 2,  0, 6},
                {"b3", 'b', 3,  0, 5},
                {"b4", 'b', 4,  0, 4},
                {"b5", 'b', 5,  0, 3},
                {"b6", 'b', 6,  0, 2},
                {"b7", 'b', 7,  0, 1},
                {"b8", 'b', 8,  0, 0},

                {"c1", 'c', 1,  0, 7},
                {"c2", 'c', 2,  0, 6},
                {"c3", 'c', 3,  0, 5},
                {"c4", 'c', 4,  0, 4},
                {"c5", 'c', 5,  0, 3},
                {"c6", 'c', 6,  0, 2},
                {"c7", 'c', 7,  0, 1},
                {"c8", 'c', 8,  0, 0},

                {"d1", 'd', 1,  0, 7},
                {"d2", 'd', 2,  0, 6},
                {"d3", 'd', 3,  0, 5},
                {"d4", 'd', 4,  0, 4},
                {"d5", 'd', 5,  0, 3},
                {"d6", 'd', 6,  0, 2},
                {"d7", 'd', 7,  0, 1},
                {"d8", 'd', 8,  0, 0},

                {"e1", 'e', 1,  0, 7},
                {"e2", 'e', 2,  0, 6},
                {"e3", 'e', 3,  0, 5},
                {"e4", 'e', 4,  0, 4},
                {"e5", 'e', 5,  0, 3},
                {"e6", 'e', 6,  0, 2},
                {"e7", 'e', 7,  0, 1},
                {"e8", 'e', 8,  0, 0},

                {"f1", 'f', 1,  0, 7},
                {"f2", 'f', 2,  0, 6},
                {"f3", 'f', 3,  0, 5},
                {"f4", 'f', 4,  0, 4},
                {"f5", 'f', 5,  0, 3},
                {"f6", 'f', 6,  0, 2},
                {"f7", 'f', 7,  0, 1},
                {"f8", 'f', 8,  0, 0},

                {"g1", 'g', 1,  0, 7},
                {"g2", 'g', 2,  0, 6},
                {"g3", 'g', 3,  0, 5},
                {"g4", 'g', 4,  0, 4},
                {"g5", 'g', 5,  0, 3},
                {"g6", 'g', 6,  0, 2},
                {"g7", 'g', 7,  0, 1},
                {"g8", 'g', 8,  0, 0},

                {"h1", 'h', 1,  0, 7},
                {"h2", 'h', 2,  0, 6},
                {"h3", 'h', 3,  0, 5},
                {"h4", 'h', 4,  0, 4},
                {"h5", 'h', 5,  0, 3},
                {"h6", 'h', 6,  0, 2},
                {"h7", 'h', 7,  0, 1},
                {"h8", 'h', 8,  0, 0},
        };


    @RunWith(Suite.class)
    @SuiteClasses({
            Add.AddPieceTest.class,
            Add.AddNullTest.class,
            Add.AddToNullCoordinateTest.class
    })
    public static class Add {

        @RunWith(Parameterized.class)
        public static class AddPieceTest {

            @Rule
            public MockitoRule rule = MockitoJUnit.rule();


            private Gameboard gameboardUnderTest;

            @Mock
            private Coordinate coordinate = mock(Coordinate.class);

            @Mock
            private Piece piece;

            @Before
            public void setupBeforeTest() {
                gameboardUnderTest = new Gameboard();

                //setup mock coordinate behaviour
                when(coordinate.toString()).thenReturn(coordinateString);
                when(coordinate.getColumn()).thenReturn(column);
                when(coordinate.getRow()).thenReturn(row);

                //setup mock piece behaviour
                piece = mock(pieceClass);
            }

            @Parameter
            public String coordinateString;

            @Parameter(value = 1)
            public int column;

            @Parameter(value = 2)
            public int row;

            @Parameter(value = 3)
            public Class<Piece> pieceClass;

            @Parameter(value = 4)
            public String pieceName;

            @Parameters(name = "{index}: add {4} to {0}")
            public static Object[][] data() {
                return new Object[][]{
                        //coordinate, column, row, piece class, piece name


                        {"a1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"a2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"a3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"a4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"a5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"a6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"a7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"a8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"b1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"b2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"b3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"b4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"b5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"b6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"b7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"b8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"c1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"c2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"c3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"c4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"c5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"c6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"c7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"c8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"d1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"d2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"d3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"d4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"d5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"d6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"d7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"d8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"e1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"e2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"e3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"e4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"e5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"e6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"e7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"e8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"f1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"f2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"f3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"f4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"f5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"f6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"f7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"f8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"g1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"g2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"g3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"g4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"g5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"g6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"g7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"g8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},

                        {"h1", 0, 7, Pawn.class, Pawn.class.getSimpleName()},
                        {"h2", 0, 6, Pawn.class, Pawn.class.getSimpleName()},
                        {"h3", 0, 5, Pawn.class, Pawn.class.getSimpleName()},
                        {"h4", 0, 4, Pawn.class, Pawn.class.getSimpleName()},
                        {"h5", 0, 3, Pawn.class, Pawn.class.getSimpleName()},
                        {"h6", 0, 2, Pawn.class, Pawn.class.getSimpleName()},
                        {"h7", 0, 1, Pawn.class, Pawn.class.getSimpleName()},
                        {"h8", 0, 0, Pawn.class, Pawn.class.getSimpleName()},


                        {"a1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"a2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"a3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"a4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"a5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"a6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"a7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"a8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"b1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"b2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"b3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"b4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"b5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"b6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"b7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"b8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"c1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"c2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"c3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"c4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"c5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"c6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"c7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"c8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"d1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"d2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"d3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"d4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"d5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"d6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"d7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"d8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"e1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"e2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"e3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"e4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"e5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"e6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"e7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"e8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"f1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"f2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"f3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"f4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"f5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"f6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"f7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"f8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"g1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"g2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"g3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"g4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"g5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"g6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"g7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"g8", 0, 0, Rook.class, Rook.class.getSimpleName()},

                        {"h1", 0, 7, Rook.class, Rook.class.getSimpleName()},
                        {"h2", 0, 6, Rook.class, Rook.class.getSimpleName()},
                        {"h3", 0, 5, Rook.class, Rook.class.getSimpleName()},
                        {"h4", 0, 4, Rook.class, Rook.class.getSimpleName()},
                        {"h5", 0, 3, Rook.class, Rook.class.getSimpleName()},
                        {"h6", 0, 2, Rook.class, Rook.class.getSimpleName()},
                        {"h7", 0, 1, Rook.class, Rook.class.getSimpleName()},
                        {"h8", 0, 0, Rook.class, Rook.class.getSimpleName()},


                        {"a1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"a2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"a3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"a4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"a5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"a6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"a7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"a8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"b1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"b2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"b3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"b4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"b5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"b6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"b7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"b8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"c1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"c2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"c3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"c4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"c5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"c6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"c7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"c8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"d1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"d2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"d3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"d4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"d5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"d6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"d7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"d8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"e1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"e2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"e3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"e4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"e5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"e6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"e7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"e8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"f1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"f2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"f3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"f4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"f5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"f6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"f7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"f8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"g1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"g2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"g3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"g4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"g5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"g6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"g7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"g8", 0, 0, Knight.class, Knight.class.getSimpleName()},

                        {"h1", 0, 7, Knight.class, Knight.class.getSimpleName()},
                        {"h2", 0, 6, Knight.class, Knight.class.getSimpleName()},
                        {"h3", 0, 5, Knight.class, Knight.class.getSimpleName()},
                        {"h4", 0, 4, Knight.class, Knight.class.getSimpleName()},
                        {"h5", 0, 3, Knight.class, Knight.class.getSimpleName()},
                        {"h6", 0, 2, Knight.class, Knight.class.getSimpleName()},
                        {"h7", 0, 1, Knight.class, Knight.class.getSimpleName()},
                        {"h8", 0, 0, Knight.class, Knight.class.getSimpleName()},


                        {"a1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"a2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"a3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"a4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"a5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"a6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"a7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"a8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"b1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"b2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"b3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"b4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"b5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"b6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"b7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"b8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"c1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"c2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"c3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"c4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"c5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"c6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"c7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"c8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"d1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"d2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"d3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"d4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"d5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"d6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"d7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"d8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"e1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"e2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"e3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"e4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"e5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"e6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"e7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"e8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"f1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"f2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"f3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"f4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"f5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"f6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"f7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"f8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"g1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"g2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"g3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"g4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"g5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"g6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"g7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"g8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},

                        {"h1", 0, 7, Bishop.class, Bishop.class.getSimpleName()},
                        {"h2", 0, 6, Bishop.class, Bishop.class.getSimpleName()},
                        {"h3", 0, 5, Bishop.class, Bishop.class.getSimpleName()},
                        {"h4", 0, 4, Bishop.class, Bishop.class.getSimpleName()},
                        {"h5", 0, 3, Bishop.class, Bishop.class.getSimpleName()},
                        {"h6", 0, 2, Bishop.class, Bishop.class.getSimpleName()},
                        {"h7", 0, 1, Bishop.class, Bishop.class.getSimpleName()},
                        {"h8", 0, 0, Bishop.class, Bishop.class.getSimpleName()},


                        {"a1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"a2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"a3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"a4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"a5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"a6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"a7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"a8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"b1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"b2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"b3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"b4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"b5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"b6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"b7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"b8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"c1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"c2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"c3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"c4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"c5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"c6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"c7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"c8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"d1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"d2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"d3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"d4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"d5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"d6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"d7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"d8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"e1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"e2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"e3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"e4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"e5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"e6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"e7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"e8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"f1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"f2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"f3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"f4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"f5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"f6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"f7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"f8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"g1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"g2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"g3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"g4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"g5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"g6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"g7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"g8", 0, 0, Queen.class, Queen.class.getSimpleName()},

                        {"h1", 0, 7, Queen.class, Queen.class.getSimpleName()},
                        {"h2", 0, 6, Queen.class, Queen.class.getSimpleName()},
                        {"h3", 0, 5, Queen.class, Queen.class.getSimpleName()},
                        {"h4", 0, 4, Queen.class, Queen.class.getSimpleName()},
                        {"h5", 0, 3, Queen.class, Queen.class.getSimpleName()},
                        {"h6", 0, 2, Queen.class, Queen.class.getSimpleName()},
                        {"h7", 0, 1, Queen.class, Queen.class.getSimpleName()},
                        {"h8", 0, 0, Queen.class, Queen.class.getSimpleName()},


                        {"a1", 0, 7, King.class, King.class.getSimpleName()},
                        {"a2", 0, 6, King.class, King.class.getSimpleName()},
                        {"a3", 0, 5, King.class, King.class.getSimpleName()},
                        {"a4", 0, 4, King.class, King.class.getSimpleName()},
                        {"a5", 0, 3, King.class, King.class.getSimpleName()},
                        {"a6", 0, 2, King.class, King.class.getSimpleName()},
                        {"a7", 0, 1, King.class, King.class.getSimpleName()},
                        {"a8", 0, 0, King.class, King.class.getSimpleName()},

                        {"b1", 0, 7, King.class, King.class.getSimpleName()},
                        {"b2", 0, 6, King.class, King.class.getSimpleName()},
                        {"b3", 0, 5, King.class, King.class.getSimpleName()},
                        {"b4", 0, 4, King.class, King.class.getSimpleName()},
                        {"b5", 0, 3, King.class, King.class.getSimpleName()},
                        {"b6", 0, 2, King.class, King.class.getSimpleName()},
                        {"b7", 0, 1, King.class, King.class.getSimpleName()},
                        {"b8", 0, 0, King.class, King.class.getSimpleName()},

                        {"c1", 0, 7, King.class, King.class.getSimpleName()},
                        {"c2", 0, 6, King.class, King.class.getSimpleName()},
                        {"c3", 0, 5, King.class, King.class.getSimpleName()},
                        {"c4", 0, 4, King.class, King.class.getSimpleName()},
                        {"c5", 0, 3, King.class, King.class.getSimpleName()},
                        {"c6", 0, 2, King.class, King.class.getSimpleName()},
                        {"c7", 0, 1, King.class, King.class.getSimpleName()},
                        {"c8", 0, 0, King.class, King.class.getSimpleName()},

                        {"d1", 0, 7, King.class, King.class.getSimpleName()},
                        {"d2", 0, 6, King.class, King.class.getSimpleName()},
                        {"d3", 0, 5, King.class, King.class.getSimpleName()},
                        {"d4", 0, 4, King.class, King.class.getSimpleName()},
                        {"d5", 0, 3, King.class, King.class.getSimpleName()},
                        {"d6", 0, 2, King.class, King.class.getSimpleName()},
                        {"d7", 0, 1, King.class, King.class.getSimpleName()},
                        {"d8", 0, 0, King.class, King.class.getSimpleName()},

                        {"e1", 0, 7, King.class, King.class.getSimpleName()},
                        {"e2", 0, 6, King.class, King.class.getSimpleName()},
                        {"e3", 0, 5, King.class, King.class.getSimpleName()},
                        {"e4", 0, 4, King.class, King.class.getSimpleName()},
                        {"e5", 0, 3, King.class, King.class.getSimpleName()},
                        {"e6", 0, 2, King.class, King.class.getSimpleName()},
                        {"e7", 0, 1, King.class, King.class.getSimpleName()},
                        {"e8", 0, 0, King.class, King.class.getSimpleName()},

                        {"f1", 0, 7, King.class, King.class.getSimpleName()},
                        {"f2", 0, 6, King.class, King.class.getSimpleName()},
                        {"f3", 0, 5, King.class, King.class.getSimpleName()},
                        {"f4", 0, 4, King.class, King.class.getSimpleName()},
                        {"f5", 0, 3, King.class, King.class.getSimpleName()},
                        {"f6", 0, 2, King.class, King.class.getSimpleName()},
                        {"f7", 0, 1, King.class, King.class.getSimpleName()},
                        {"f8", 0, 0, King.class, King.class.getSimpleName()},

                        {"g1", 0, 7, King.class, King.class.getSimpleName()},
                        {"g2", 0, 6, King.class, King.class.getSimpleName()},
                        {"g3", 0, 5, King.class, King.class.getSimpleName()},
                        {"g4", 0, 4, King.class, King.class.getSimpleName()},
                        {"g5", 0, 3, King.class, King.class.getSimpleName()},
                        {"g6", 0, 2, King.class, King.class.getSimpleName()},
                        {"g7", 0, 1, King.class, King.class.getSimpleName()},
                        {"g8", 0, 0, King.class, King.class.getSimpleName()},

                        {"h1", 0, 7, King.class, King.class.getSimpleName()},
                        {"h2", 0, 6, King.class, King.class.getSimpleName()},
                        {"h3", 0, 5, King.class, King.class.getSimpleName()},
                        {"h4", 0, 4, King.class, King.class.getSimpleName()},
                        {"h5", 0, 3, King.class, King.class.getSimpleName()},
                        {"h6", 0, 2, King.class, King.class.getSimpleName()},
                        {"h7", 0, 1, King.class, King.class.getSimpleName()},
                        {"h8", 0, 0, King.class, King.class.getSimpleName()},
                };
            }

            @Test
            public void validAddToEmpty_isCorrect() {

                // GIVEN
                // the coordinate we are adding to is empty
                Assert.assertNull("Spot should have been empty but was not.", gameboardUnderTest.getPieceAt(coordinate));

                // WHEN
                // we add a piece
                gameboardUnderTest.add(piece, coordinate);

                // THEN
                // the same piece we added should be found at the coordinate
                Assert.assertSame(piece, gameboardUnderTest.getPieceAt(coordinate));
            }

            @Test(expected = Gameboard.AlreadyOccupiedException.class)
            public void addToOcupiedShouldThrowSpaceAlreadyOccupiedException() {
                gameboardUnderTest.add(mock(Piece.class), coordinate);
                gameboardUnderTest.add(piece, coordinate);
            }
        }

        @RunWith(Parameterized.class)
        public static class AddNullTest {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();


            private Gameboard gameboardUnderTest;

            @Mock
            private Coordinate coordinate = mock(Coordinate.class);


            @Before
            public void setupBeforeTest() {
                gameboardUnderTest = new Gameboard();

                //setup mock coordinate behaviour
                when(coordinate.toString()).thenReturn(coordinateString);
                when(coordinate.getColumn()).thenReturn(column);
                when(coordinate.getRow()).thenReturn(row);
            }

            @Parameter
            public String coordinateString;

            @Parameter(value = 1)
            public char file;

            @Parameter(value = 2)
            public int rank;

            @Parameter(value = 3)
            public int column;

            @Parameter(value = 4)
            public int row;

            @Parameters(name = "{index}: add null piece to {0}")
            public static Object[][] data() {
                return coordinateParameterData;
            }

            @SuppressWarnings("ConstantConditions")
            @Test(expected = IllegalArgumentException.class)
            public void addNullToCoordinateShouldThrowsIllegalArgumentException() {
                gameboardUnderTest.add(null, coordinate);
            }

        }


        @RunWith(Parameterized.class)
        public static class AddToNullCoordinateTest {

            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            private Gameboard gameboardUnderTest;

            @Mock
            private Piece piece;

            @Before
            public void setupBeforeTest() {
                gameboardUnderTest = new Gameboard();

                //setup mock piece behaviour
                piece = mock(pieceClass);
            }

            @Parameter
            public Class<Piece> pieceClass;
            @Parameter(value = 1)
            public String pieceName;

            @Parameters(name = "{index}: add {1} to null coordinate")
            public static Object[][] data() {
                return new Object[][]{
                        {Pawn.class, Pawn.class.getSimpleName()},
                        {Rook.class, Rook.class.getSimpleName()},
                        {Knight.class, Knight.class.getSimpleName()},
                        {Bishop.class, Bishop.class.getSimpleName()},
                        {Queen.class, Queen.class.getSimpleName()},
                        {King.class, King.class.getSimpleName()},
                };
            }

            @Test(expected = IllegalArgumentException.class)
            public void addPieceToNullCoordinateShouldThrowsIllegalArgumentException() {
                gameboardUnderTest.add(piece, null);
            }


        }
    }

    @RunWith(Suite.class)
    @SuiteClasses(Move.OccupiedToOtherOccupied.class)//TODO add rest
    public static class Move {

        @RunWith(Parameterized.class)
        public static class NullToEmpty {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class NullToOccupied {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class EmptyToNull {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class OccupiedToNull {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class EmptyToEmpty {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class EmptyToOccupied {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class OccupiedToEmpty {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }


        @RunWith(Parameterized.class)
        public static class OccupiedToSameCoordinate {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            //TODO
        }

        @RunWith(Parameterized.class)
        public static class OccupiedToOtherOccupied {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            private Gameboard gameboardUnderTest;

            @Mock
            Coordinate mockOriginCoordinate = mock(Coordinate.class);
            @Mock
            Piece mockOriginPiece;
            @Mock
            Coordinate mockDestinationCoordinate = mock(Coordinate.class);
            @Mock
            Piece mockDestinationPiece;

            @Parameter
            public Piece.Color originPieceColor;
            @Parameter(value = 1)
            public Class<? extends Piece> originPieceClass;
            @Parameter(value = 2)
            public String originPieceClassSimpleName;
            @Parameter(value = 3)
            public char originFile;
            @Parameter(value = 4)
            public int originColumn;
            @Parameter(value = 5)
            public int originRank;
            @Parameter(value = 6)
            public int originRow;
            @Parameter(value = 7)
            public Piece.Color destinationPieceColor;
            @Parameter(value = 8)
            public Class<? extends Piece> destinationPieceClass;
            @Parameter(value = 9)
            public String destinationPieceClassSimpleName;
            @Parameter(value = 10)
            public char destinationFile;
            @Parameter(value = 11)
            public int destinationColumn;
            @Parameter(value = 12)
            public int destinationRank;
            @Parameter(value = 13)
            public int destinationRow;

            @Parameters(name = "{index}: move {0} {2} from {6} to {13} when destination is occupied by a {7} {9}. ")
            public static Object[][] data() {
                return new Object[][]{

                        // generated using pict for pairwise combinations
                        // see docs/6_test_data/test_design/all_pairs/pict/for_unit_tests/match/Gameboard/move/OccupiedToOtherOccupied* files
                        //
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'g', 6, 6, 2, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'e', 4, 6, 2,},
                        {Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'g', 6, 5, 3, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'b', 1, 2, 6,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'd', 3, 6, 2, Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'h', 7, 4, 4,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'h', 7, 6, 2, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'd', 3, 1, 7,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'c', 2, 4, 4, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'c', 2, 7, 1,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'a', 0, 3, 5, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'g', 6, 6, 2,},
                        {Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'f', 5, 1, 7, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'c', 2, 8, 0,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'a', 0, 6, 2, Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'f', 5, 3, 5,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'c', 2, 8, 0, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'a', 0, 5, 3,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'b', 1, 2, 6, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'f', 5, 1, 7,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'f', 5, 7, 1, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'e', 4, 3, 5,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'a', 0, 7, 1, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'b', 1, 1, 7,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'e', 4, 5, 3, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'd', 3, 4, 4,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'e', 4, 8, 0, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'g', 6, 7, 1,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'e', 4, 2, 6, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'b', 1, 8, 0,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'h', 7, 1, 7, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'a', 0, 2, 6,},
                        {Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'c', 2, 2, 6, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'h', 7, 2, 6,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'd', 3, 4, 4, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'f', 5, 5, 3,},
                        {Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'c', 2, 7, 1, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'd', 3, 6, 2,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'b', 1, 5, 3, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'g', 6, 3, 5,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'h', 7, 3, 5, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'e', 4, 7, 1,},
                        {Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'g', 6, 1, 7, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'g', 6, 5, 3,},
                        {Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'f', 5, 4, 4, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'g', 6, 2, 6,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'd', 3, 1, 7, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'h', 7, 6, 2,},
                        {Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'a', 0, 4, 4, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'a', 0, 8, 0,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'h', 7, 8, 0, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'h', 7, 3, 5,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'd', 3, 7, 1, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'g', 6, 8, 0,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'd', 3, 5, 3, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 7, 1,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'd', 3, 8, 0, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'e', 4, 1, 7,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'g', 6, 8, 0, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'c', 2, 4, 4,},
                        {Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'e', 4, 3, 5, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 6, 2,},
                        {Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'e', 4, 7, 1, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'c', 2, 5, 3,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 2, 6, Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'd', 3, 5, 3,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'f', 5, 5, 3, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'h', 7, 1, 7,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'c', 2, 3, 5, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'f', 5, 8, 0,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'g', 6, 4, 4, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'b', 1, 1, 7,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'a', 0, 8, 0, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'c', 2, 2, 6,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'c', 2, 1, 7, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'b', 1, 4, 4,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'b', 1, 6, 2, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'b', 1, 7, 1,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'e', 4, 6, 2, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'e', 4, 2, 6,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'h', 7, 2, 6, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'c', 2, 6, 2,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'b', 1, 7, 1, Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'a', 0, 4, 4,},
                        {Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'f', 5, 3, 5, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'b', 1, 5, 3,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'g', 6, 2, 6, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'a', 0, 3, 5,},
                        {Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'h', 7, 5, 3, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'f', 5, 4, 4,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'g', 6, 6, 2, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'd', 3, 8, 0,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'g', 6, 7, 1, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'f', 5, 7, 1,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'b', 1, 3, 5, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'd', 3, 3, 5,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'a', 0, 2, 6, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'e', 4, 4, 4,},
                        {Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'f', 5, 2, 6, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'g', 6, 7, 1,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'd', 3, 2, 6, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'b', 1, 3, 5,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'b', 1, 5, 3, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'e', 4, 5, 3,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'b', 1, 4, 4, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'd', 3, 2, 6,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'e', 4, 3, 5, Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'h', 7, 4, 4,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'f', 5, 8, 0, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'f', 5, 6, 2,},
                        {Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'h', 7, 4, 4, Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'e', 4, 8, 0,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'e', 4, 1, 7, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'f', 5, 2, 6,},
                        {Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'd', 3, 4, 4, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'c', 2, 3, 5,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'b', 1, 1, 7, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'd', 3, 3, 5,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 4, 4, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'h', 7, 4, 4,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'b', 1, 5, 3, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'c', 2, 6, 2,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'e', 4, 4, 4, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'a', 0, 1, 7,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'f', 5, 6, 2, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'a', 0, 5, 3,},
                        {Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'g', 6, 3, 5, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'c', 2, 1, 7,},
                        {Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'h', 7, 8, 0, Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'b', 1, 6, 2,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'h', 7, 7, 1, Piece.Color.WHITE, Rook.class, Rook.class.getSimpleName(), 'h', 7, 5, 3,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'f', 5, 1, 7, Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'd', 3, 4, 4,},
                        {Piece.Color.WHITE, King.class, King.class.getSimpleName(), 'c', 2, 4, 4, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'g', 6, 6, 2,},
                        {Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 5, 3, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'd', 3, 7, 1,},
                        {Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'a', 0, 1, 7, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'h', 7, 7, 1,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'e', 4, 8, 0, Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'd', 3, 3, 5,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'c', 2, 1, 7, Piece.Color.WHITE, Bishop.class, Bishop.class.getSimpleName(), 'e', 4, 1, 7,},
                        {Piece.Color.BLACK, Pawn.class, Pawn.class.getSimpleName(), 'c', 2, 6, 2, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'g', 6, 3, 5,},
                        {Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'b', 1, 8, 0, Piece.Color.BLACK, Knight.class, Knight.class.getSimpleName(), 'h', 7, 8, 0,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'd', 3, 3, 5, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'd', 3, 2, 6,},
                        {Piece.Color.WHITE, Knight.class, Knight.class.getSimpleName(), 'g', 6, 5, 3, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'h', 7, 8, 0,},
                        {Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'h', 7, 5, 3, Piece.Color.BLACK, Bishop.class, Bishop.class.getSimpleName(), 'g', 6, 4, 4,},
                        {Piece.Color.BLACK, Queen.class, Queen.class.getSimpleName(), 'c', 2, 5, 3, Piece.Color.BLACK, King.class, King.class.getSimpleName(), 'g', 6, 1, 7,},
                        {Piece.Color.BLACK, Rook.class, Rook.class.getSimpleName(), 'a', 0, 6, 2, Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'c', 2, 4, 4,},
                        {Piece.Color.WHITE, Queen.class, Queen.class.getSimpleName(), 'g', 6, 7, 1, Piece.Color.WHITE, Pawn.class, Pawn.class.getSimpleName(), 'a', 0, 2, 6,}
                };
            }

            @Before
            public void setupTest() {
                gameboardUnderTest = new Gameboard();

                mockOriginPiece = mock(originPieceClass);

                mockDestinationPiece = mock(destinationPieceClass);

                when(mockOriginPiece.getColor()).thenReturn(originPieceColor);
                when(mockOriginCoordinate.getFile()).thenReturn(originFile);
                when(mockOriginCoordinate.getRank()).thenReturn(originRank);
                when(mockOriginCoordinate.getColumn()).thenReturn(originColumn);
                when(mockOriginCoordinate.getRow()).thenReturn(originRow);

                when(mockDestinationPiece.getColor()).thenReturn(destinationPieceColor);
                when(mockDestinationCoordinate.getFile()).thenReturn(destinationFile);
                when(mockDestinationCoordinate.getRank()).thenReturn(destinationRank);
                when(mockDestinationCoordinate.getColumn()).thenReturn(destinationColumn);
                when(mockDestinationCoordinate.getRow()).thenReturn(destinationRow);

                gameboardUnderTest.add(mockOriginPiece, mockOriginCoordinate);
                gameboardUnderTest.add(mockDestinationPiece, mockDestinationCoordinate);
            }

            @Test(expected = Gameboard.AlreadyOccupiedException.class)
            public void moveFromOccupiedToDifferentOccupiedShouldThrowAlreadyOccupiedException() {
                gameboardUnderTest.move(mockOriginCoordinate, mockDestinationCoordinate);
            }
        }
    }

    @RunWith(Suite.class)
    public static class isEmptyAt {

        @RunWith(Suite.class)
        @SuiteClasses({
                //TODO add classes to test
        })
        public static class ShouldThrowException{
            //TODO Add Test for bad rows on coordinates


            //TODO Add Test for bad columns on coordinates


            //TODO Add Test for null coordinates
        }

        @RunWith(Parameterized.class)
        public static class ShouldReturnTrue {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            private Gameboard gameboardUnderTest;

            @Mock
            Coordinate mockCoordinate = mock(Coordinate.class);

            @Parameter
            public String coordinateString;

            @Parameter(value = 1)
            public char file;

            @Parameter(value = 2)
            public int rank;

            @Parameter(value = 3)
            public int column;

            @Parameter(value = 4)
            public int row;

            @Parameters(name = "{index}: {0} isEmpty should return true. ")
            public static Object[][] data() {
                return coordinateParameterData;
            }

            @Before
            public void setup(){
                gameboardUnderTest = new Gameboard();

                when(mockCoordinate.getFile()).thenReturn(file);
                when(mockCoordinate.getRank()).thenReturn(rank);
                when(mockCoordinate.getColumn()).thenReturn(column);
                when(mockCoordinate.getRow()).thenReturn(row);
                when(mockCoordinate.toString()).thenReturn(coordinateString);

            }

            @Test
            public void shouldReturnTrue(){
                Assert.assertTrue(gameboardUnderTest.isEmptyAt(mockCoordinate));
            }
        }

        @RunWith(Parameterized.class)
        public static class ShouldReturnFalse {
            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            private Gameboard gameboardUnderTest;

            @Mock
            Coordinate mockCoordinate = mock(Coordinate.class);

            @Mock
            Piece mockPiece;

            @Parameter
            public String colorString;

            @Parameter(value = 1)
            public String pieceName;

            @Parameter(value = 2)
            public String coordinateString;

            @Parameter(value = 3)
            public Piece.Color pieceColor;

            @Parameter(value = 4)
            public Class<? extends Piece> pieceClass;

            @Parameter(value = 5)
            public char file;

            @Parameter(value = 6)
            public int rank;

            @Parameter(value = 7)
            public int column;

            @Parameter(value = 8)
            public int row;

            @Parameters(name = "{index}:{0} {1} at {2}")
            public static Object[][] data() {
                return new Object[][]{

                        //generated pairwise combinations using pict
                        // see docs/6_test_data/test_design/all_pairs/pict/for_unit_tests/match/Gameboard/isEmptyAt/isEmptyAt_shouldReturnFalse.model
                        {"BLACK", "Knight", "e1", Piece.Color.BLACK, Knight.class, 'e', 1, 4, 7},
                        {"WHITE", "King", "b7", Piece.Color.WHITE, King.class, 'b', 7, 1, 1},
                        {"WHITE", "Bishop", "c3", Piece.Color.WHITE, Bishop.class, 'c', 3, 2, 5},
                        {"WHITE", "Pawn", "g1", Piece.Color.WHITE, Pawn.class, 'g', 1, 6, 7},
                        {"BLACK", "Queen", "c5", Piece.Color.BLACK, Queen.class, 'c', 5, 2, 3},
                        {"BLACK", "Rook", "g8", Piece.Color.BLACK, Rook.class, 'g', 8, 6, 0},
                        {"WHITE", "Pawn", "e2", Piece.Color.WHITE, Pawn.class, 'e', 2, 4, 6},
                        {"BLACK", "King", "f3", Piece.Color.BLACK, King.class, 'f', 3, 5, 5},
                        {"BLACK", "Queen", "d2", Piece.Color.BLACK, Queen.class, 'd', 2, 3, 6},
                        {"WHITE", "Bishop", "f2", Piece.Color.WHITE, Bishop.class, 'f', 2, 5, 6},
                        {"BLACK", "Pawn", "h6", Piece.Color.BLACK, Pawn.class, 'h', 6, 7, 2},
                        {"WHITE", "Queen", "f8", Piece.Color.WHITE, Queen.class, 'f', 8, 5, 0},
                        {"WHITE", "Rook", "b5", Piece.Color.WHITE, Rook.class, 'b', 5, 1, 3},
                        {"BLACK", "Knight", "f7", Piece.Color.BLACK, Knight.class, 'f', 7, 5, 1},
                        {"WHITE", "Knight", "h3", Piece.Color.WHITE, Knight.class, 'h', 3, 7, 5},
                        {"BLACK", "King", "c8", Piece.Color.BLACK, King.class, 'c', 8, 2, 0},
                        {"BLACK", "Knight", "b2", Piece.Color.BLACK, Knight.class, 'b', 2, 1, 6},
                        {"WHITE", "Bishop", "d1", Piece.Color.WHITE, Bishop.class, 'd', 1, 3, 7},
                        {"BLACK", "Bishop", "a7", Piece.Color.BLACK, Bishop.class, 'a', 7, 0, 1},
                        {"WHITE", "Pawn", "f5", Piece.Color.WHITE, Pawn.class, 'f', 5, 5, 3},
                        {"BLACK", "Bishop", "h8", Piece.Color.BLACK, Bishop.class, 'h', 8, 7, 0},
                        {"BLACK", "Pawn", "c7", Piece.Color.BLACK, Pawn.class, 'c', 7, 2, 1},
                        {"WHITE", "Rook", "h7", Piece.Color.WHITE, Rook.class, 'h', 7, 7, 1},
                        {"BLACK", "Pawn", "b3", Piece.Color.BLACK, Pawn.class, 'b', 3, 1, 5},
                        {"BLACK", "Queen", "g7", Piece.Color.BLACK, Queen.class, 'g', 7, 6, 1},
                        {"WHITE", "Rook", "f1", Piece.Color.WHITE, Rook.class, 'f', 1, 5, 7},
                        {"BLACK", "King", "e5", Piece.Color.BLACK, King.class, 'e', 5, 4, 3},
                        {"WHITE", "Queen", "b6", Piece.Color.WHITE, Queen.class, 'b', 6, 1, 2},
                        {"WHITE", "Bishop", "h5", Piece.Color.WHITE, Bishop.class, 'h', 5, 7, 3},
                        {"WHITE", "King", "g6", Piece.Color.WHITE, King.class, 'g', 6, 6, 2},
                        {"BLACK", "Knight", "g5", Piece.Color.BLACK, Knight.class, 'g', 5, 6, 3},
                        {"WHITE", "Rook", "e4", Piece.Color.WHITE, Rook.class, 'e', 4, 4, 4},
                        {"WHITE", "Rook", "a6", Piece.Color.WHITE, Rook.class, 'a', 6, 0, 2},
                        {"BLACK", "King", "d5", Piece.Color.BLACK, King.class, 'd', 5, 3, 3},
                        {"BLACK", "Knight", "c4", Piece.Color.BLACK, Knight.class, 'c', 4, 2, 4},
                        {"WHITE", "Knight", "f6", Piece.Color.WHITE, Knight.class, 'f', 6, 5, 2},
                        {"BLACK", "Queen", "a3", Piece.Color.BLACK, Queen.class, 'a', 3, 0, 5},
                        {"BLACK", "King", "h2", Piece.Color.BLACK, King.class, 'h', 2, 7, 6},
                        {"WHITE", "Pawn", "d4", Piece.Color.WHITE, Pawn.class, 'd', 4, 3, 4},
                        {"BLACK", "Rook", "g3", Piece.Color.BLACK, Rook.class, 'g', 3, 6, 5},
                        {"BLACK", "Bishop", "e6", Piece.Color.BLACK, Bishop.class, 'e', 6, 4, 2},
                        {"BLACK", "Rook", "c1", Piece.Color.BLACK, Rook.class, 'c', 1, 2, 7},
                        {"WHITE", "Queen", "h1", Piece.Color.WHITE, Queen.class, 'h', 1, 7, 7},
                        {"WHITE", "Knight", "d8", Piece.Color.WHITE, Knight.class, 'd', 8, 3, 0},
                        {"WHITE", "Queen", "e7", Piece.Color.WHITE, Queen.class, 'e', 7, 4, 1},
                        {"BLACK", "Pawn", "a8", Piece.Color.BLACK, Pawn.class, 'a', 8, 0, 0},
                        {"WHITE", "Rook", "c2", Piece.Color.WHITE, Rook.class, 'c', 2, 2, 6},
                        {"WHITE", "Knight", "c6", Piece.Color.WHITE, Knight.class, 'c', 6, 2, 2},
                        {"WHITE", "Knight", "e3", Piece.Color.WHITE, Knight.class, 'e', 3, 4, 5},
                        {"BLACK", "Bishop", "g4", Piece.Color.BLACK, Bishop.class, 'g', 4, 6, 4},
                        {"WHITE", "Knight", "a2", Piece.Color.WHITE, Knight.class, 'a', 2, 0, 6},
                        {"WHITE", "Queen", "e8", Piece.Color.WHITE, Queen.class, 'e', 8, 4, 0},
                        {"WHITE", "King", "a5", Piece.Color.WHITE, King.class, 'a', 5, 0, 3},
                        {"WHITE", "Rook", "d6", Piece.Color.WHITE, Rook.class, 'd', 6, 3, 2},
                        {"BLACK", "King", "a1", Piece.Color.BLACK, King.class, 'a', 1, 0, 7},
                        {"BLACK", "King", "h4", Piece.Color.BLACK, King.class, 'h', 4, 7, 4},
                        {"WHITE", "Queen", "f4", Piece.Color.WHITE, Queen.class, 'f', 4, 5, 4},
                        {"WHITE", "Bishop", "d3", Piece.Color.WHITE, Bishop.class, 'd', 3, 3, 5},
                        {"BLACK", "Knight", "a4", Piece.Color.BLACK, Knight.class, 'a', 4, 0, 4},
                        {"BLACK", "Pawn", "d7", Piece.Color.BLACK, Pawn.class, 'd', 7, 3, 1},
                        {"BLACK", "Bishop", "b8", Piece.Color.BLACK, Bishop.class, 'b', 8, 1, 0},
                        {"WHITE", "Pawn", "b1", Piece.Color.WHITE, Pawn.class, 'b', 1, 1, 7},
                        {"BLACK", "Knight", "g2", Piece.Color.BLACK, Knight.class, 'g', 2, 6, 6},
                        {"WHITE", "Pawn", "b4", Piece.Color.WHITE, Pawn.class, 'b', 4, 1, 4},

                };
            }

            @Before
            public void setup(){
                gameboardUnderTest = new Gameboard();

                mockPiece = mock(pieceClass);
                when(mockPiece.getColor()).thenReturn(pieceColor);

                when(mockCoordinate.getFile()).thenReturn(file);
                when(mockCoordinate.getRank()).thenReturn(rank);
                when(mockCoordinate.getColumn()).thenReturn(column);
                when(mockCoordinate.getRow()).thenReturn(row);
                when(mockCoordinate.toString()).thenReturn(coordinateString);

                gameboardUnderTest.add(mockPiece,mockCoordinate);
            }

            @Test
            public void shouldReturnFalse(){
                Assert.assertFalse(gameboardUnderTest.isEmptyAt(mockCoordinate));
            }
        }
    }

//TODO uncomment and make this work

//    @RunWith(Suite.class)
//    @Suite.SuiteClasses()
//    public static class RemoveTest {
//
//        @RunWith(Parameterized.class)
//        public static class RemoveFromEmpty{
//            @Rule
//            public MockitoRule rule = MockitoJUnit.rule();
//
//            
//        }
//    }
}