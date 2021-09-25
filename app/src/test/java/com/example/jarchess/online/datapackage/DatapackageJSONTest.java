package com.example.jarchess.online.datapackage;

import android.util.Log;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.pieces.PromotionChoice;
import com.example.jarchess.match.result.AgreedUponDrawResult;
import com.example.jarchess.match.result.CheckmateResult;
import com.example.jarchess.match.result.ExceptionResult;
import com.example.jarchess.match.result.FlagFallResult;
import com.example.jarchess.match.result.InvalidTurnReceivedResult;
import com.example.jarchess.match.result.RepetitionRuleDrawResult;
import com.example.jarchess.match.result.ResignationResult;
import com.example.jarchess.match.result.StalemateDrawResult;
import com.example.jarchess.match.result.XMoveRuleDrawResult;
import com.example.jarchess.match.turn.Turn;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.pieces.PromotionChoice.PROMOTE_TO_BISHOP;
import static com.example.jarchess.match.pieces.PromotionChoice.PROMOTE_TO_KNIGHT;
import static com.example.jarchess.match.pieces.PromotionChoice.PROMOTE_TO_QUEEN;
import static com.example.jarchess.match.pieces.PromotionChoice.PROMOTE_TO_ROOK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;

@RunWith(Enclosed.class)
public class DatapackageJSONTest {

    private static final int EXPECTED = 0;
    private static final int ACTUAL = 1;


    @RunWith(PowerMockRunner.class)
    @PrepareForTest({Log.class})
    public static class JSONToResultDatapackage {

