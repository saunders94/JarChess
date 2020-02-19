package com.example.jarchess.match;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AlreadyOccupiedExceptionUnitTest.validTests.class,
        AlreadyOccupiedExceptionUnitTest.InvalidTests.class
})
public class AlreadyOccupiedExceptionUnitTest {
    
    @RunWith(Parameterized.class)
    public static class validTests{
        Gameboard.AlreadyOccupiedException alreadyOccupiedExceptionUnderTest;

        @Rule
        public MockitoRule rule = MockitoJUnit.rule();

        @Mock Coordinate mockCoordinate = mock(Coordinate.class);

        @Parameter public String coodrinateString;
        @Parameter(value = 1) public String expectedMessage;
        
        @Parameters (name = "{index}:{0} should have message \"{1}\"")
        public static Object[][] data() {
            return new Object[][]{
                    {"a1", "a1 is already occupied."},
                    {"a2", "a2 is already occupied."},
                    {"a3", "a3 is already occupied."},
                    {"a4", "a4 is already occupied."},
                    {"a5", "a5 is already occupied."},
                    {"a6", "a6 is already occupied."},
                    {"a7", "a7 is already occupied."},
                    {"a8", "a8 is already occupied."},

                    {"b1", "b1 is already occupied."},
                    {"b2", "b2 is already occupied."},
                    {"b3", "b3 is already occupied."},
                    {"b4", "b4 is already occupied."},
                    {"b5", "b5 is already occupied."},
                    {"b6", "b6 is already occupied."},
                    {"b7", "b7 is already occupied."},
                    {"b8", "b8 is already occupied."},

                    {"c1", "c1 is already occupied."},
                    {"c2", "c2 is already occupied."},
                    {"c3", "c3 is already occupied."},
                    {"c4", "c4 is already occupied."},
                    {"c5", "c5 is already occupied."},
                    {"c6", "c6 is already occupied."},
                    {"c7", "c7 is already occupied."},
                    {"c8", "c8 is already occupied."},

                    {"e1", "e1 is already occupied."},
                    {"e2", "e2 is already occupied."},
                    {"e3", "e3 is already occupied."},
                    {"e4", "e4 is already occupied."},
                    {"e5", "e5 is already occupied."},
                    {"e6", "e6 is already occupied."},
                    {"e7", "e7 is already occupied."},
                    {"e8", "e8 is already occupied."},

                    {"f1", "f1 is already occupied."},
                    {"f2", "f2 is already occupied."},
                    {"f3", "f3 is already occupied."},
                    {"f4", "f4 is already occupied."},
                    {"f5", "f5 is already occupied."},
                    {"f6", "f6 is already occupied."},
                    {"f7", "f7 is already occupied."},
                    {"f8", "f8 is already occupied."},

                    {"g1", "g1 is already occupied."},
                    {"g2", "g2 is already occupied."},
                    {"g3", "g3 is already occupied."},
                    {"g4", "g4 is already occupied."},
                    {"g5", "g5 is already occupied."},
                    {"g6", "g6 is already occupied."},
                    {"g7", "g7 is already occupied."},
                    {"g8", "g8 is already occupied."},

                    {"h1", "h1 is already occupied."},
                    {"h2", "h2 is already occupied."},
                    {"h3", "h3 is already occupied."},
                    {"h4", "h4 is already occupied."},
                    {"h5", "h5 is already occupied."},
                    {"h6", "h6 is already occupied."},
                    {"h7", "h7 is already occupied."},
                    {"h8", "h8 is already occupied."}
            };
        }

        @Before
        public void setup(){
            when(mockCoordinate.toString()).thenReturn(coodrinateString);

            alreadyOccupiedExceptionUnderTest = new Gameboard.AlreadyOccupiedException(mockCoordinate);
        }

        @Test
        public void getCoordinate_isCorrect(){
            Assert.assertSame(mockCoordinate, alreadyOccupiedExceptionUnderTest.getCoordinateThatWasAlreadyOccupied());
        }

        @Test
        public void getMessage_isCorrect(){
            Assert.assertEquals(expectedMessage, alreadyOccupiedExceptionUnderTest.getMessage());
        }
    }

    @RunWith(JUnit4.class)
    public static class InvalidTests{

         @SuppressWarnings("ThrowableNotThrown")
         @Test(expected = IllegalArgumentException.class)
        public void constructionWithNullCoordinateShouldThrowIllegalArgumentException(){
             new Gameboard.AlreadyOccupiedException(null);
         }
    }
    
}
