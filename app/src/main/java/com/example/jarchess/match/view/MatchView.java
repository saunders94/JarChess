package com.example.jarchess.match.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jarchess.JarAccount;
import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.PlayerMatch;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.events.ClockTickEvent;
import com.example.jarchess.match.events.ClockTickEventListener;
import com.example.jarchess.match.events.ClockTickEventManager;
import com.example.jarchess.match.move.Move;
import com.example.jarchess.match.move.PieceMovement;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.result.Result;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;
import com.example.jarchess.match.turn.Turn;

import java.util.Collection;
import java.util.Locale;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

public class MatchView extends View implements ClockTickEventListener {
    private final MatchParticipant leftParticipant;
    private final MatchParticipant rightParticipant;
    private final View participantInfoBarView;
    private final ChessboardView chessboardView;
    private final View rightParticipantInfoView;
    private final View leftParticipantInfoView;
    private final TextView rightParticipantNameTextView;
    private final TextView leftParticipantNameTextView;
    private final ImageView rightParticipantAvatarImageView;
    private final ImageView leftParticipantAvatarImageView;
    private final TextView rightParticipantColorTextView;
    private final TextView rightParticipantTimeTextView;
    private final TextView leftParticipantColorTextView;
    private final TextView leftParticipantTimeTextView;
    private final int leftParticipantColor;
    private final int rightParticipantColor;
    private final int leftParticipantTextColor;
    private final int rightParticipantTextColor;
    private final Button commitButton;
    private final LeaveMatchDialog leaveMatchDialog;
    private final CommitButtonClickObserver commitButtonClickObserver;
    private final CapturedPiecesView capturedPieceView;
    private final MatchResultDialog matchResultDialog;
    private final PawnPromotionChoiceDialog pawnPromotionChoiceDialog;
    private final Activity activity;

