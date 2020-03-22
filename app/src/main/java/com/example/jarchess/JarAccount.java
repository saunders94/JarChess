package com.example.jarchess;

import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;
import com.example.jarchess.match.styles.LeopardPrintAvatarStyle;
import com.example.jarchess.match.styles.MarbleChessboardStyle;
import com.example.jarchess.match.styles.NeonLetterChesspieceStyle;

import static com.example.jarchess.match.clock.MatchClockChoice.CASUAL_MATCH_CLOCK;

public class JarAccount {
    private static JarAccount instance = null;
    private String name;
    private AvatarStyle avatarStyle;
    private ChessboardStyle boardStyle;
    private ChesspieceStyle pieceStyle;
    private boolean commitButtonClickIsRequired;
    private String signonToken;

    public static void setInstance(JarAccount instance) {
        JarAccount.instance = instance;
    }

    private JarAccount() {
        name = "Display Name";//FIXME needs to getDisplayedTimeMillis this from account/preference file/database
        avatarStyle = LeopardPrintAvatarStyle.getInstance();//FIXME needs to getDisplayedTimeMillis this from preference file/database
        boardStyle = MarbleChessboardStyle.getInstance();//FIXME needs to getDisplayedTimeMillis this from preference file/database
        pieceStyle = NeonLetterChesspieceStyle.getInstance();//FIXME needs to getDisplayedTimeMillis this from preference file/database
        commitButtonClickIsRequired = false;//FIXME needs to getDisplayedTimeMillis this from preference file/database
    }

    public String getSignonToken() {
        return signonToken;
    }

    public void setSignonToken(String signonToken) {
        this.signonToken = signonToken;
    }

    public boolean isCommitButtonClickIsRequired() {
        return commitButtonClickIsRequired;
    }

    public void setCommitButtonClickIsRequired(boolean commitButtonClickIsRequired) {
        this.commitButtonClickIsRequired = commitButtonClickIsRequired;
    }

    public void setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
    }

    public void setBoardStyle(ChessboardStyle boardStyle) {
        this.boardStyle = boardStyle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPieceStyle(ChesspieceStyle pieceStyle) {
        this.pieceStyle = pieceStyle;
    }

    public MatchClockChoice getPreferedMatchClock() {
        return CASUAL_MATCH_CLOCK;
    }

    public static JarAccount getInstance() {
        if (instance == null) {
            instance = new JarAccount();
        }
        return instance;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public ChessboardStyle getBoardStyle() {
        return boardStyle;
    }

    public boolean getCommitButtonClickIsRequired() {
        return commitButtonClickIsRequired;
    }

    public String getName() {
        return name;
    }

    public ChesspieceStyle getPieceStyle() {
        return pieceStyle;
    }
}
