package com.example.jarchess;

import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;
import com.example.jarchess.match.styles.LeopardPrintAvatarStyle;
import com.example.jarchess.match.styles.MarbleChessboardStyle;
import com.example.jarchess.match.styles.NeonLetterChesspieceStyle;
import com.example.jarchess.testmode.TestMode;

public class JarAccount {
    private static JarAccount instance = null;
    private String name;
    private AvatarStyle avatarStyle;
    private ChessboardStyle boardStyle;
    private ChesspieceStyle pieceStyle;
    private boolean commitButtonClickIsRequired;
    private String signonToken;
    private MatchClockChoice preferedMatchClock;
    private JarAccount() {
        name = "Display Name";//FIXME needs to get this from account/preference file/database
        avatarStyle = LeopardPrintAvatarStyle.getInstance();//FIXME needs to get this from preference file/database
        boardStyle = MarbleChessboardStyle.getInstance();//FIXME needs to get this from preference file/database
        pieceStyle = NeonLetterChesspieceStyle.getInstance();//FIXME needs to get this from preference file/database
        commitButtonClickIsRequired = false;//FIXME needs to get this from preference file/database
        preferedMatchClock = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;//FIXME needs to get this from file/database
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

    public MatchClockChoice getPreferredMatchClock() {

        TestMode.turnOn();//TODO remove the test clock
        if (TestMode.isOn()) {
            return MatchClockChoice.TEST_MATCH_CLOCK;
        }
        return preferedMatchClock;
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

    public boolean isLoggedIn() {
        return false;//FIXME
    }
}
