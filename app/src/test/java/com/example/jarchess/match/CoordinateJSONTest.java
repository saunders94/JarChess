package com.example.jarchess.match;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(Enclosed.class)
public class CoordinateJSONTest {

    @RunWith(Parameterized.class)
    public static class validCoordinates {
        private static final int PARAM_VALUE_OF_STRING = 0;
        private static final int PARAM_VALUE_OF_COLUMN = 1;
        private static final int PARAM_VALUE_OF_ROW = 2;
        private static final int PARAM_VALUE_OF_FILE = 3;
        private static final int PARAM_VALUE_OF_RANK = 4;

        @Parameter(value = PARAM_VALUE_OF_STRING)
        public String coordinateString;

        @Parameter(value = PARAM_VALUE_OF_COLUMN)
        public int column;

        @Parameter(value = PARAM_VALUE_OF_ROW)
        public int row;

        @Parameter(value = PARAM_VALUE_OF_FILE)
        public char file;

        @Parameter(value = PARAM_VALUE_OF_RANK)
        public int rank;

        private Coordinate coordinateUnderTest;

        @Parameters(name = "{index}: {" + PARAM_VALUE_OF_STRING + "}")
        public static Object[][] data() {
            return new Object[][]{
                    {"a1", 0, 7, 'a', 1},
                    {"b1", 1, 7, 'b', 1},
                    {"c1", 2, 7, 'c', 1},
                    {"d1", 3, 7, 'd', 1},
                    {"e1", 4, 7, 'e', 1},
                    {"f1", 5, 7, 'f', 1},
                    {"g1", 6, 7, 'g', 1},
                    {"h1", 7, 7, 'h', 1},

                    {"a2", 0, 6, 'a', 2},
                    {"b2", 1, 6, 'b', 2},
                    {"c2", 2, 6, 'c', 2},
                    {"d2", 3, 6, 'd', 2},
                    {"e2", 4, 6, 'e', 2},
                    {"f2", 5, 6, 'f', 2},
                    {"g2", 6, 6, 'g', 2},
                    {"h2", 7, 6, 'h', 2},

                    {"a3", 0, 5, 'a', 3},
                    {"b3", 1, 5, 'b', 3},
                    {"c3", 2, 5, 'c', 3},
                    {"d3", 3, 5, 'd', 3},
                    {"e3", 4, 5, 'e', 3},
                    {"f3", 5, 5, 'f', 3},
                    {"g3", 6, 5, 'g', 3},
                    {"h3", 7, 5, 'h', 3},

                    {"a4", 0, 4, 'a', 4},
                    {"b4", 1, 4, 'b', 4},
                    {"c4", 2, 4, 'c', 4},
                    {"d4", 3, 4, 'd', 4},
                    {"e4", 4, 4, 'e', 4},
                    {"f4", 5, 4, 'f', 4},
                    {"g4", 6, 4, 'g', 4},
                    {"h4", 7, 4, 'h', 4},

                    {"a5", 0, 3, 'a', 5},
                    {"b5", 1, 3, 'b', 5},
                    {"c5", 2, 3, 'c', 5},
                    {"d5", 3, 3, 'd', 5},
                    {"e5", 4, 3, 'e', 5},
                    {"f5", 5, 3, 'f', 5},
                    {"g5", 6, 3, 'g', 5},
                    {"h5", 7, 3, 'h', 5},

                    {"a6", 0, 2, 'a', 6},
                    {"b6", 1, 2, 'b', 6},
                    {"c6", 2, 2, 'c', 6},
                    {"d6", 3, 2, 'd', 6},
                    {"e6", 4, 2, 'e', 6},
                    {"f6", 5, 2, 'f', 6},
                    {"g6", 6, 2, 'g', 6},
                    {"h6", 7, 2, 'h', 6},

                    {"a7", 0, 1, 'a', 7},
                    {"b7", 1, 1, 'b', 7},
                    {"c7", 2, 1, 'c', 7},
                    {"d7", 3, 1, 'd', 7},
                    {"e7", 4, 1, 'e', 7},
                    {"f7", 5, 1, 'f', 7},
                    {"g7", 6, 1, 'g', 7},
                    {"h7", 7, 1, 'h', 7},

                    {"a8", 0, 0, 'a', 8},
                    {"b8", 1, 0, 'b', 8},
                    {"c8", 2, 0, 'c', 8},
                    {"d8", 3, 0, 'd', 8},
                    {"e8", 4, 0, 'e', 8},
                    {"f8", 5, 0, 'f', 8},
                    {"g8", 6, 0, 'g', 8},
                    {"h8", 7, 0, 'h', 8},
            };
        }

        @Before
        public void setup() {
            coordinateUnderTest = Coordinate.getByColumnAndRow(column, row);
        }

        @Test
        public void getJSONObject_isCorrect() throws Exception {
            JSONObject jsonObject = coordinateUnderTest.getJSONObject();
            Coordinate actualCoordinate = Coordinate.JSON_CONVERTER.convertFromJSONObject(jsonObject);
            int actualColumn = actualCoordinate.getColumn();
            int actualRow = actualCoordinate.getRow();
            char actualFile = actualCoordinate.getFile();
            int actualRank = actualCoordinate.getRank();
            int actualHashCode = actualCoordinate.hashCode();

            assertEquals("columns are not equal", column, actualColumn);
            assertEquals("rows are not equal", row, actualRow);
            assertEquals("files are not equal", file, actualFile);
            assertEquals("ranks are not equal", rank, actualRank);
            assertEquals("hashCodes are not equal", coordinateUnderTest.hashCode(), actualHashCode);
            assertEquals("Coordaintes are not equal", coordinateUnderTest, actualCoordinate);
            assertSame("Coordaintes are not same", coordinateUnderTest, actualCoordinate);


        }
    }
}