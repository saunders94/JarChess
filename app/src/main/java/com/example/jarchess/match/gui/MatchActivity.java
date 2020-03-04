package com.example.jarchess.match.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jarchess.JarAccount;
import com.example.jarchess.R;
import com.example.jarchess.match.ChessColor;
import com.example.jarchess.match.Coordinate;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchMaker;
import com.example.jarchess.match.PlayerMatch;
import com.example.jarchess.match.participant.LocalParticipantController;
import com.example.jarchess.match.participant.MatchParticipant;
import com.example.jarchess.match.pieces.Piece;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;

import static com.example.jarchess.match.ChessColor.BLACK;
import static com.example.jarchess.match.ChessColor.WHITE;

public class MatchActivity extends AppCompatActivity implements LocalParticipantController, SquareClickHandler {


    private Match match;
    private MatchParticipant leftParticipant;
    private MatchParticipant rightParticipant;
    private int leftParticipantColor;
    private int rightParticipantColor;
    private int leftParticipantTextColor;
    private int rightParticipantTextColor;

    private View participantInfoBarView;
    private ChessboardView chessboardView;
    private View rightParticipantInfoView;
    private View leftParticipantInfoView;
    private TextView rightParticipantNameTextView;
    private TextView leftParticipantNameTextView;
    private ImageView rightParticipantAvatarImageView;
    private ImageView leftParticipantAvatarImageView;
    private TextView rightParticipantColorTextView;
    private TextView rightParticipantTimeTextView;
    private TextView leftParticipantColorTextView;
    private TextView leftParticipantTimeTextView;

    private Coordinate origin;
    private Coordinate destination;

    // these are volatile, but need more robust synchronization
    private ChessColor waitingForMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MatchActivity", "created");
        setContentView(R.layout.activity_match);
        participantInfoBarView = findViewById(R.id.participant_info_bar);
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

        // set match to the current match
        match = MatchMaker.getInstance().getCurrentMatch();
        match.setLocalParticipantController(this);

        // update the view's style
        ChesspieceStyle chesspieceStyle = JarAccount.getInstance().getPieceStyle();
        ChessboardStyle chessboardStyle = JarAccount.getInstance().getBoardStyle();


        if (match instanceof PlayerMatch) {

            PlayerMatch playerMatch = (PlayerMatch) match;
            leftParticipant = playerMatch.getPlayer();
            chessboardView = new ChessboardView(
                    findViewById(R.id.chessboard),
                    chesspieceStyle,
                    chessboardStyle,
                    leftParticipant.getColor(),
                    match,
                    this);
        } else {
            leftParticipant = match.getBlackPlayer();
            chessboardView = new ChessboardView(
                    findViewById(R.id.chessboard),
                    chesspieceStyle,
                    chessboardStyle,
                    WHITE,
                    match,
                    this);

        }
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
//        leftParticipantNameTextView.setBackgroundColor(leftParticipantColor);
        leftParticipantAvatarImageView.setImageResource(leftParticipant.getAvatarStyle().getAvatarResourceID());
        leftParticipantInfoView.setBackgroundColor(leftParticipantColor);

        rightParticipantNameTextView.setText(rightParticipant.getName());
        rightParticipantNameTextView.setTextColor(rightParticipantTextColor);
        rightParticipantTimeTextView.setTextColor(rightParticipantTextColor);
        rightParticipantColorTextView.setTextColor(rightParticipantTextColor);
        String rightColorText = rightParticipant.getColor().toString() + " PLAYER";
        rightParticipantColorTextView.setText(rightColorText);
//        rightParticipantNameTextView.setBackgroundColor(rightParticipantColor);
        rightParticipantAvatarImageView.setImageResource(rightParticipant.getAvatarStyle().getAvatarResourceID());
        rightParticipantInfoView.setBackgroundColor(rightParticipantColor);

    }

    @Override
    public synchronized void handleSquareClick(Coordinate coordinateClicked) {
        Log.d("MatchActivity", "clicked " + coordinateClicked.toString());
        Piece piece = match.getPieceAt(coordinateClicked);
        if (waitingForMove != null) {
            if (piece.getColor() == waitingForMove) {
                origin = coordinateClicked;
            } else {
                destination = coordinateClicked;
            }
        }

    }

    @Override
    public synchronized void requestMove(ChessColor color) {
        waitingForMove = color;
    }
}
