package com.example.jarchess.jaraccount;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.jarchess.MainActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.styles.avatar.AvatarStyle;
import com.example.jarchess.match.styles.avatar.PlayerAvatarStyles;
import com.example.jarchess.match.styles.chessboard.ChessboardStyle;
import com.example.jarchess.match.styles.chessboard.ChessboardStyles;
import com.example.jarchess.match.styles.chesspiece.ChesspieceStyle;
import com.example.jarchess.match.styles.chesspiece.ChesspieceStyles;
import com.example.jarchess.online.usermanagement.Account;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.jarchess.match.clock.MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK;
import static com.example.jarchess.match.styles.avatar.PlayerAvatarStyles.LEOPARD_PRINT;
import static com.example.jarchess.match.styles.chessboard.ChessboardStyles.MARBLE_1;
import static com.example.jarchess.match.styles.chesspiece.ChesspieceStyles.NEON_LETTERS;

public class JarAccount {
    private static JarAccount instance = null;
    private static final String TAG = "JarAccount";
    private final JarAccountStringSetting name;
    private final JarAccountIntegerSetting avatarStyleInt;
    private final JarAccountIntegerSetting chessboardStyleInt;
    private final JarAccountIntegerSetting chesspieceStyleInt;
    private final JarAccountIntegerSetting matchClockChoiceInt;
    private final JarAccountBooleanSetting automaticQueening;
    private final JarAccountBooleanSetting disablePausing;
    private final JarAccountBooleanSetting requireCommitPress;
    private final JarAccountStringSetting signonToken;
    private final Set<JarAccountSetting> jarAccountSettings;
    private final Account accountIO = new Account();
    private SharedPreferences preferences;

    private JarAccount() {
        jarAccountSettings = new LinkedHashSet<>();

        name = new JarAccountStringSetting("name", "Display Name");
        jarAccountSettings.add(name);

        avatarStyleInt = new JarAccountIntegerSetting("avatarStyle", LEOPARD_PRINT.getIntValue());
        jarAccountSettings.add(avatarStyleInt);

        chessboardStyleInt = new JarAccountIntegerSetting("chessboardStyle", MARBLE_1.getIntValue());
        jarAccountSettings.add(chessboardStyleInt);

        chesspieceStyleInt = new JarAccountIntegerSetting("chesspieceStyle", NEON_LETTERS.getIntValue());
        jarAccountSettings.add(chesspieceStyleInt);

        matchClockChoiceInt = new JarAccountIntegerSetting("matchClockChoice", CLASSIC_FIDE_MATCH_CLOCK.getIntValue());
        jarAccountSettings.add(matchClockChoiceInt);

        automaticQueening = new JarAccountBooleanSetting("automaticQueening", false);
        jarAccountSettings.add(automaticQueening);

        disablePausing = new JarAccountBooleanSetting("disablePausing", false);
        jarAccountSettings.add(disablePausing);

        requireCommitPress = new JarAccountBooleanSetting("requireCommitPress", false);
        jarAccountSettings.add(requireCommitPress);

        signonToken = new JarAccountStringSetting("signonToken", "");
        jarAccountSettings.add(signonToken);

    }

    public synchronized static JarAccount getInstance() {
        if (instance == null) {
            instance = new JarAccount();
        }
        return instance;
    }

    public synchronized AvatarStyle getAvatarStyle() {
        return PlayerAvatarStyles.getFromInt(avatarStyleInt.getValue()).getAvatarStyle();
    }

    public synchronized void setAvatarStyle(PlayerAvatarStyles avatarStyle) {
        this.avatarStyleInt.setValue(avatarStyle.getIntValue());
        this.avatarStyleInt.saveToLocal(preferences);

        try {
            accountIO.setAvatar(getName(), getSignonToken(), avatarStyleInt.getValue());
        } catch (IOException e) {
            Log.e(TAG, "setAvatarStyle: ", e);
            // just continue
        }
    }

    public synchronized ChessboardStyle getBoardStyle() {
        return ChessboardStyles.getFromInt(chessboardStyleInt.getValue()).getChessboardStyle();
    }

