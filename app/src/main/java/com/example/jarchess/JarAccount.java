package com.example.jarchess;

import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.styles.avatar.LeopardPrintAvatarStyle;
import com.example.jarchess.match.styles.chessboard.ChessboardStyle;
import com.example.jarchess.match.styles.chessboard.MarbleChessboardStyle;
import com.example.jarchess.match.styles.chesspiece.ChesspieceStyle;
import com.example.jarchess.match.styles.chesspiece.NeonLetterChesspieceStyle;

import java.io.IOException;

public class JarAccount {
    private static JarAccount instance = null;
    private String name;
    private AvatarStyle avatarStyle;
    private ChessboardStyle boardStyle;
    private ChesspieceStyle pieceStyle;
    private boolean commitButtonClickIsRequired;
    private boolean automaticQueening;
    private boolean pausingDisabled;
    private String signonToken;
    private MatchClockChoice preferredMatchClock;
    private JarAccount() {
        name = "Display Name";//FIXME needs to get this from account/preference file/database
        avatarStyle = LeopardPrintAvatarStyle.getInstance();//FIXME needs to get this from preference file/database
        boardStyle = MarbleChessboardStyle.getInstance();//FIXME needs to get this from preference file/database
        pieceStyle = NeonLetterChesspieceStyle.getInstance();//FIXME needs to get this from preference file/database
        commitButtonClickIsRequired = false;//FIXME needs to get this from preference file/database
        preferredMatchClock = MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;//FIXME needs to get this from file/database
        automaticQueening = false; //FIXME needs to get this from file/database
        pausingDisabled = false; //FIXME needs to get this from file/database
    }

    public MatchClockChoice getPreferredMatchClock() {

//        TestMode.turnOn();//TODO remove the test clock
//        if (TestMode.isOn()) {
//            return MatchClockChoice.TEST_MATCH_CLOCK;
//        }
        return preferredMatchClock;
    }

    public void setPreferredMatchClock(MatchClockChoice preferredMatchClock) {
        this.preferredMatchClock = preferredMatchClock;
    }

    public boolean isAutomaticQueening() {
        return automaticQueening;
    }

    public void setAutomaticQueening(boolean automaticQueening) {
        this.automaticQueening = automaticQueening;
    }

    public boolean isPausingDisabled() {
        return pausingDisabled;
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

    public void setPausingDisabled(boolean pausingDisabled) {
        this.pausingDisabled = pausingDisabled;
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

    /**
     * checks to see if the jar account is considered logged in.
     *
     * @return true if the server verifies that the account is logged in (token and username matches as expected), otherwise false
     * @throws IOException if the interaction with the sever fails
     */
    public boolean isLoggedIn() throws IOException {
        return signonToken != null;//FIXME
    }

    /**
     * checks to see if the Account is Online (not necessarily logged in).
     *
     * @return true if the server can be reached, otherwise false.
     */
    public boolean isOnline() {
        return false;//FIXME
    }
}
