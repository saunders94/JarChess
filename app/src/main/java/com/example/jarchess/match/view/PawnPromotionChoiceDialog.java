package com.example.jarchess.match.view;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.pieces.Piece;

class PawnPromotionChoiceDialog {
    private final MatchActivity activity;
    private View view;
    private View backgroundFadeView;
    private Button queen, knight, rook, bishop;

    public PawnPromotionChoiceDialog(@NonNull MatchActivity matchActivity) {
        activity = matchActivity;


        view = activity.findViewById(R.id.promotionChoiceDialogFrameLayout);

        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);

        queen = view.findViewById(R.id.promotionChoiceQueenButton);
        knight = view.findViewById(R.id.promotionChoiceKnightButton);
        bishop = view.findViewById(R.id.promotionChoiceBishopButton);
        rook = view.findViewById(R.id.promotionChoiceRookButton);

        backgroundFadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        queen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(Piece.PromotionChoice.PROMOTE_TO_QUEEN);
                hide();
            }
        });

        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(Piece.PromotionChoice.PROMOTE_TO_KNIGHT);
                hide();
            }
        });


        bishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(Piece.PromotionChoice.PROMOTE_TO_BISHOP);
                hide();
            }
        });


        rook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(Piece.PromotionChoice.PROMOTE_TO_ROOK);
                hide();
            }
        });
    }

    public void hide() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void show() {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        });
    }
}
