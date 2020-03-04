package com.example.jarchess.match.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

/**
 * A match activity is an activity where two participants play a chess match with each other.
 */
public class MatchActivity extends AppCompatActivity implements LocalParticipantController, SquareClickListener {


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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void observeSquareClick(Coordinate coordinateClicked) {
        // log the click
        log("clicked " + coordinateClicked);


        Piece piece = match.getPieceAt(coordinateClicked);
        if (waitingForMove != null) {
            // Than one of the participants is waiting for a move.

            if (piece != null && piece.getColor() == waitingForMove && origin != coordinateClicked) {
                // If the square has a piece and it is the color of the waiting participant
                // and it is not already the current origin,
                //
                // than we assume that the click indicates that the user intends to set that square as the new origin of the move

                // set the origin
                setOrigin(coordinateClicked);

                // clear any garbage destination.
                clearDestination();


            } else if (origin != null) {
                // If the origin was already set
                // and the square was empty or had a piece that was a different color than the participant waiting for a move to be input,
                //
                // than we assume that the click indicates that the user intends to set that square as the destination of the move.

                //TODO add some stuff to limit the destination to valid destinations
                setDestination(coordinateClicked);

            }
        }

    }

    /**
     * clears the destination
     */
    private void clearDestination() {
        setDestination(null);
    }

    /**
     * Sets the origin.
     *
     * @param origin the origin coordinate to set to, may be null
     */
    private void setOrigin(@Nullable Coordinate origin) {

        this.origin = origin;
        log("set origin to " + this.origin);

    }

    /**
     * Sets the destination.
     *
     * @param destination the destination coordinate to set to, may be null
     */
    private void setDestination(@Nullable Coordinate destination) {

        this.destination = destination;
        log("set destination to " + this.destination);

    }

    /**
     * Logs a message as a debug message.
     *
     * @param msg the message to log
     */
    private void log(String msg) {
        Log.d("MatchActivity", msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void requestMove(ChessColor color) {
        log("move was requested by " + color.toString());
        waitingForMove = color;
    }
}
