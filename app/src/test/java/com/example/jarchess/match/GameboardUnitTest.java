package com.example.jarchess.match;

import com.example.jarchess.match.chessboard.Chessboard;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class GameboardUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public Coordinate mockCoordinate1 = mock(Coordinate.class);

    Chessboard gameboardUnderTest;

    @Before
    public void setup() {
        gameboardUnderTest = new Chessboard();
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