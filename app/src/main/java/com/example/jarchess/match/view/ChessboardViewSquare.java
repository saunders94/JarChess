package com.example.jarchess.match.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.activity.MatchActivity;

import static android.support.constraint.Constraints.TAG;


/**
 * A chessboard view square is the view's representation of a chessboard square.
 *
 * @author Joshua Zierman
 */
class ChessboardViewSquare {

    public static final PorterDuff.Mode SCREEN = PorterDuff.Mode.SCREEN;
    private static final int ORIGIN_TINT_COLOR = Color.argb(100, 255, 255, 0);
    private static final int POSIBLE_DESTINATION_TINT_COLOR = Color.argb(50, 0, 255, 0);
    private static final int CHOSEN_DESTINATION_TINT_COLOR = Color.argb(100, 0, 255, 0);
    private static final int PROMOTION_TINT_COLOR = Color.argb(150, 255, 255, 0);
    private final ImageView squareImageView;
    private Coordinate coordinate;
    private MatchActivity activity;

    /**
     * Creates a chessboard view square.
     *
     * @param matchActivity
     * @param imageView
     */
    public ChessboardViewSquare(final MatchActivity matchActivity, ImageView imageView) {
        this.activity = matchActivity;
        squareImageView = imageView;
        squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchActivity.observeSquareClick(coordinate);
            }
        });
    }

    public void clearDestinationSelectionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.clearColorFilter();
            }
        });
    }

    public void clearOriginSelectionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.clearColorFilter();
            }
        });
    }

    public void clearPossibleDestinationIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.clearColorFilter();
            }
        });
    }

    public void clearPromotionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.clearColorFilter();
            }
        });
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setDestinationSelectionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setColorFilter(CHOSEN_DESTINATION_TINT_COLOR, SCREEN);
            }
        });
    }

    public void setIsClickable(final boolean isClickable) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setClickable(isClickable);
            }
        });
    }

    public void setOriginSelectionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setColorFilter(ORIGIN_TINT_COLOR, SCREEN);
            }
        });
    }

    public void setPieceImageResource(final int pieceImageResourceID) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setImageResource(pieceImageResourceID);
            }
        });

    }

    public void setPossibleDestinationIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setColorFilter(POSIBLE_DESTINATION_TINT_COLOR, SCREEN);
            }
        });
    }

    public void setPromotionIndicator() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setColorFilter(PROMOTION_TINT_COLOR, SCREEN);
            }
        });
    }

    public void setRotation(float rotationAmount) {
        squareImageView.setRotation(rotationAmount);
    }

    public void setSquareImageResource(final int squareResourceID) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                squareImageView.setBackgroundResource(squareResourceID);

            }
        });
    }
}
