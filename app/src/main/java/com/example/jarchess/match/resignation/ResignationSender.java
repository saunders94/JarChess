package com.example.jarchess.match.resignation;

import com.example.jarchess.match.result.ResignationResult;

public interface ResignationSender {
    void send(ResignationResult resignationResult);
}
