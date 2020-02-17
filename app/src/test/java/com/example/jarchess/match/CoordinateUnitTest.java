package com.example.jarchess.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Suite;

import static org.junit.Assert.assertEquals;


/**
 * @author Joshua Zierman
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CoordinateUnitTest.ValidTests.class,
        CoordinateUnitTest.ValidFileInvalidRankTest.class,
        CoordinateUnitTest.InvalidFileValidRankTest.class,
        CoordinateUnitTest.InvalidFileInvalidRankTest.class
})
public class CoordinateUnitTest {
    private static Coordinate coordinate;

    @RunWith(Parameterized.class)
    public static class ValidTests {

        @Parameter() //default value 0
        public char validFile;

        @Parameter(value = 1)
        public int validRank;

        @Parameter(value = 2)
        public int expectedColumn;

        @Parameter(value = 3)
        public int expectedRow;

        @Parameter(value = 4)
        public String expectedString;

        private char expectedFile;
        private int expectedRank;

        @Before
        public void init(){
            coordinate = new Coordinate(validFile, validRank);
            expectedFile = Character.toLowerCase(validFile);
            expectedRank = validRank;
        }


        @Parameters(name = "{index}: {0}{1} ")
        public static Object[][] validData() {
            return new Object[][]{

                    // validFile, validRank, expectedColumn, expectedRow, expectedString

                    //lowercase

                    {'a', 1, 0, 7, "a1"},
                    {'a', 2, 0, 6, "a2"},
                    {'a', 3, 0, 5, "a3"},
                    {'a', 4, 0, 4, "a4"},
                    {'a', 5, 0, 3, "a5"},
                    {'a', 6, 0, 2, "a6"},
                    {'a', 7, 0, 1, "a7"},
                    {'a', 8, 0, 0, "a8"},

                    {'b', 1, 1, 7, "b1"},
                    {'b', 2, 1, 6, "b2"},
                    {'b', 3, 1, 5, "b3"},
                    {'b', 4, 1, 4, "b4"},
                    {'b', 5, 1, 3, "b5"},
                    {'b', 6, 1, 2, "b6"},
                    {'b', 7, 1, 1, "b7"},
                    {'b', 8, 1, 0, "b8"},

                    {'c', 1, 2, 7, "c1"},
                    {'c', 2, 2, 6, "c2"},
                    {'c', 3, 2, 5, "c3"},
                    {'c', 4, 2, 4, "c4"},
                    {'c', 5, 2, 3, "c5"},
                    {'c', 6, 2, 2, "c6"},
                    {'c', 7, 2, 1, "c7"},
                    {'c', 8, 2, 0, "c8"},

                    {'d', 1, 3, 7, "d1"},
                    {'d', 2, 3, 6, "d2"},
                    {'d', 3, 3, 5, "d3"},
                    {'d', 4, 3, 4, "d4"},
                    {'d', 5, 3, 3, "d5"},
                    {'d', 6, 3, 2, "d6"},
                    {'d', 7, 3, 1, "d7"},
                    {'d', 8, 3, 0, "d8"},

                    {'e', 1, 4, 7, "e1"},
                    {'e', 2, 4, 6, "e2"},
                    {'e', 3, 4, 5, "e3"},
                    {'e', 4, 4, 4, "e4"},
                    {'e', 5, 4, 3, "e5"},
                    {'e', 6, 4, 2, "e6"},
                    {'e', 7, 4, 1, "e7"},
                    {'e', 8, 4, 0, "e8"},

                    {'f', 1, 5, 7, "f1"},
                    {'f', 2, 5, 6, "f2"},
                    {'f', 3, 5, 5, "f3"},
                    {'f', 4, 5, 4, "f4"},
                    {'f', 5, 5, 3, "f5"},
                    {'f', 6, 5, 2, "f6"},
                    {'f', 7, 5, 1, "f7"},
                    {'f', 8, 5, 0, "f8"},

                    {'g', 1, 6, 7, "g1"},
                    {'g', 2, 6, 6, "g2"},
                    {'g', 3, 6, 5, "g3"},
                    {'g', 4, 6, 4, "g4"},
                    {'g', 5, 6, 3, "g5"},
                    {'g', 6, 6, 2, "g6"},
                    {'g', 7, 6, 1, "g7"},
                    {'g', 8, 6, 0, "g8"},

                    {'h', 1, 7, 7, "h1"},
                    {'h', 2, 7, 6, "h2"},
                    {'h', 3, 7, 5, "h3"},
                    {'h', 4, 7, 4, "h4"},
                    {'h', 5, 7, 3, "h5"},
                    {'h', 6, 7, 2, "h6"},
                    {'h', 7, 7, 1, "h7"},
                    {'h', 8, 7, 0, "h8"},

                    //uppercase

                    {'A', 1, 0, 7, "a1"},
                    {'A', 2, 0, 6, "a2"},
                    {'A', 3, 0, 5, "a3"},
                    {'A', 4, 0, 4, "a4"},
                    {'A', 5, 0, 3, "a5"},
                    {'A', 6, 0, 2, "a6"},
                    {'A', 7, 0, 1, "a7"},
                    {'A', 8, 0, 0, "a8"},

                    {'B', 1, 1, 7, "b1"},
                    {'B', 2, 1, 6, "b2"},
                    {'B', 3, 1, 5, "b3"},
                    {'B', 4, 1, 4, "b4"},
                    {'B', 5, 1, 3, "b5"},
                    {'B', 6, 1, 2, "b6"},
                    {'B', 7, 1, 1, "b7"},
                    {'B', 8, 1, 0, "b8"},

                    {'C', 1, 2, 7, "c1"},
                    {'C', 2, 2, 6, "c2"},
                    {'C', 3, 2, 5, "c3"},
                    {'C', 4, 2, 4, "c4"},
                    {'C', 5, 2, 3, "c5"},
                    {'C', 6, 2, 2, "c6"},
                    {'C', 7, 2, 1, "c7"},
                    {'C', 8, 2, 0, "c8"},

                    {'D', 1, 3, 7, "d1"},
                    {'D', 2, 3, 6, "d2"},
                    {'D', 3, 3, 5, "d3"},
                    {'D', 4, 3, 4, "d4"},
                    {'D', 5, 3, 3, "d5"},
                    {'D', 6, 3, 2, "d6"},
                    {'D', 7, 3, 1, "d7"},
                    {'D', 8, 3, 0, "d8"},

                    {'E', 1, 4, 7, "e1"},
                    {'E', 2, 4, 6, "e2"},
                    {'E', 3, 4, 5, "e3"},
                    {'E', 4, 4, 4, "e4"},
                    {'E', 5, 4, 3, "e5"},
                    {'E', 6, 4, 2, "e6"},
                    {'E', 7, 4, 1, "e7"},
                    {'E', 8, 4, 0, "e8"},

                    {'F', 1, 5, 7, "f1"},
                    {'F', 2, 5, 6, "f2"},
                    {'F', 3, 5, 5, "f3"},
                    {'F', 4, 5, 4, "f4"},
                    {'F', 5, 5, 3, "f5"},
                    {'F', 6, 5, 2, "f6"},
                    {'F', 7, 5, 1, "f7"},
                    {'F', 8, 5, 0, "f8"},

                    {'G', 1, 6, 7, "g1"},
                    {'G', 2, 6, 6, "g2"},
                    {'G', 3, 6, 5, "g3"},
                    {'G', 4, 6, 4, "g4"},
                    {'G', 5, 6, 3, "g5"},
                    {'G', 6, 6, 2, "g6"},
                    {'G', 7, 6, 1, "g7"},
                    {'G', 8, 6, 0, "g8"},

                    {'H', 1, 7, 7, "h1"},
                    {'H', 2, 7, 6, "h2"},
                    {'H', 3, 7, 5, "h3"},
                    {'H', 4, 7, 4, "h4"},
                    {'H', 5, 7, 3, "h5"},
                    {'H', 6, 7, 2, "h6"},
                    {'H', 7, 7, 1, "h7"},
                    {'H', 8, 7, 0, "h8"}
            };
        }


        @Test
        public void getFile_isCorrect(){
            assertEquals(methodTestFailedMessage("getFile", validFile, validRank), expectedFile, coordinate.getFile());
        }

        @Test
        public void getRank_isCorrect(){
            assertEquals(methodTestFailedMessage("getRank", validFile, validRank), expectedRank, coordinate.getRank());
        }

        @Test
        public void getRow_isCorrect(){
            assertEquals(methodTestFailedMessage("getRow", validFile,validRank), expectedRow, coordinate.getRow());
        }

        @Test
        public void getColumn_isCorrect(){
            assertEquals(methodTestFailedMessage("getColumn", validFile, validRank), expectedColumn, coordinate.getColumn());
        }

        @Test
        public void toString_isCorrect(){
            assertEquals(methodTestFailedMessage("toString", validFile, validRank), expectedString, coordinate.toString());
        }

        private static String methodTestFailedMessage(String method, Character file, Integer rank) {
            return "Coordinate." + method + " test failed for " + file.toString() + rank.toString() + ". ";
        }
    }



    @RunWith(Parameterized.class)
    public static class ValidFileInvalidRankTest {

        @Parameter() //default value 0
        public char validFile;

        @Parameter(value = 1)
        public int invalidRank;

        @Parameters(name = "{index}: file is '{0}', rank is {1}. ")
        public static Object[][] invalidRankData() {
            return new Object[][]{

                    // file, rank

                    // valid lowercase
                    {'a', Integer.MIN_VALUE},// min value
                    {'a', -1},// negative value with absolute value that would be valid
                    {'a', 0},// one below valid
                    {'a', 9},// one above valid
                    {'a', Integer.MAX_VALUE},// max value

                    {'b', Integer.MIN_VALUE},
                    {'b', -1},
                    {'b', 0},
                    {'b', 9},
                    {'b', Integer.MAX_VALUE},

                    {'c', Integer.MIN_VALUE},
                    {'c', -1},
                    {'c', 0},
                    {'c', 9},
                    {'c', Integer.MAX_VALUE},

                    {'d', Integer.MIN_VALUE},
                    {'d', -1},
                    {'d', 0},
                    {'d', 9},
                    {'d', Integer.MAX_VALUE},

                    {'e', Integer.MIN_VALUE},
                    {'e', -1},
                    {'e', 0},
                    {'e', 9},
                    {'e', Integer.MAX_VALUE},

                    {'f', Integer.MIN_VALUE},
                    {'f', -1},
                    {'f', 0},
                    {'f', 9},
                    {'f', Integer.MAX_VALUE},

                    {'g', Integer.MIN_VALUE},
                    {'g', -1},
                    {'g', 0},
                    {'g', 9},
                    {'g', Integer.MAX_VALUE},

                    {'h', Integer.MIN_VALUE},
                    {'h', -1},
                    {'h', 0},
                    {'h', 9},
                    {'h', Integer.MAX_VALUE},

                    //valid uppercase file

                    {'A', Integer.MIN_VALUE},
                    {'A', -1},
                    {'A', 0},
                    {'A', 9},
                    {'A', Integer.MAX_VALUE},

                    {'B', Integer.MIN_VALUE},
                    {'B', -1},
                    {'B', 0},
                    {'B', 9},
                    {'B', Integer.MAX_VALUE},

                    {'C', Integer.MIN_VALUE},
                    {'C', -1},
                    {'C', 0},
                    {'C', 9},
                    {'C', Integer.MAX_VALUE},

                    {'D', Integer.MIN_VALUE},
                    {'D', -1},
                    {'D', 0},
                    {'D', 9},
                    {'D', Integer.MAX_VALUE},

                    {'E', Integer.MIN_VALUE},
                    {'E', -1},
                    {'E', 0},
                    {'E', 9},
                    {'E', Integer.MAX_VALUE},

                    {'F', Integer.MIN_VALUE},
                    {'F', -1},
                    {'F', 0},
                    {'F', 9},
                    {'F', Integer.MAX_VALUE},

                    {'G', Integer.MIN_VALUE},
                    {'G', -1},
                    {'G', 0},
                    {'G', 9},
                    {'G', Integer.MAX_VALUE},

                    {'H', Integer.MIN_VALUE},
                    {'H', -1},
                    {'H', 0},
                    {'H', 9},
                    {'H', Integer.MAX_VALUE},
            };
        }

        @Test(expected = IllegalArgumentException.class)
        public void validFileInvalidRank_ShouldThrowIllegalArgumentException(){
            new Coordinate(validFile, invalidRank);
        }
    }
    
    @RunWith(Parameterized.class)
    public static class InvalidFileInvalidRankTest {

        @Parameter() //default value 0
        public char invalidFile;

        @Parameter(value = 1)
        public int validRank;

        @Parameters(name = "{index}: file is '{0}', rank is {1}. ")
        public static Object[][] invalidRankData() {
            return new Object[][]{

                    // file, rank

                    // one less than valid
                    {(char)('a'-1), 1},
                    {(char)('a'-1), 2},
                    {(char)('a'-1), 3},
                    {(char)('a'-1), 4},
                    {(char)('a'-1), 5},
                    {(char)('a'-1), 6},
                    {(char)('a'-1), 7},
                    {(char)('a'-1), 8},
                    
                    {(char)('A'-1), 1},
                    {(char)('A'-1), 2},
                    {(char)('A'-1), 3},
                    {(char)('A'-1), 4},
                    {(char)('A'-1), 5},
                    {(char)('A'-1), 6},
                    {(char)('A'-1), 7},
                    {(char)('A'-1), 8},

                    // one greater than valid
                    {'i', 1},
                    {'i', 2},
                    {'i', 3},
                    {'i', 4},
                    {'i', 5},
                    {'i', 6},
                    {'i', 7},
                    {'i', 8},

                    {'I', 1},
                    {'I', 2},
                    {'I', 3},
                    {'I', 4},
                    {'I', 5},
                    {'I', 6},
                    {'I', 7},
                    {'I', 8},

                    // numeric character
                    {'1', 1},
                    {'1', 2},
                    {'1', 3},
                    {'1', 4},
                    {'1', 5},
                    {'1', 6},
                    {'1', 7},
                    {'1', 8},

                    // max value
                    {Character.MAX_VALUE, 1},
                    {Character.MAX_VALUE, 2},
                    {Character.MAX_VALUE, 3},
                    {Character.MAX_VALUE, 4},
                    {Character.MAX_VALUE, 5},
                    {Character.MAX_VALUE, 6},
                    {Character.MAX_VALUE, 7},
                    {Character.MAX_VALUE, 8},

                    // min value
                    {Character.MIN_VALUE, 1},
                    {Character.MIN_VALUE, 2},
                    {Character.MIN_VALUE, 3},
                    {Character.MIN_VALUE, 4},
                    {Character.MIN_VALUE, 5},
                    {Character.MIN_VALUE, 6},
                    {Character.MIN_VALUE, 7},
                    {Character.MIN_VALUE, 8},

            };
        }

        @Test(expected = IllegalArgumentException.class)
        public void validFileInvalidRank_ShouldThrowIllegalArgumentException(){
            new Coordinate(invalidFile, validRank);
        }
    }
    
    @RunWith(Parameterized.class)
    public static class InvalidFileValidRankTest {

        @Parameter() //default value 0
        public char invalidFile;

        @Parameter(value = 1)
        public int invalidRank;

        @Parameters(name = "{index}: file is '{0}', rank is {1}. ")
        public static Object[][] invalidRankData() {
            return new Object[][]{

                    // file, rank

                    {'a', Integer.MIN_VALUE},// min value
                    {'a', -1},// negative value with absolute value that would be valid
                    {'a', 0},// one below valid
                    {'a', 9},// one above valid
                    {'a', Integer.MAX_VALUE},// max value
                    
                    // one less than valid
                    {(char)('a'-1), Integer.MIN_VALUE},
                    {(char)('a'-1), -1},
                    {(char)('a'-1), 0},
                    {(char)('a'-1), 9},
                    {(char)('a'-1), Integer.MAX_VALUE},

                    {(char)('A'-1), Integer.MIN_VALUE},
                    {(char)('A'-1), -1},
                    {(char)('A'-1), 0},
                    {(char)('A'-1), 9},
                    {(char)('A'-1), Integer.MAX_VALUE},

                    // one greater than valid
                    {'i', Integer.MIN_VALUE},
                    {'i', -1},
                    {'i', 0},
                    {'i', 9},
                    {'i', Integer.MAX_VALUE},

                    {'I', Integer.MIN_VALUE},
                    {'I', -1},
                    {'I', 0},
                    {'I', 9},
                    {'I', Integer.MAX_VALUE},

                    // numeric character
                    {'1', Integer.MIN_VALUE},
                    {'1', -1},
                    {'1', 0},
                    {'1', 9},
                    {'1', Integer.MAX_VALUE},

                    // max value
                    {Character.MAX_VALUE, Integer.MIN_VALUE},
                    {Character.MAX_VALUE, -1},
                    {Character.MAX_VALUE, 0},
                    {Character.MAX_VALUE, 9},
                    {Character.MAX_VALUE, Integer.MAX_VALUE},

                    // min value
                    {Character.MIN_VALUE, Integer.MIN_VALUE},
                    {Character.MIN_VALUE, -1},
                    {Character.MIN_VALUE, 0},
                    {Character.MIN_VALUE, 9},
                    {Character.MIN_VALUE, Integer.MAX_VALUE},

            };
        }

        @Test(expected = IllegalArgumentException.class)
        public void validFileInvalidRank_ShouldThrowIllegalArgumentException(){
            new Coordinate(invalidFile, invalidRank);
        }


    }
}
