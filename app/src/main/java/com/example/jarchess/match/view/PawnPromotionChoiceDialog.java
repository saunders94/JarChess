package com.example.jarchess.match.view;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jarchess.R;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.pieces.PromotionChoice;

class PawnPromotionChoiceDialog {
    private static final int MINIMUM_HEIGHT = 120;
    private final MatchView matchView;
    private final MatchActivity activity;
    private ViewGroup viewGroup;
    private View backgroundFadeView;
    private Button queen, knight, rook, bishop;

    public PawnPromotionChoiceDialog(final MatchView matchView) {
        this.matchView = matchView;
        activity = matchView.getActivity();


        viewGroup = activity.findViewById(R.id.promotionChoiceDialogFrameLayout);

        backgroundFadeView = activity.findViewById(R.id.fadeFrameLayoutBottom);

        queen = viewGroup.findViewById(R.id.promotionChoiceQueenButton);
        knight = viewGroup.findViewById(R.id.promotionChoiceKnightButton);
        bishop = viewGroup.findViewById(R.id.promotionChoiceBishopButton);
        rook = viewGroup.findViewById(R.id.promotionChoiceRookButton);

        backgroundFadeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        queen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(PromotionChoice.PROMOTE_TO_QUEEN);
                hide();
            }
        });

        knight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(PromotionChoice.PROMOTE_TO_KNIGHT);
                hide();
            }
        });


        bishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(PromotionChoice.PROMOTE_TO_BISHOP);
                hide();
            }
        });


        rook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setPromotionChoiceInput(PromotionChoice.PROMOTE_TO_ROOK);
                matchView.enableDrawButton();
                hide();
            }
        });
    }

    public void hide() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.INVISIBLE);
                viewGroup.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void show() {
        matchView.disableDrawButton();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundFadeView.setVisibility(View.VISIBLE);
                if (backgroundFadeView.getTop() < MINIMUM_HEIGHT) {
                    viewGroup.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                }
                viewGroup.setVisibility(View.VISIBLE);
            }
        });
    }
}