    public synchronized void setBoardStyle(ChessboardStyles boardStyle) {
        this.chessboardStyleInt.setValue(boardStyle.getIntValue());
        this.chessboardStyleInt.saveToLocal(preferences);
    }

    public synchronized boolean getCommitButtonClickIsRequired() {
        return requireCommitPress.getValue();
    }

    public synchronized void setCommitButtonClickIsRequired(boolean commitButtonClickIsRequired) {
        this.requireCommitPress.setValue(commitButtonClickIsRequired);
        this.requireCommitPress.saveToLocal(preferences);
    }

    public synchronized String getName() {
        return name.getValue();
    }

    public synchronized void setName(String name) {
        this.name.setValue(name);
        this.name.saveToLocal(preferences);
    }

    public synchronized ChesspieceStyle getPieceStyle() {
        return ChesspieceStyles.getFromInt(chesspieceStyleInt.getValue()).getChesspieceStyle();
    }

    public synchronized void setPieceStyle(ChesspieceStyles pieceStyle) {

        this.chesspieceStyleInt.setValue(pieceStyle.getIntValue());
        this.chessboardStyleInt.saveToLocal(preferences);
    }

    public synchronized MatchClockChoice getPreferredMatchClock() {
        return MatchClockChoice.getFromIntValue(matchClockChoiceInt.getValue());
    }

    public synchronized void setPreferredMatchClock(MatchClockChoice preferredMatchClock) {
        this.matchClockChoiceInt.setValue(preferredMatchClock.getIntValue());
        matchClockChoiceInt.saveToLocal(preferences);
    }

    public synchronized String getSignonToken() {
        return signonToken.getValue();
    }

    public synchronized void setSignonToken(String signonToken) {
        this.signonToken.setValue(signonToken);
        this.signonToken.saveToLocal(preferences);
    }

    public synchronized boolean isAutomaticQueening() {
        return automaticQueening.getValue();
    }

    public synchronized void setAutomaticQueening(boolean automaticQueening) {
        this.automaticQueening.setValue(automaticQueening);
        this.automaticQueening.saveToLocal(preferences);
    }

    public synchronized boolean isCommitButtonClickIsRequired() {
        return requireCommitPress.getValue();
    }

    /**
     * checks to see if the jar account is considered logged in.
     *
     * @return true if the server verifies that the account is logged in (token and username matches
     * as expected) or if the server cannot be reached, otherwise false
     * @throws IOException if the interaction with the sever fails
     */
    public synchronized boolean isLoggedIn() {
        boolean result;
        try {
            result = verifyLogin();
        } catch (IOException e) {
            Log.e(TAG, "isLoggedIn: ", e);
        }
        result = !signonToken.getValue().isEmpty();

        Log.d(TAG, "isLoggedIn() returned: " + result);

        return result;
    }

    public synchronized boolean isPausingDisabled() {
        return disablePausing.getValue();
    }

    public synchronized void setPausingDisabled(boolean pausingDisabled) {
        this.disablePausing.setValue(pausingDisabled);
        this.disablePausing.saveToLocal(preferences);
    }

    public synchronized void loadFromLocal(MainActivity mainActivity) {
        preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);

        for (JarAccountSetting<?> jarAccountSetting : jarAccountSettings) {
            jarAccountSetting.loadFromLocal(preferences);
        }

    }

    public boolean logout() {
        try {
            return accountIO.signout(getName(), getSignonToken());
        } finally {
            //clear all settings.
            for (JarAccountSetting jarAccountSetting : jarAccountSettings) {
                //noinspection unchecked
                jarAccountSetting.clear(preferences);
            }
        }
    }

    /**
     * Verifies the loginToken
     *
     * @return true if the token can be verified as valid, false if the token can be verified as incorrect
     * @throws IOException If communication with the server fails
     */
    public synchronized boolean verifyLogin() throws IOException {
        return accountIO.signonTokenIsValid(getName(), getSignonToken());
    }
}
