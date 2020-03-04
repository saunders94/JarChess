package com.example.jarchess.match.gui;

import android.view.View;
import android.widget.ImageView;

import com.example.jarchess.match.Coordinate;

class ChessboardViewSquare {

    private final SquareClickHandler handler;
    private final ImageView squareImageView;
    private Coordinate coordinate;

    public ChessboardViewSquare(final SquareClickHandler handler, ImageView imageView) {
        this.handler = handler;
        squareImageView = imageView;
        squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.handleSquareClick(coordinate);
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
