package com.example.jarchess;

import com.example.jarchess.match.styles.AvatarStyle;

public class RemoteOpponentInfoBundle {
    private final String name;
    private final AvatarStyle avatarStyle;
    private final String ip;
    private final int port;

    public RemoteOpponentInfoBundle(String name, AvatarStyle avatarStyle, String ip, int port) {
        this.name = name;
        this.avatarStyle = avatarStyle;
        this.ip = ip;
        this.port = port;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
