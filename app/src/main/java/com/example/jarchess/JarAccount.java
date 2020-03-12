package com.example.jarchess;

import com.example.jarchess.match.styles.AvatarStyle;
import com.example.jarchess.match.styles.ChessboardStyle;
import com.example.jarchess.match.styles.ChesspieceStyle;
import com.example.jarchess.match.styles.LeopardPrintAvatarStyle;
import com.example.jarchess.match.styles.MarbleChessboardStyle;
import com.example.jarchess.match.styles.NeonLetterChesspieceStyle;

public class JarAccount {
    private static JarAccount instance = null;
    private String name;
    private AvatarStyle avatarStyle;
    private ChessboardStyle boardStyle;
    private ChesspieceStyle pieceStyle;
    private boolean commitButtonClickIsRequired;

    private JarAccount() {
        name = "Display Name";//FIXME needs to get this from account/preference file/database
        avatarStyle = LeopardPrintAvatarStyle.getInstance();//FIXME needs to get this from preference file/database
        boardStyle = MarbleChessboardStyle.getInstance();//FIXME needs to get this from preference file/database
        pieceStyle = NeonLetterChesspieceStyle.getInstance();//FIXME needs to get this from preference file/database
        commitButtonClickIsRequired = false;//FIXME needs to get this from preference file/database
    }

    public static JarAccount getInstance() {
        if (instance == null) {
            instance = new JarAccount();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public ChessboardStyle getBoardStyle() {
        return boardStyle;
    }

    public ChesspieceStyle getPieceStyle() {
        return pieceStyle;
    }

    public boolean getCommitButtonClickIsRequired() {
        return commitButtonClickIsRequired;
    }
}
