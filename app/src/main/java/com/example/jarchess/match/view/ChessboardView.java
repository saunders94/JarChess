package com.example.jarchess.match.view;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;
import static com.example.jarchess.match.Coordinate.COLUMNS;
import static com.example.jarchess.match.Coordinate.ROWS;

//TODO javadocs
class ChessboardView {

    private static final float DELTA = 0.1f;
    private final View view;
    private final ChesspieceStyle chesspieceStyle;
    private final ChessboardStyle chessboardStyle;
    private final Match match;
    private final MatchActivity activity;
    private final ChessboardViewSquare[][] squares = new ChessboardViewSquare[COLUMNS.length][Coordinate.ROWS.length];

    private ChessColor currentPerspective;

    public ChessboardView(View view, ChesspieceStyle chesspieceStyle, ChessboardStyle chessboardStyle, ChessColor perspective, Match match, MatchActivity matchActivity) {
        this.view = view;
        this.chesspieceStyle = chesspieceStyle;
        this.chessboardStyle = chessboardStyle;
        this.match = match;
        this.activity = matchActivity;

        setPerspective(perspective);
    }

    public void setPerspective(ChessColor perspectiveColor) {
        switch (perspectiveColor) {

            case BLACK:
                if (currentPerspective == null) {
                    setViewOrientationBlackPerspective();
                } else if (currentPerspective != BLACK) {
                    rotateOrientation();
                }
                break;
            case WHITE:
                if (currentPerspective == null) {
                    setViewOrientationWhitePerspective();
                } else if (currentPerspective != WHITE) {
                    rotateOrientation();
                }
                break;


            default:
                throw new IllegalStateException("Unexpected color value: " + perspectiveColor);
        }
    }

    private void rotateOrientation() {

        ChessboardViewSquare tmp;
        int maxRow = ROWS.length / 2;
        int maxColumn = COLUMNS.length / 2;
        for (int row = 0; row < maxRow; row++) {
            for (int column = 0; column < maxColumn; column++) {

                int columnToSwapWith = 7 - column;
                int rowToSwapWith = 7 - row;

                tmp = squares[column][row];
                squares[column][row] = squares[columnToSwapWith][rowToSwapWith];
                squares[columnToSwapWith][rowToSwapWith] = tmp;

            }
        }

        currentPerspective = ChessColor.getOther(currentPerspective);
        float rotationAmount = currentPerspective == WHITE ? 0 : 180;

        for (int row : ROWS) {
            for (int column : COLUMNS) {
                squares[column][row].setRotation(rotationAmount);
            }
        }


        for (int row : ROWS) {
            for (int column : COLUMNS) {

            }
        }

        updateAllSquares();

    }

    private void updateAllSquares() {
        for (int row : Coordinate.ROWS) {
            for (int column : COLUMNS) {

                Coordinate coordinate = Coordinate.getByColumnAndRow(column, row);
                Piece piece = match.getPieceAt(coordinate);
                ChessboardViewSquare s = squares[column][row];

                s.setCoordinate(coordinate);

                // update background images
                s.setSquareImageResource(chessboardStyle.getSquareResourceID(coordinate));

                // update piece images
                s.setPieceImageResource(chesspieceStyle.getResourceID(piece));
//
//                if(piece == null){
//                    s.setIsClickable(false);
//                }else{
//                    s.setIsClickable(true);
//                }
            }
        }
    }


