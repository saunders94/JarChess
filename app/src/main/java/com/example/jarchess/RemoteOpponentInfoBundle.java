package com.example.jarchess;

import com.example.jarchess.match.styles.AvatarStyle;

public class RemoteOpponentInfoBundle {
    private final String name;
    private final AvatarStyle avatarStyle;
    private final String color;
    private final String ip;
    private final int port;

    public RemoteOpponentInfoBundle(String name, AvatarStyle avatarStyle, String color, String ip, int port) {
        this.name = name;
        this.avatarStyle = avatarStyle;
        this.color = color;
        //TODO Remove ip and port info
        this.ip = ip;
        this.port = port;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getIP(){ return ip;}

    public int getPort() { return port;}
}
