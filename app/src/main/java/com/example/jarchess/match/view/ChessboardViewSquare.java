package com.example.jarchess.match.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.activity.SquareClickHandler;


/**
 * A chessboard view square is the view's representation of a chessboard square.
 *
 * @author Joshua Zierman
 */
class ChessboardViewSquare {

    private static final int ORIGIN_TINT_COLOR = Color.argb(100,255,255,0) ;
    private static final int POSIBLE_DESTINATION_TINT_COLOR = Color.argb(50,0,255,0);
    private static final int CHOSEN_DESTINATION_TINT_COLOR = Color.argb(100,0,255,0);
    public static final PorterDuff.Mode SCREEN = PorterDuff.Mode.SCREEN;
    private final SquareClickHandler handler;
    private final ImageView squareImageView;
    private Coordinate coordinate;

    /**
     * Creates a chessboard view square.
     *
     * @param handler
     * @param imageView
     */
    public ChessboardViewSquare(final SquareClickHandler handler, ImageView imageView) {
        this.handler = handler;
        squareImageView = imageView;
        squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.observeSquareClick(coordinate);
            }
        });
    }

    public void setRotation(float rotationAmount) {
        squareImageView.setRotation(rotationAmount);
    }

    public void setSquareImageResource(int squareResourceID) {
        squareImageView.setBackgroundResource(squareResourceID);
    }

    public void setPieceImageResource(int pieceImageResourceID) {
        squareImageView.setImageResource(pieceImageResourceID);
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setIsClickable(boolean isClickable) {
        squareImageView.setClickable(isClickable);
    }

    public void clearOriginSelectionIndicator() {
        squareImageView.clearColorFilter();
    }

    public void setOriginSelectionIndicator() {
        squareImageView.setColorFilter(ORIGIN_TINT_COLOR, SCREEN);
    }

    public void clearPossibleDestinationIndicator() {
        squareImageView.clearColorFilter();
    }

    public void setPossibleDestinationIndicator() {
        squareImageView.setColorFilter(POSIBLE_DESTINATION_TINT_COLOR, SCREEN);
    }

    public void clearDestinationSelectionIndicator() {
        squareImageView.clearColorFilter();
    }

    public void setDestinationSelectionIndicator() {
        squareImageView.setColorFilter(CHOSEN_DESTINATION_TINT_COLOR, SCREEN);
    }
}
