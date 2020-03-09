package com.example.jarchess;

import com.example.jarchess.match.styles.AvatarStyle;

public class RemoteOpponentAccount {
    private String name;
    private AvatarStyle avatarStyle;

    public RemoteOpponentAccount(String name, AvatarStyle avatarStyle) {
        this.name = name;
        this.avatarStyle = avatarStyle;
    }

    public String getName() {
        return name;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }
}
