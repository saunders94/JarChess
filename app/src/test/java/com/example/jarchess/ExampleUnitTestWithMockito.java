package com.example.jarchess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTestWithMockito {


    // These interfaces represent what we are going to mock...
    private interface GamePiece{ String getName();}// remove
    private interface Pawn extends GamePiece{}// remove

    // This is representing the class we are testing
    // remove this class
    private static class SystemUnderTest{
        GamePiece piece;

        SystemUnderTest(GamePiece piece){
            this.piece = piece;
        }
         boolean isPromotable(){
//            return false;//Failing Test
           return piece instanceof Pawn;//Passing Test
        }
         int countCharsInName(){
//            return -1;//Failing Test
            return piece.getName().length();//Passing Test
        }
    }

    @Mock
    Pawn mockPawn = mock(Pawn.class); // replace with what you want to mock


    @Test
    public void pawn_IsPromotablePiece_isCorrect()
    {
        final boolean expected_isPromotable = true;
        boolean actual_isPromotable;

        SystemUnderTest SUT = new SystemUnderTest(mockPawn);

        actual_isPromotable = SUT.isPromotable();

        assertEquals(expected_isPromotable, actual_isPromotable);
    }


    @Test
    public void pawn_CharacterLength_is4()
    {
        final int expected_length = 4;
        int actual_length;

        when(mockPawn.getName()).thenReturn("pawn");

        SystemUnderTest SUT = new SystemUnderTest(mockPawn);

        actual_length = SUT.countCharsInName();

        assertEquals(expected_length, actual_length);
    }
}