package com.example.jarchess;

import com.example.jarchess.match.ChessColor;
import com.example.jarchess.jaraccount.styles.avatar.AvatarStyle;

public class RemoteOpponentInfoBundle {
    private final String name;
    private final AvatarStyle avatarStyle;
    private final ChessColor color;
    private final String ip;
    private final int port;

    public RemoteOpponentInfoBundle(String name, AvatarStyle avatarStyle, String color, String ip, int port) {
        this.name = name;
        this.avatarStyle = avatarStyle;
        this.color = ChessColor.valueOf(color.toUpperCase());
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

    public ChessColor getColor() {
        return color;
    }

    public String getIP(){ return ip;}

    public int getPort() { return port;}
}
