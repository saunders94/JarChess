package com.example.jarchess.match.view;

import android.view.View;
import android.widget.ImageView;

import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.styles.chesspiece.ChesspieceStyle;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.Coordinate.COLUMNS;

public class CapturedPiecesView {

    private static final int[] CAPTURED_ROWS = {0, 1};
    private final View view;
    private final ChesspieceStyle chesspieceStyle;
    private final ImageView[][][] capturedImageViews;
    private final MatchActivity activity;

    public CapturedPiecesView(MatchActivity matchActivity, ChesspieceStyle chesspieceStyle, ChessColor leftParticipantColor) {

        this.chesspieceStyle = chesspieceStyle;
        this.activity = matchActivity;
        this.view = activity.findViewById(R.id.capturedBar);


        capturedImageViews = new ImageView[ChessColor.values().length][COLUMNS.length][CAPTURED_ROWS.length];

        if (leftParticipantColor == WHITE) {
            int colorInt = WHITE.getIntValue();
            int capturedRow = convertRowToCapturedRow(6);
            int column = 0;
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn2);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn3);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn4);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn5);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftPawn6);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.leftPawn7);

            colorInt = BLACK.getIntValue();
            capturedRow = convertRowToCapturedRow(1);
            column = 0;
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn2);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn3);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn4);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn5);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightPawn6);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.rightPawn7);

            colorInt = WHITE.getIntValue();
            capturedRow = convertRowToCapturedRow(7);
            column = 0;
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftRook0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftKnight0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftBishop0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftKingOrQueen0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftKingOrQueen1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftBishop1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.leftKnight1);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.leftRook1);

            colorInt = BLACK.getIntValue();
            capturedRow = convertRowToCapturedRow(0);
            column = 0;
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightRook0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightKnight0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightBishop0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightKingOrQueen0);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightKingOrQueen1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightBishop1);
            capturedImageViews[colorInt][column++][capturedRow] = view.findViewById(R.id.rightKnight1);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.rightRook1);
        } else {

            int colorInt = BLACK.getIntValue();
            int capturedRow = convertRowToCapturedRow(6);
            int column = 7;
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn2);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn3);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn4);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn5);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftPawn6);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.leftPawn7);

            colorInt = WHITE.getIntValue();
            capturedRow = convertRowToCapturedRow(1);
            column = 7;
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn2);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn3);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn4);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn5);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightPawn6);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.rightPawn7);

            colorInt = BLACK.getIntValue();
            capturedRow = convertRowToCapturedRow(7);
            column = 7;
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftRook0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftKnight0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftBishop0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftKingOrQueen0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftKingOrQueen1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftBishop1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.leftKnight1);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.leftRook1);

            colorInt = WHITE.getIntValue();
            capturedRow = convertRowToCapturedRow(0);
            column = 7;
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightRook0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightKnight0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightBishop0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightKingOrQueen0);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightKingOrQueen1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightBishop1);
            capturedImageViews[colorInt][column--][capturedRow] = view.findViewById(R.id.rightKnight1);
            capturedImageViews[colorInt][column][capturedRow] = view.findViewById(R.id.rightRook1);

        }

        for(ChessColor color : ChessColor.values()){
            for(int row : CAPTURED_ROWS){
                for(int column : COLUMNS){
                    capturedImageViews[color.getIntValue()][column][row].setImageResource(chesspieceStyle.getResourceID(null));
                }
            }
        }

    }

    public void add(final Piece pieceTOAadd) {
        final ImageView imageView = getCapturedImageViewFor(pieceTOAadd.getColor(), pieceTOAadd.getStartingPosition());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(chesspieceStyle.getResourceID(pieceTOAadd));
            }
        });
    }

    private int convertRowToCapturedRow(int rowToConvert) {
        return (rowToConvert + 1) % 2;
    }

    private ImageView getCapturedImageViewFor(ChessColor color, Coordinate startingCoordinate){
        return capturedImageViews[color.getIntValue()][startingCoordinate.getColumn()][convertRowToCapturedRow(startingCoordinate.getRow())];
    }
}
