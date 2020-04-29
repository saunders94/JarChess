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

        signonToken = new JarAccountStringSetting("signonToken", "", JarAccountSetting.Flag.DO_NOT_SAVE_TO_SERVER);
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

    public synchronized boolean isCommitButtonClickIsRequired() {
        return getCommitButtonClickIsRequired();
    }

    public synchronized ChessboardStyle getBoardStyle() {
        return ChessboardStyles.getFromInt(chessboardStyleInt.getValue()).getChessboardStyle();
    }

    public synchronized void setCommitButtonClickIsRequired(boolean commitButtonClickIsRequired) {
        JarAccountSetting<Boolean> jas = this.requireCommitPress;
        jas.setValue(commitButtonClickIsRequired);
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized boolean getCommitButtonClickIsRequired() {
        return requireCommitPress.getValue();
    }

    public boolean logout() {
        final String username = getName();
        final String token = getSignonToken();


        //clear all settings.
        for (JarAccountSetting jarAccountSetting : jarAccountSettings) {

            try {
                jarAccountSetting.saveToServer(accountIO, username, token);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //noinspection unchecked
            jarAccountSetting.clear(preferences);
        }
        return accountIO.signout(username, token);

    }

    public synchronized String getName() {
        return name.getValue();
    }

    public synchronized void setAutomaticQueening(boolean automaticQueening) {
        JarAccountSetting<Boolean> jas = this.automaticQueening;
        jas.setValue(automaticQueening);
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized ChesspieceStyle getPieceStyle() {
        return ChesspieceStyles.getFromInt(chesspieceStyleInt.getValue()).getChesspieceStyle();
    }

    public synchronized void setAvatarStyle(PlayerAvatarStyles avatarStyle) {
        JarAccountSetting<Integer> jas = this.avatarStyleInt;
        jas.setValue(avatarStyle.getIntValue());
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized MatchClockChoice getPreferredMatchClock() {
        return MatchClockChoice.getFromIntValue(matchClockChoiceInt.getValue());
    }

    public synchronized void setBoardStyle(ChessboardStyles boardStyle) {
        JarAccountSetting<Integer> jas = this.chessboardStyleInt;
        jas.setValue(boardStyle.getIntValue());
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized String getSignonToken() {
        return signonToken.getValue();
    }

    public synchronized void setName(String name) {
        JarAccountSetting<String> jas = this.name;
        jas.setValue(name);
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized boolean isAutomaticQueening() {
        return automaticQueening.getValue();
    }

    public synchronized void setPausingDisabled(boolean pausingDisabled) {
        JarAccountSetting<Boolean> jas = this.disablePausing;
        jas.setValue(pausingDisabled);
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized void setPieceStyle(ChesspieceStyles pieceStyle) {
        JarAccountSetting<Integer> jas = this.chesspieceStyleInt;
        jas.setValue(pieceStyle.getIntValue());
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
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

    public synchronized void setPreferredMatchClock(MatchClockChoice preferredMatchClock) {
        JarAccountSetting<Integer> jas = this.matchClockChoiceInt;
        jas.setValue(preferredMatchClock.getIntValue());
        jas.saveToLocal(preferences);

        try {
            jas.saveToServer(accountIO, getName(), getSignonToken());
        } catch (IOException e) {
            // continue execution
        }
    }

    public synchronized void loadFromLocal(MainActivity mainActivity) {
        preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);

        for (JarAccountSetting<?> jarAccountSetting : jarAccountSettings) {
            jarAccountSetting.loadFromLocal(preferences);
        }

    }

    public synchronized void setSignonToken(String signonToken) {
        JarAccountSetting<String> jas = this.signonToken;
        jas.setValue(signonToken);
        jas.saveToLocal(preferences);
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
