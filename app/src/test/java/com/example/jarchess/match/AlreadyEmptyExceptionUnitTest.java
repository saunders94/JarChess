package com.example.jarchess.match;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AlreadyEmptyExceptionUnitTest.validTests.class,
        AlreadyEmptyExceptionUnitTest.InvalidTests.class
})
public class AlreadyEmptyExceptionUnitTest {

    @RunWith(Parameterized.class)
    public static class validTests {
        Gameboard.AlreadyEmptyException alreadyEmptyExceptionUnderTest;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Mock
        Coordinate mockCoordinate = mock(Coordinate.class);

        @Parameter
        public String coodrinateString;
        @Parameter(value = 1)
        public String expectedMessage;

        @Parameters(name = "{index}:{0} should have message \"{1}\"")
        public static Object[][] data() {
            return new Object[][]{
                    {"a1", "a1 is already empty."},
                    {"a2", "a2 is already empty."},
                    {"a3", "a3 is already empty."},
                    {"a4", "a4 is already empty."},
                    {"a5", "a5 is already empty."},
                    {"a6", "a6 is already empty."},
                    {"a7", "a7 is already empty."},
                    {"a8", "a8 is already empty."},

                    {"b1", "b1 is already empty."},
                    {"b2", "b2 is already empty."},
                    {"b3", "b3 is already empty."},
                    {"b4", "b4 is already empty."},
                    {"b5", "b5 is already empty."},
                    {"b6", "b6 is already empty."},
                    {"b7", "b7 is already empty."},
                    {"b8", "b8 is already empty."},

                    {"c1", "c1 is already empty."},
                    {"c2", "c2 is already empty."},
                    {"c3", "c3 is already empty."},
                    {"c4", "c4 is already empty."},
                    {"c5", "c5 is already empty."},
                    {"c6", "c6 is already empty."},
                    {"c7", "c7 is already empty."},
                    {"c8", "c8 is already empty."},

                    {"e1", "e1 is already empty."},
                    {"e2", "e2 is already empty."},
                    {"e3", "e3 is already empty."},
                    {"e4", "e4 is already empty."},
                    {"e5", "e5 is already empty."},
                    {"e6", "e6 is already empty."},
                    {"e7", "e7 is already empty."},
                    {"e8", "e8 is already empty."},

                    {"f1", "f1 is already empty."},
                    {"f2", "f2 is already empty."},
                    {"f3", "f3 is already empty."},
                    {"f4", "f4 is already empty."},
                    {"f5", "f5 is already empty."},
                    {"f6", "f6 is already empty."},
                    {"f7", "f7 is already empty."},
                    {"f8", "f8 is already empty."},

                    {"g1", "g1 is already empty."},
                    {"g2", "g2 is already empty."},
                    {"g3", "g3 is already empty."},
                    {"g4", "g4 is already empty."},
                    {"g5", "g5 is already empty."},
                    {"g6", "g6 is already empty."},
                    {"g7", "g7 is already empty."},
                    {"g8", "g8 is already empty."},

                    {"h1", "h1 is already empty."},
                    {"h2", "h2 is already empty."},
                    {"h3", "h3 is already empty."},
                    {"h4", "h4 is already empty."},
                    {"h5", "h5 is already empty."},
                    {"h6", "h6 is already empty."},
                    {"h7", "h7 is already empty."},
                    {"h8", "h8 is already empty."}
            };
        }

        @Before
        public void setup() {
            when(mockCoordinate.toString()).thenReturn(coodrinateString);

            alreadyEmptyExceptionUnderTest = new Gameboard.AlreadyEmptyException(mockCoordinate);
        }

        @Test
        public void getCoordinate_isCorrect() {
            Assert.assertSame(mockCoordinate, alreadyEmptyExceptionUnderTest.getCoordinateThatWasAlreadyEmpty());
        }

        @Test
        public void getMessage_isCorrect() {
            Assert.assertEquals(expectedMessage, alreadyEmptyExceptionUnderTest.getMessage());
        }
    }

    @SuppressWarnings("ThrowableNotThrown")
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
            InvalidTests.NullCoordinate.class,
            InvalidTests.BadCoordinate.class
    })
    public static class InvalidTests {


        @RunWith(JUnit4.class)
        public static class NullCoordinate {
            @Test(expected = IllegalArgumentException.class)
            public void ShouldThrowIllegalArgumentException() {
                //noinspection ConstantConditions
                new Gameboard.AlreadyEmptyException(null);
            }
        }

        @RunWith(Parameterized.class)
        public static class BadCoordinate {

            @Rule
            public MockitoRule rule = MockitoJUnit.rule();

            @Mock
            Coordinate mockCoordinate = mock(Coordinate.class);

            @Parameter
            public int column;

            @Parameter(value = 1)
            public int row;

            @Parameters(name = "{index}: column is {0}, row is {1}")
            public static Object[][] data() {
                return new Object[][]{
                        {0, Integer.MIN_VALUE},
                        {0, -1},
                        {0, 8},
                        {0, Integer.MAX_VALUE},


                        {Integer.MIN_VALUE, 0},
                        {-1, 0},
                        {8, 0},
                        {Integer.MAX_VALUE, 0},

                        {Integer.MIN_VALUE, Integer.MIN_VALUE},
                        {Integer.MIN_VALUE, -1},
                        {Integer.MIN_VALUE, 8},
                        {Integer.MIN_VALUE, Integer.MAX_VALUE},

                        {-1, Integer.MIN_VALUE},
                        {-1, -1},
                        {-1, 8},
                        {-1, Integer.MAX_VALUE},

                        {8, Integer.MIN_VALUE},
                        {8, -1},
                        {8, 8},
                        {8, Integer.MAX_VALUE},

                        {Integer.MAX_VALUE, Integer.MIN_VALUE},
                        {Integer.MAX_VALUE, -1},
                        {Integer.MAX_VALUE, 8},
                        {Integer.MAX_VALUE, Integer.MAX_VALUE},
                };
            }

            @Before
            public void setup() {
                when(mockCoordinate.getColumn()).thenReturn(column);
                when(mockCoordinate.getRow()).thenReturn(row);
            }

            @Test(expected = IllegalArgumentException.class)
            public void constructionWithBadCoordinateShouldThrowIllegalArgumentException() {
                new Gameboard.AlreadyEmptyException(mockCoordinate);
            }

            @After
            public void checkUntestedMethods() {
                verify(mockCoordinate, times(0)).getFile();
                verify(mockCoordinate, times(0)).getRank();
            }
        }
    }

}