    public MatchView(Match match, MatchActivity activity) {
        super(activity.getBaseContext());
        commitButtonClickObserver = activity;
        this.activity = activity;
        participantInfoBarView = activity.findViewById(R.id.participant_info_bar);
        leftParticipantInfoView = participantInfoBarView.findViewById(R.id.player_info);
        leftParticipantNameTextView = leftParticipantInfoView.findViewById(R.id.nameTextView);
        leftParticipantColorTextView = leftParticipantInfoView.findViewById(R.id.playerColorTextView);
        leftParticipantTimeTextView = leftParticipantInfoView.findViewById(R.id.timeTextView);
        leftParticipantAvatarImageView = leftParticipantInfoView.findViewById(R.id.avatarImageView);
        rightParticipantInfoView = participantInfoBarView.findViewById(R.id.opponent_info);
        rightParticipantNameTextView = rightParticipantInfoView.findViewById(R.id.nameTextView);
        rightParticipantColorTextView = rightParticipantInfoView.findViewById(R.id.playerColorTextView);
        rightParticipantTimeTextView = rightParticipantInfoView.findViewById(R.id.timeTextView);
        rightParticipantAvatarImageView = rightParticipantInfoView.findViewById(R.id.avatarImageView);
        commitButton = participantInfoBarView.findViewById(R.id.commitButton);
        leaveMatchDialog = new LeaveMatchDialog(activity);
        matchResultDialog = new MatchResultDialog(activity);
        pawnPromotionChoiceDialog = new PawnPromotionChoiceDialog(activity);

        commitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commitButtonClickObserver.observeCommitButtonClick();
            }
        });


        // update the view's style
        ChesspieceStyle chesspieceStyle = JarAccount.getInstance().getPieceStyle();
        ChessboardStyle chessboardStyle = JarAccount.getInstance().getBoardStyle();

        if (match instanceof PlayerMatch) {

            PlayerMatch playerMatch = (PlayerMatch) match;
            leftParticipant = playerMatch.getPlayer();
            chessboardView = new ChessboardView(
                    activity.findViewById(R.id.chessboard),
                    chesspieceStyle,
                    chessboardStyle,
                    leftParticipant.getColor(),
                    match,
                    activity);
        } else {
            leftParticipant = match.getBlackPlayer();
            chessboardView = new ChessboardView(
                    activity.findViewById(R.id.chessboard),
                    chesspieceStyle,
                    chessboardStyle,
                    WHITE,
                    match,
                    activity);

        }

        capturedPieceView = new CapturedPiecesView(activity, chesspieceStyle, leftParticipant.getColor());

        rightParticipant = leftParticipant.equals(match.getBlackPlayer()) ? match.getWhitePlayer() : match.getBlackPlayer();
        leftParticipantColor = leftParticipant.getColor() == BLACK ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;
        rightParticipantColor = rightParticipant.getColor() == BLACK ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;
        leftParticipantTextColor = leftParticipant.getColor() == ChessColor.WHITE ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;
        rightParticipantTextColor = rightParticipant.getColor() == ChessColor.WHITE ? android.graphics.Color.BLACK : android.graphics.Color.WHITE;

        leftParticipantNameTextView.setText(leftParticipant.getName());
        leftParticipantNameTextView.setTextColor(leftParticipantTextColor);
        leftParticipantTimeTextView.setTextColor(leftParticipantTextColor);
        leftParticipantColorTextView.setTextColor(leftParticipantTextColor);
        String leftColorText = leftParticipant.getColor().toString() + " PLAYER";
        leftParticipantColorTextView.setText(leftColorText);
        leftParticipantAvatarImageView.setImageResource(leftParticipant.getAvatarStyle().getAvatarResourceID());
        leftParticipantInfoView.setBackgroundColor(leftParticipantColor);

        rightParticipantNameTextView.setText(rightParticipant.getName());
        rightParticipantNameTextView.setTextColor(rightParticipantTextColor);
        rightParticipantTimeTextView.setTextColor(rightParticipantTextColor);
        rightParticipantColorTextView.setTextColor(rightParticipantTextColor);
        String rightColorText = rightParticipant.getColor().toString() + " PLAYER";
        rightParticipantColorTextView.setText(rightColorText);
        rightParticipantAvatarImageView.setImageResource(rightParticipant.getAvatarStyle().getAvatarResourceID());
        rightParticipantInfoView.setBackgroundColor(rightParticipantColor);

        ClockTickEventManager.getInstance().add(this);
    }

    public void addCapturedPiece(Piece capturedPiece) {
        capturedPieceView.add(capturedPiece);
    }

    public void clearDestinationSelectionIndicator(Coordinate coordinate) {
        if (coordinate == null) {
            return;
        }
        chessboardView.clearDestinationSelectionIndicator(coordinate);
    }

    public void clearOriginSelectionIndicator(Coordinate coordinate) {
        if(coordinate == null){
            return;
        }
        chessboardView.clearOriginSelectionIndicator(coordinate);
    }

    public void clearPossibleDestinationIndicators(Collection<Coordinate> coordinates) {
        if(coordinates == null || coordinates.isEmpty()){
            return;
        }
        chessboardView.clearPossibleDestinationIndicator(coordinates);
    }

    public void clearPromotionIndicator(PieceMovement movement) {
        if(movement == null || movement.getDestination() == null){
            return;
        }
        chessboardView.clearDestinationSelectionIndicator(movement.getDestination());
    }

    @Override
    public void observe(ClockTickEvent clockTickEvent) {
        updateParticipantTime(leftParticipantTimeTextView, clockTickEvent.getDisplayedTimeMillis(leftParticipant.getColor()));
        updateParticipantTime(rightParticipantTimeTextView, clockTickEvent.getDisplayedTimeMillis(rightParticipant.getColor()));
    }

    public void setDestinationSelectionIndicator(@NonNull Coordinate coordinate) {
        chessboardView.setDestinationSelectionIndicator(coordinate);
    }

    public void setPromotionIndicator(Coordinate coordinate) {
        chessboardView.setPromotionIndicator(coordinate);
    }

    public void showLeaveMatchDialog() {
        leaveMatchDialog.show();
    }

    public void showMatchResultDialog(Result matchResult) {
        matchResultDialog.show(matchResult);
    }

    public void showPawnPromotionChoiceDialog() {
        pawnPromotionChoiceDialog.show();
    }

    public void updateAfterSettingOrigin(Coordinate origin) {

        chessboardView.setOriginSelectionIndicator(origin);
    }

    public void updateAfterSettingPossibleDestinations(Collection<Coordinate> possibleDestinations) {
        //TODO optimize
        for (Coordinate possibleDestination : possibleDestinations) {
            chessboardView.setPossibleDestinationIndicator(possibleDestination);
        }
    }

    public void updatePiece(@NonNull Coordinate coordinate) {
        chessboardView.updatePiece(coordinate);
    }

    private void updateParticipantTime(final TextView participantTimeTextView, final long displayedTimeMillis) {
        long seconds = displayedTimeMillis / 1000;
        final long minutes = seconds / 60;
        final long secondsRemaining = seconds % 60;

        final String secondsRemainingString = String.format(Locale.US, "%02d", secondsRemaining);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                participantTimeTextView.setText(minutes + ":" + secondsRemainingString);
            }
        });


    }

//TODO remove

//    public void updateTimes(MatchClock matchClock) {
//        updateParticipantTime(leftParticipantTimeTextView, matchClock.getDisplayedTimeMillis(leftParticipant.getColor()));
//        updateParticipantTime(rightParticipantTimeTextView, matchClock.getDisplayedTimeMillis(rightParticipant.getColor()));
//
//    }

    public void updateViewAfter(Turn turn) {
        Move move = turn.getMove();
        updateViewAfter(move);
    }

    public void updateViewAfter(Move move) {
        for (PieceMovement movement : move) {
            updateViewAfter(movement);
        }
    }

    public void updateViewAfter(PieceMovement movement) {
        updatePiece(movement.getOrigin());
        updatePiece(movement.getDestination());
    }



    public void updateViewBefore(Turn turn) {
        Move move = turn.getMove();
        updateViewBefore(move);
    }

    public void updateViewBefore(Move move) {
        for (PieceMovement movement : move) {
            updateViewBefore(movement);
        }
    }

    public void updateViewBefore(PieceMovement movement) {
        chessboardView.previewMovement(movement);
    }
}