    private void setViewOrientationBlackPerspective() {

        //layout the board so black player is at bottom of screen

        Object coordinate;
        squares[7][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_7)));
        squares[7][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_6)));
        squares[7][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_5)));
        squares[7][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_4)));
        squares[7][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_3)));
        squares[7][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_2)));
        squares[7][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_1)));
        squares[7][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_0)));


        squares[6][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_7)));
        squares[6][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_6)));
        squares[6][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_5)));
        squares[6][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_4)));
        squares[6][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_3)));
        squares[6][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_2)));
        squares[6][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_1)));
        squares[6][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_0)));


        squares[5][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_7)));
        squares[5][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_6)));
        squares[5][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_5)));
        squares[5][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_4)));
        squares[5][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_3)));
        squares[5][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_2)));
        squares[5][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_1)));
        squares[5][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_0)));


        squares[4][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_7)));
        squares[4][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_6)));
        squares[4][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_5)));
        squares[4][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_4)));
        squares[4][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_3)));
        squares[4][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_2)));
        squares[4][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_1)));
        squares[4][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_0)));


        squares[3][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_7)));
        squares[3][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_6)));
        squares[3][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_5)));
        squares[3][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_4)));
        squares[3][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_3)));
        squares[3][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_2)));
        squares[3][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_1)));
        squares[3][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_0)));


        squares[2][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_7)));
        squares[2][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_6)));
        squares[2][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_5)));
        squares[2][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_4)));
        squares[2][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_3)));
        squares[2][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_2)));
        squares[2][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_1)));
        squares[2][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_0)));


        squares[1][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_7)));
        squares[1][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_6)));
        squares[1][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_5)));
        squares[1][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_4)));
        squares[1][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_3)));
        squares[1][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_2)));
        squares[1][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_1)));
        squares[1][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_0)));


        squares[0][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_7)));
        squares[0][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_6)));
        squares[0][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_5)));
        squares[0][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_4)));
        squares[0][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_3)));
        squares[0][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_2)));
        squares[0][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_1)));
        squares[0][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_0)));

        for (int row : ROWS) {
            for (int column : COLUMNS) {
                squares[column][row].setCoordinate(Coordinate.getByColumnAndRow(column, row));
            }
        }

        updateAllSquares();
    }

    private void setViewOrientationWhitePerspective() {

        // sets the orientation so the white player is at the bottom of the screen

        squares[0][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_7)));
        squares[0][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_6)));
        squares[0][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_5)));
        squares[0][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_4)));
        squares[0][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_3)));
        squares[0][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_2)));
        squares[0][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_1)));
        squares[0][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_0_0)));


        squares[1][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_7)));
        squares[1][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_6)));
        squares[1][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_5)));
        squares[1][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_4)));
        squares[1][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_3)));
        squares[1][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_2)));
        squares[1][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_1)));
        squares[1][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_1_0)));


        squares[2][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_7)));
        squares[2][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_6)));
        squares[2][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_5)));
        squares[2][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_4)));
        squares[2][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_3)));
        squares[2][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_2)));
        squares[2][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_1)));
        squares[2][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_2_0)));


        squares[3][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_7)));
        squares[3][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_6)));
        squares[3][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_5)));
        squares[3][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_4)));
        squares[3][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_3)));
        squares[3][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_2)));
        squares[3][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_1)));
        squares[3][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_3_0)));


        squares[4][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_7)));
        squares[4][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_6)));
        squares[4][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_5)));
        squares[4][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_4)));
        squares[4][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_3)));
        squares[4][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_2)));
        squares[4][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_1)));
        squares[4][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_4_0)));


        squares[5][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_7)));
        squares[5][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_6)));
        squares[5][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_5)));
        squares[5][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_4)));
        squares[5][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_3)));
        squares[5][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_2)));
        squares[5][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_1)));
        squares[5][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_5_0)));


        squares[6][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_7)));
        squares[6][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_6)));
        squares[6][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_5)));
        squares[6][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_4)));
        squares[6][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_3)));
        squares[6][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_2)));
        squares[6][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_1)));
        squares[6][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_6_0)));


        squares[7][7] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_7)));
        squares[7][6] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_6)));
        squares[7][5] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_5)));
        squares[7][4] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_4)));
        squares[7][3] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_3)));
        squares[7][2] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_2)));
        squares[7][1] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_1)));
        squares[7][0] = new ChessboardViewSquare(activity, (ImageView) (view.findViewById(R.id.SquareImageView_7_0)));


        for (int row : ROWS) {
            for (int column : COLUMNS) {
                squares[column][row].setCoordinate(Coordinate.getByColumnAndRow(column, row));
            }
        }

        updateAllSquares();

    }

    public void clearOriginSelectionIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).clearOriginSelectionIndicator();
    }

    public void setOriginSelectionIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).setOriginSelectionIndicator();
    }

    public void clearPossibleDestinationIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).clearPossibleDestinationIndicator();
    }

    public void setPossibleDestinationIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).setPossibleDestinationIndicator();
    }

    public void clearDestinationSelectionIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).clearDestinationSelectionIndicator();
    }

    public void setDestinationSelectionIndicator(@NonNull Coordinate coordinate) {
        squareAt(coordinate).setDestinationSelectionIndicator();
    }

    private ChessboardViewSquare squareAt(@NonNull Coordinate coordinate) {
        return squares[coordinate.getColumn()][coordinate.getRow()];
    }

    public void updatePiece(@NonNull Coordinate coordinate) {
        Piece piece = match.getPieceAt(coordinate);
        ChessboardViewSquare s = squareAt(coordinate);

        s.setCoordinate(coordinate);

        // update piece images
        s.setPieceImageResource(chesspieceStyle.getResourceID(piece));
    }
}
