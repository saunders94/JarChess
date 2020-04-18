package com.example.jarchess.online.datapackage;

import com.example.jarchess.match.result.ChessMatchResult;

public interface ChessMatchResultSender {
    void send(ChessMatchResult result);
}
