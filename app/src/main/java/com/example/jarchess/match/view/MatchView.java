package com.example.jarchess.match.view;

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
import com.example.jarchess.match.activity.CommitButtonClickHandler;
import com.example.jarchess.match.activity.MatchActivity;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.result.Result;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

public class MatchView extends View {
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
    private final CommitButtonClickHandler commitButtonClickHandler;
    private final CapturedPiecesView capturedPieceView;
    private final MatchResultDialog matchResultDialog;
    private final PawnPromotionChoiceDialog pawnPromotionChoiceDialog;

    public MatchView(Match match, MatchActivity activity) {
        super(activity.getBaseContext());
        commitButtonClickHandler = activity;

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
                commitButtonClickHandler.handleCommitButtonClick();
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


    }

    public void clearOriginSelectionIndicator(@NonNull Coordinate coordinate) {
        chessboardView.clearOriginSelectionIndicator(coordinate);
    }

    public void setOriginSelectionIndicator(@NonNull Coordinate coordinate) {
        chessboardView.setOriginSelectionIndicator(coordinate);
    }

    public void clearPossibleDestinationIndicator(@NonNull Coordinate coordinate) {
        chessboardView.clearPossibleDestinationIndicator(coordinate);
    }

    public void setPossibleDestinationIndicator(@NonNull Coordinate coordinate) {
        chessboardView.setPossibleDestinationIndicator(coordinate);
    }

    public void clearDestinationSelectionIndicator(@NonNull Coordinate coordinate) {
        chessboardView.clearDestinationSelectionIndicator(coordinate);
    }

    public void setDestinationSelectionIndicator(@NonNull Coordinate coordinate) {
        chessboardView.setDestinationSelectionIndicator(coordinate);
    }

    public void updatePiece(@NonNull Coordinate coordinate) {
        chessboardView.updatePiece(coordinate);
    }

    public void addCapturedPiece(Piece capturedPiece) {
        capturedPieceView.add(capturedPiece);
    }

    public void showMatchResultDialog(Result matchResult) {
        matchResultDialog.show(matchResult);
    }

    public void showLeaveMatchDialog() {
        leaveMatchDialog.show();
    }

    public void showPawnPromotionChoiceDialog() {
        pawnPromotionChoiceDialog.show();
    }

}
