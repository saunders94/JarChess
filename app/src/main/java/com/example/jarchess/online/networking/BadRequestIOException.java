package com.example.jarchess.online.networking;

import java.io.IOException;

class BadRequestIOException extends IOException {

    public static final String BAD_REQUEST = "ERROR - BAD REQUEST";

    public BadRequestIOException() {
        super("server sent \"" + BAD_REQUEST + "\"");
    }
}