        @Test
        public void agreedUponDraw() throws JSONException {
            Datapackage d = new Datapackage(new AgreedUponDrawResult());
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void checkmate() throws JSONException {
            Datapackage d = new Datapackage(new CheckmateResult(BLACK));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void exception() throws JSONException {
            Datapackage d = new Datapackage(new ExceptionResult(BLACK, "this is a message", new RuntimeException("this is an exception message")));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void flagFall() throws JSONException {
            Datapackage d = new Datapackage(new FlagFallResult(BLACK));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void invalidTurn() throws JSONException {
            Datapackage d = new Datapackage(new InvalidTurnReceivedResult(BLACK));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void repetitionDraw() throws JSONException {
            Datapackage d = new Datapackage(new RepetitionRuleDrawResult(5));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void resignation() throws JSONException {
            Datapackage d = new Datapackage(new ResignationResult(BLACK));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Before
        public void setup() {
            PowerMockito.mockStatic(Log.class);
            PowerMockito.when(Log.d(anyString(), anyString())).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {

                    Object[] arguments = invocation.getArguments();
                    System.out.println(arguments[0] + ": " + arguments[1]);
                    return null;
                }
            });
        }

        @Test
        public void stalemate() throws JSONException {
            Datapackage d = new Datapackage(new StalemateDrawResult());
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }

        @Test
        public void xMoveRule() throws JSONException {
            Datapackage d = new Datapackage(new XMoveRuleDrawResult(75));
            JSONObject j = d.getJSONObject();
            try {
                Datapackage d2 = Datapackage.JSON_CONVERTER.convertFromJSONObject(j);
                assertEquals(j.toString(), d2.getJSONObject().toString());
            } catch (Exception e) {
                System.out.println(e.toString());
                throw e;
            }
        }
    }


    @RunWith(JUnit4.class)
    public static class ResultDatapackageToJSON {

        @Test
        public void agreedUponDraw() throws JSONException {
            System.out.println(new Datapackage(new AgreedUponDrawResult()).getJSONObject().toString());
        }

        @Test
        public void checkmate() throws JSONException {
            System.out.println(new Datapackage(new CheckmateResult(BLACK)).getJSONObject().toString());
        }

        @Test
        public void exception() throws JSONException {
            System.out.println(new Datapackage(new ExceptionResult(BLACK, "this is a message", new RuntimeException("this is an exception message"))).getJSONObject().toString());
        }

        @Test
        public void flagFall() throws JSONException {
            System.out.println(new Datapackage(new FlagFallResult(BLACK)).getJSONObject().toString());
        }

        @Test
        public void invalidTurn() throws JSONException {
            System.out.println(new Datapackage(new InvalidTurnReceivedResult(BLACK)).getJSONObject().toString());
        }

        @Test
        public void repetitionDraw() throws JSONException {
            System.out.println(new Datapackage(new RepetitionRuleDrawResult(5)).getJSONObject().toString());
        }

        @Test
        public void resignation() throws JSONException {
            System.out.println(new Datapackage(new ResignationResult(BLACK)).getJSONObject().toString());
        }

        @Test
        public void stalemate() throws JSONException {
            System.out.println(new Datapackage(new StalemateDrawResult()).getJSONObject().toString());
        }

        @Test
        public void xMoveRule() throws JSONException {
            System.out.println(new Datapackage(new XMoveRuleDrawResult(75)).getJSONObject().toString());
        }
    }

    @RunWith(JUnit4.class)
    public static class ResultsJSON {

        @Test
        public void agreedUponDraw() throws JSONException {
            System.out.println("new AgreedUponDrawResult().getJSONObject().toString() = \"" + new AgreedUponDrawResult().getJSONObject().toString() + "\"");
        }

        @Test
        public void checkmate() throws JSONException {
            System.out.println("new CheckmateResult(BLACK).getJSONObject().toString() = \"" + new CheckmateResult(BLACK).getJSONObject().toString() + "\"");
        }

        @Test
        public void exception() throws JSONException {
            System.out.println("new ExceptionResult(BLACK, \"this is a message\", new RuntimeException(\"this is an exception message\")).getJSONObject().toString() = \"" + new ExceptionResult(BLACK, "this is a message", new RuntimeException("this is an exception message")).getJSONObject().toString() + "\"");
        }

        @Test
        public void flagFall() throws JSONException {
            System.out.println("new FlagFallResult(BLACK).getJSONObject().toString() = \"" + new FlagFallResult(BLACK).getJSONObject().toString() + "\"");
        }

        @Test
        public void invalidTurn() throws JSONException {
            System.out.println("new InvalidTurnReceivedResult(BLACK).getJSONObject().toString() = \"" + new InvalidTurnReceivedResult(BLACK).getJSONObject().toString() + "\"");
        }

        @Test
        public void repetitionDraw() throws JSONException {
            System.out.println("new RepetitionRuleDrawResult(5).getJSONObject().toString() = \"" + new RepetitionRuleDrawResult(5).getJSONObject().toString() + "\"");
        }

        @Test
        public void resignation() throws JSONException {
            System.out.println("new ResignationResult(BLACK).getJSONObject().toString() = \"" + new ResignationResult(BLACK).getJSONObject().toString() + "\"");
        }

        @Test
        public void stalemate() throws JSONException {
            System.out.println("new StalemateDrawResult().getJSONObject().toString() = \"" + new StalemateDrawResult().getJSONObject().toString() + "\"");
        }

        @Test
        public void xMoveRule() throws JSONException {
            System.out.println("new XMoveRuleDrawResult(75).getJSONObject().toString() = \"" + new XMoveRuleDrawResult(75).getJSONObject().toString() + "\"");
        }
    }

    @RunWith(Parameterized.class)
    public static class turnSingleMovement {

        /*
         * Test case parameters where generated using Joshua Zierman's custom pict utility program
         *
         * This software runs [pict](https://github.com/microsoft/pict) on a model file and uses the results to generate a CSV file and a Java snippet text file.
         */

        final static int PARAM_VALUE_ORIGIN_FILE = 0;
        final static int PARAM_VALUE_ORIGIN_RANK = 1;
        final static int PARAM_VALUE_DESTINATION_FILE = 2;
        final static int PARAM_VALUE_DESTINATION_RANK = 3;
        final static int PARAM_VALUE_TURN_COLOR = 4;
        final static int PARAM_VALUE_ELAPSED_TIME = 5;
        final static int PARAM_VALUE_PROMOTION_CHOICE = 6;


        @Parameter(value = PARAM_VALUE_ORIGIN_FILE)
        public char originFileParameter;
        @Parameter(value = PARAM_VALUE_ORIGIN_RANK)
        public int originRankParameter;
        @Parameter(value = PARAM_VALUE_DESTINATION_FILE)
        public char destinationFileParameter;
        @Parameter(value = PARAM_VALUE_DESTINATION_RANK)
        public int destinationRankParameter;
        @Parameter(value = PARAM_VALUE_TURN_COLOR)
        public ChessColor turnColorParameter;
        @Parameter(value = PARAM_VALUE_ELAPSED_TIME)
        public long elapsedTimeParameter;
        @Parameter(value = PARAM_VALUE_PROMOTION_CHOICE)
        public PromotionChoice promotionChoiceParameter;


        @Parameters(name = "{index} | originFile = {" + PARAM_VALUE_ORIGIN_FILE + "}, originRank = {" + PARAM_VALUE_ORIGIN_RANK + "}, destinationFile = {" + PARAM_VALUE_DESTINATION_FILE + "}, destinationRank = {" + PARAM_VALUE_DESTINATION_RANK + "}, turnColor = {" + PARAM_VALUE_TURN_COLOR + "}, elapsedTime = {" + PARAM_VALUE_ELAPSED_TIME + "}, promotionChoice = {" + PARAM_VALUE_PROMOTION_CHOICE + "}")
        public static Object[][] data() {
            return new Object[][]{
                    {/*originFile*/ 'h', /*originRank*/ 6, /*destinationFile*/ 'g', /*destinationRank*/ 2, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'b', /*originRank*/ 2, /*destinationFile*/ 'h', /*destinationRank*/ 1, /*turnColor*/ WHITE, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'c', /*originRank*/ 3, /*destinationFile*/ 'b', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'c', /*originRank*/ 5, /*destinationFile*/ 'c', /*destinationRank*/ 2, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ null},
                    {/*originFile*/ 'a', /*originRank*/ 2, /*destinationFile*/ 'e', /*destinationRank*/ 3, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'a', /*originRank*/ 7, /*destinationFile*/ 'd', /*destinationRank*/ 8, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'b', /*originRank*/ 1, /*destinationFile*/ 'b', /*destinationRank*/ 5, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'f', /*originRank*/ 2, /*destinationFile*/ 'a', /*destinationRank*/ 4, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'a', /*originRank*/ 4, /*destinationFile*/ 'g', /*destinationRank*/ 6, /*turnColor*/ WHITE, /*elapsedTime*/ 1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'e', /*originRank*/ 8, /*destinationFile*/ 'c', /*destinationRank*/ 8, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'g', /*originRank*/ 4, /*destinationFile*/ 'd', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ null},
                    {/*originFile*/ 'g', /*originRank*/ 6, /*destinationFile*/ 'f', /*destinationRank*/ 3, /*turnColor*/ WHITE, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'e', /*originRank*/ 6, /*destinationFile*/ 'e', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'f', /*originRank*/ 1, /*destinationFile*/ 'b', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'd', /*originRank*/ 2, /*destinationFile*/ 'c', /*destinationRank*/ 7, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'd', /*originRank*/ 4, /*destinationFile*/ 'h', /*destinationRank*/ 5, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'h', /*originRank*/ 8, /*destinationFile*/ 'g', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'g', /*originRank*/ 5, /*destinationFile*/ 'a', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'e', /*originRank*/ 3, /*destinationFile*/ 'd', /*destinationRank*/ 3, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'a', /*originRank*/ 7, /*destinationFile*/ 'b', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ -1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'g', /*originRank*/ 8, /*destinationFile*/ 'h', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'd', /*originRank*/ 6, /*destinationFile*/ 'b', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ null},
                    {/*originFile*/ 'd', /*originRank*/ 1, /*destinationFile*/ 'd', /*destinationRank*/ 2, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'b', /*originRank*/ 1, /*destinationFile*/ 'f', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ null},
                    {/*originFile*/ 'b', /*originRank*/ 3, /*destinationFile*/ 'a', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'c', /*originRank*/ 1, /*destinationFile*/ 'e', /*destinationRank*/ 4, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'b', /*originRank*/ 8, /*destinationFile*/ 'e', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'g', /*originRank*/ 2, /*destinationFile*/ 'f', /*destinationRank*/ 2, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'e', /*originRank*/ 7, /*destinationFile*/ 'a', /*destinationRank*/ 2, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'h', /*originRank*/ 5, /*destinationFile*/ 'd', /*destinationRank*/ 1, /*turnColor*/ WHITE, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'd', /*originRank*/ 3, /*destinationFile*/ 'e', /*destinationRank*/ 6, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'a', /*originRank*/ 5, /*destinationFile*/ 'f', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'e', /*originRank*/ 5, /*destinationFile*/ 'h', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'h', /*originRank*/ 2, /*destinationFile*/ 'e', /*destinationRank*/ 3, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'e', /*originRank*/ 8, /*destinationFile*/ 'b', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'g', /*originRank*/ 7, /*destinationFile*/ 'c', /*destinationRank*/ 6, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'c', /*originRank*/ 4, /*destinationFile*/ 'h', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'b', /*originRank*/ 5, /*destinationFile*/ 'g', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'f', /*originRank*/ 7, /*destinationFile*/ 'g', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'f', /*originRank*/ 3, /*destinationFile*/ 'e', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ null},
                    {/*originFile*/ 'f', /*originRank*/ 7, /*destinationFile*/ 'h', /*destinationRank*/ 3, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'e', /*originRank*/ 2, /*destinationFile*/ 'g', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'a', /*originRank*/ 1, /*destinationFile*/ 'a', /*destinationRank*/ 8, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'f', /*originRank*/ 6, /*destinationFile*/ 'c', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'c', /*originRank*/ 6, /*destinationFile*/ 'f', /*destinationRank*/ 3, /*turnColor*/ WHITE, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'h', /*originRank*/ 4, /*destinationFile*/ 'c', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'f', /*originRank*/ 4, /*destinationFile*/ 'e', /*destinationRank*/ 2, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'c', /*originRank*/ 6, /*destinationFile*/ 'd', /*destinationRank*/ 5, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'h', /*originRank*/ 8, /*destinationFile*/ 'a', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ null},
                    {/*originFile*/ 'f', /*originRank*/ 8, /*destinationFile*/ 'd', /*destinationRank*/ 7, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'd', /*originRank*/ 7, /*destinationFile*/ 'f', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'e', /*originRank*/ 4, /*destinationFile*/ 'b', /*destinationRank*/ 4, /*turnColor*/ WHITE, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'a', /*originRank*/ 6, /*destinationFile*/ 'a', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'd', /*originRank*/ 4, /*destinationFile*/ 'a', /*destinationRank*/ 3, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'f', /*originRank*/ 5, /*destinationFile*/ 'f', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'b', /*originRank*/ 3, /*destinationFile*/ 'f', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'c', /*originRank*/ 2, /*destinationFile*/ 'd', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'a', /*originRank*/ 3, /*destinationFile*/ 'h', /*destinationRank*/ 2, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'd', /*originRank*/ 8, /*destinationFile*/ 'g', /*destinationRank*/ 3, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'e', /*originRank*/ 1, /*destinationFile*/ 'f', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'g', /*originRank*/ 5, /*destinationFile*/ 'e', /*destinationRank*/ 8, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'h', /*originRank*/ 7, /*destinationFile*/ 'b', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'b', /*originRank*/ 4, /*destinationFile*/ 'f', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'b', /*originRank*/ 8, /*destinationFile*/ 'b', /*destinationRank*/ 2, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'd', /*originRank*/ 6, /*destinationFile*/ 'h', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'h', /*originRank*/ 1, /*destinationFile*/ 'h', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'g', /*originRank*/ 3, /*destinationFile*/ 'g', /*destinationRank*/ 1, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_QUEEN},
                    {/*originFile*/ 'h', /*originRank*/ 6, /*destinationFile*/ 'f', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'h', /*originRank*/ 3, /*destinationFile*/ 'c', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'c', /*originRank*/ 7, /*destinationFile*/ 'a', /*destinationRank*/ 7, /*turnColor*/ WHITE, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'g', /*originRank*/ 1, /*destinationFile*/ 'g', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'a', /*originRank*/ 8, /*destinationFile*/ 'c', /*destinationRank*/ 4, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MIN_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'e', /*originRank*/ 2, /*destinationFile*/ 'b', /*destinationRank*/ 1, /*turnColor*/ WHITE, /*elapsedTime*/ 0L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'f', /*originRank*/ 2, /*destinationFile*/ 'c', /*destinationRank*/ 5, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'c', /*originRank*/ 8, /*destinationFile*/ 'f', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'g', /*originRank*/ 5, /*destinationFile*/ 'b', /*destinationRank*/ 3, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'b', /*originRank*/ 1, /*destinationFile*/ 'c', /*destinationRank*/ 3, /*turnColor*/ WHITE, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'd', /*originRank*/ 5, /*destinationFile*/ 'g', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ PROMOTE_TO_KNIGHT},
                    {/*originFile*/ 'b', /*originRank*/ 7, /*destinationFile*/ 'a', /*destinationRank*/ 6, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_BISHOP},
                    {/*originFile*/ 'b', /*originRank*/ 6, /*destinationFile*/ 'd', /*destinationRank*/ 4, /*turnColor*/ BLACK, /*elapsedTime*/ 1L, /*promotionChoice*/ PROMOTE_TO_ROOK},
                    {/*originFile*/ 'h', /*originRank*/ 2, /*destinationFile*/ 'e', /*destinationRank*/ 8, /*turnColor*/ WHITE, /*elapsedTime*/ Long.MAX_VALUE, /*promotionChoice*/ null},
                    {/*originFile*/ 'g', /*originRank*/ 7, /*destinationFile*/ 'e', /*destinationRank*/ 7, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ null},
                    {/*originFile*/ 'c', /*originRank*/ 1, /*destinationFile*/ 'g', /*destinationRank*/ 7, /*turnColor*/ BLACK, /*elapsedTime*/ -1L, /*promotionChoice*/ PROMOTE_TO_QUEEN}
            };
        }

        @Test
        public void singlePieceMovement() throws JSONException {


            Coordinate[] origin = new Coordinate[2];
            Coordinate[] destination = new Coordinate[2];
            ChessColor[] turnColor = new ChessColor[2];
            long[] elapsedTime = new long[2];
            PromotionChoice[] chosenPromotion = new PromotionChoice[2];


            origin[EXPECTED] = Coordinate.getByFileAndRank(originFileParameter, originRankParameter);
            destination[EXPECTED] = Coordinate.getByFileAndRank(destinationFileParameter, destinationRankParameter);
            Move move = new Move(origin[EXPECTED], destination[EXPECTED]);
            ChessColor color = turnColor[EXPECTED] = turnColorParameter;
            long time = elapsedTime[EXPECTED] = elapsedTimeParameter;
            PromotionChoice pieceType = chosenPromotion[EXPECTED] = promotionChoiceParameter;


            Turn turnExpected = new Turn(color, move, time, pieceType);

            Datapackage datapackageUnderTest = new Datapackage(turnExpected);
            JSONObject jsonObject = datapackageUnderTest.getJSONObject();

            Datapackage resultingDatapackage = Datapackage.JSON_CONVERTER.convertFromJSONObject(jsonObject);
            Turn resultingTurn = resultingDatapackage.getTurn();
            Move resultingMove = resultingTurn.getMove();
            PieceMovement[] resultingMoments = new PieceMovement[0];
            resultingMoments = resultingMove.toArray(resultingMoments);

            assertEquals("resultingMoments.length != 1", 1, resultingMoments.length);

            origin[ACTUAL] = resultingMoments[0].getOrigin();
            destination[ACTUAL] = resultingMoments[0].getDestination();
            turnColor[ACTUAL] = resultingTurn.getColor();
            elapsedTime[ACTUAL] = resultingTurn.getElapsedTime();
            chosenPromotion[ACTUAL] = resultingTurn.getPromotionChoice();

            assertSame("origins not same.", origin[EXPECTED], origin[ACTUAL]);
            assertSame("destinations not same.", destination[EXPECTED], destination[ACTUAL]);
            assertSame("turn color not same.", turnColor[EXPECTED], turnColor[ACTUAL]);
            assertEquals("elapsed time not same.", elapsedTime[EXPECTED], elapsedTime[ACTUAL]);
            assertSame("chosen promotion is not the same.", chosenPromotion[EXPECTED], chosenPromotion[ACTUAL]);
        }

//        @Rule
//        public MockitoRule rule = MockitoJUnit.rule();
//
//        @Mock
//        public Turn turnMock = mock(Turn.class);
//
//        @Mock
//        public JSONObject turnJSONObjectMock = mock(JSONObject.class);
//
//        @Mock
//        public ResignationResult resignationMock = mock(ResignationResult.class);
//
//        @Mock
//        public JSONObject resignationJSONObjectMock = mock(JSONObject.class);
//
//
//        @Test
//        public void t(){
//            when(turnMock.getJSONObject()).thenReturn(turnJSONObjectMock);
//            when(turnJSONObjectMock.get())
//            assertEquals(originFile, destinationFile);
//        }
    }
//
//    @RunWith(Parameterized.class)
//    public class tests{
//
//
//        private static final int PARAM_ORIGIN = 0;
//        private static final int PARAM_DESTINATION = 1;
//        private static final int PARAM_COLOR = 2;
//        private static final int PARAM_ELAPSED_TIME = 3;
//        private static final int PARAM_PROMOTION_CHOICE = 4;
//        private static final String PARAMS_NAME = String.format(
//                "{index}: {%d} turn, {%d}->{%d}, took {%d}ms, promotion choice is {%d}.",
//                PARAM_COLOR, PARAM_ORIGIN, PARAM_DESTINATION, PARAM_ELAPSED_TIME, PARAM_PROMOTION_CHOICE);
//
//        @Parameter(value = PARAM_ORIGIN)
//        public Coordinate providedOrigin;
//
//        @Parameter(value = PARAM_DESTINATION)
//        public Coordinate providedDestination;
//
//        @Parameter(value = PARAM_COLOR)
//        public Coordinate providedColor;
//
//        @Parameter(value = PARAM_ELAPSED_TIME)
//        public Coordinate providedElapsedTime;
//
//        @Parameter(value = PARAM_PROMOTION_CHOICE)
//        public Coordinate providedPromotionChoice;
//
//        @Parameters(name = "{index}: {"+PARAM_COLOR+"} turn, {"+PARAM_ORIGIN+"}->{"+PARAM_DESTINATION+"}, took {"+PARAM_ELAPSED_TIME+"}ms, promotion choice is {"+PARAM_PROMOTION_CHOICE+"}.")
//        public static Object[][] data() {
//
//            Collection<Object[]> data = new LinkedList<Object[]>();
//
//            Coordinate[] coordinatesToTest = {
//                    Coordinate.getByColumnAndRow(0,0),
//                    Coordinate.getByColumnAndRow(0,1),
//                    Coordinate.getByColumnAndRow(0,7),
//                    Coordinate.getByColumnAndRow(7,0),
//                    Coordinate.getByColumnAndRow(7,7),
//            };
//
//            long[] elapsedTimesToTest = {Long.MIN_VALUE, -1L, 0L, 1L, Long.MAX_VALUE};
//            Piece.PromotionChoice[] promotionTypesToTest = {PROMOTE_TO_QUEEN, PROMOTE_TO_KNIGHT, PROMOTE_TO_ROOK, PROMOTE_TO_BISHOP};
//
//            for(Coordinate origin : coordinatesToTest){
//                for(Coordinate destination : coordinatesToTest){
//                    if(origin != destination){
//                        for(ChessColor color : ChessColor.values()){
//                            for(long elapsedTime : elapsedTimesToTest){
//                                for(Piece.PromotionChoice promotionType : promotionTypesToTest){
//                                    data.add(new Object[]{origin, destination, color, elapsedTime, promotionType});
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            return data.toArray(new Object[0][]);
//        }
//
//
//        @Test
//        public void test(){
//            throw e;
//        }
//    }

}