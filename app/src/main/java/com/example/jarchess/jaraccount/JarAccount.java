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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.jarchess.jaraccount.JarAccountSetting.Flag.DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER;
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
    private final JarAccountIntegerSetting level;
    private final Set<JarAccountSetting> jarAccountSettings;
    private final JarAccountStringSetting passwordHash;
    private final Account accountIO = new Account();
    private SharedPreferences preferences;

    private JarAccount() {
        jarAccountSettings = new LinkedHashSet<>();

        name = new JarAccountStringSetting("name", "Display Name", DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER);
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

        signonToken = new JarAccountStringSetting("signonToken", "", DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER);
        jarAccountSettings.add(signonToken);

        passwordHash = new JarAccountStringSetting("passwordHash", "", DO_NOT_SAVE_TO_OR_LOAD_FROM_SERVER);
        jarAccountSettings.add(passwordHash);

        level = new JarAccountIntegerSetting("level", 0);
        jarAccountSettings.add(level);

    }

    public synchronized static JarAccount getInstance() {
        if (instance == null) {
            instance = new JarAccount();
        }
        return instance;
    }

    public int getLevel() {
        return level.getValue();
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

    public JSONObject getJsonForLogout(JSONObject signoutJson) {
        final String username = getName();
        final String token = getSignonToken();


        //save to server and clear all settings from local storage.
        for (JarAccountSetting jarAccountSetting : jarAccountSettings) {
            try {
                jarAccountSetting.saveToJson(signoutJson, accountIO, username, token);

            } catch (JSONException e) {
                Log.e(TAG, "logout: ", e);
                // continue
            }

            //noinspection unchecked
            jarAccountSetting.clear(preferences);
        }


        return signoutJson;
    }

    public synchronized void setAutomaticQueening(boolean automaticQueening) {
        JarAccountSetting<Boolean> jas = this.automaticQueening;
        jas.setValue(automaticQueening);
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
    }

    public synchronized boolean getCommitButtonClickIsRequired() {
        return requireCommitPress.getValue();
    }

    private String getPasswordHash() {
        return passwordHash.getValue();
    }

    public synchronized String getName() {
        return name.getValue();
    }

    public synchronized void setAvatarStyle(PlayerAvatarStyles avatarStyle) {
        JarAccountSetting<Integer> jas = this.avatarStyleInt;
        jas.setValue(avatarStyle.getIntValue());
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
    }

    public synchronized ChesspieceStyle getPieceStyle() {
        return ChesspieceStyles.getFromInt(chesspieceStyleInt.getValue()).getChesspieceStyle();
    }

    public synchronized void setBoardStyle(ChessboardStyles boardStyle) {
        JarAccountSetting<Integer> jas = this.chessboardStyleInt;
        jas.setValue(boardStyle.getIntValue());
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
    }

    public synchronized MatchClockChoice getPreferredMatchClock() {
        return MatchClockChoice.getFromIntValue(matchClockChoiceInt.getValue());
    }

    public synchronized void setCommitButtonClickIsRequired(boolean commitButtonClickIsRequired) {
        JarAccountSetting<Boolean> jas = this.requireCommitPress;
        jas.setValue(commitButtonClickIsRequired);
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
    }

    public synchronized String getSignonToken() {
        return signonToken.getValue();
    }

    public synchronized void setName(String name) {
        JarAccountSetting<String> jas = this.name;
        jas.setValue(name);
        jas.saveToLocal(preferences);
    }

    public synchronized boolean isAutomaticQueening() {
        return automaticQueening.getValue();
    }

    public synchronized void setPausingDisabled(boolean pausingDisabled) {
        JarAccountSetting<Boolean> jas = this.disablePausing;
        jas.setValue(pausingDisabled);
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
    }

    public synchronized void setPieceStyle(ChesspieceStyles pieceStyle) {
        JarAccountSetting<Integer> jas = this.chesspieceStyleInt;
        jas.setValue(pieceStyle.getIntValue());
        jas.saveToLocal(preferences);

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
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
        //TODO uncomment when we get verifyLogin working
//        try {
//            result = verifyLogin();
//        } catch (IOException e) {
//            Log.e(TAG, "isLoggedIn: ", e);
//        }
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

//        try {
//            jas.saveToServer(accountIO, getName(), getSignonToken());
//        } catch (IOException e) {
//            // continue execution
//        }
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

    private void setPasswordHash(String passwordHash) {
        this.passwordHash.setValue(passwordHash);
    }

    public void loadAccountFromJson(JSONObject jsonObject) {
        for (JarAccountSetting jas : jarAccountSettings) {
            try {
                jas.loadFromJson(jsonObject, accountIO, getName(), getSignonToken());
            } catch (JSONException e) {
                Log.e(TAG, "loadAccountFromJson: ", e);
                //continue
            }
        }
    }

    public boolean signIn(String username, String password) {
        return accountIO.signin(username, password);
    }

    public boolean signOut() {
        return accountIO.signout(getName(), getSignonToken());
    }
//
//    /**
//     * Verifies the loginToken
//     *
//     * @return true if the token can be verified as valid, false if the token can be verified as incorrect
//     * @throws IOException If communication with the server fails
//     */
//    public synchronized boolean verifyLogin() throws IOException {
//        return accountIO.verifySignin(getName(), getPasswordHash());
//    }
}
