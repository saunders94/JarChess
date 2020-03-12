package com.example.jarchess.match.activity;

import com.example.jarchess.RemoteOpponentAccount;
import com.example.jarchess.match.Match;
import com.example.jarchess.match.MatchStarter;
import com.example.jarchess.match.styles.YellowBlackYellowCircleAvatarStyle;

public class OnlineMultiplayerMatchActivity extends MatchActivity {
    @Override
    public Match createMatch() {
        RemoteOpponentAccount remoteOpponentAccount = new RemoteOpponentAccount("Remote Opponent", YellowBlackYellowCircleAvatarStyle.getInstance());

        return MatchStarter.getInstance().startRemoteMultiplayerMatch();
    }
}
