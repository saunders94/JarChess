package com.example.jarchess.match.gui;

import android.view.View;
import android.widget.ImageView;

import com.example.jarchess.match.Coordinate;


/**
 * A chessboard view square is the view's representation of a chessboard square.
 */
class ChessboardViewSquare {

    private final SquareClickListener handler;
    private final ImageView squareImageView;
    private Coordinate coordinate;

    /**
     * Creates a chessboard view square.
     * @param handler
     * @param imageView
     */
    public ChessboardViewSquare(final SquareClickListener handler, ImageView imageView) {
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
}
