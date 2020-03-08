package com.example.jarchess.match;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GameboardUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public Coordinate mockCoordinate1 = mock(Coordinate.class);

    Gameboard gameboardUnderTest;

    @Before
    public void setup() {
        gameboardUnderTest = new Gameboard();
    }

    @Test
    public void clone_isCorrect() throws CloneNotSupportedException {
        when(mockCoordinate1.getColumn()).thenReturn(0);
        when(mockCoordinate1.getRow()).thenReturn(0);
        when(mockCoordinate1.getFile()).thenReturn('a');
        when(mockCoordinate1.getRank()).thenReturn(8);

        Gameboard clone = (Gameboard) gameboardUnderTest.clone();

        assertEquals("check that getClass method returns results that are equal", gameboardUnderTest.getClass(), clone.getClass());
        assertEquals("check that the gameboards are equal", gameboardUnderTest, clone);
        assertNotSame("check that the gameboards are not the same", gameboardUnderTest, clone);

        assertNotSame("check that piece at a8 is not same \noriginal: " + gameboardUnderTest.getPieceAt(mockCoordinate1) + "\nclone   : " + clone.getPieceAt(mockCoordinate1) + "\n", gameboardUnderTest.getPieceAt(mockCoordinate1), clone.getPieceAt(mockCoordinate1));
    }

    @Test
    public void getPieceAt_isCorrect() {
        fail();
    }

    @Test
    public void move_isCorrect() {
        fail();
    }

    @Test
    public void reset_isCorrect() {
        fail();
    }

    @Test
    public void remove_isCorrect() {
        fail();
    }

    @Test
    public void isEmptyA_isCorrectt() {
        fail();
    }
}