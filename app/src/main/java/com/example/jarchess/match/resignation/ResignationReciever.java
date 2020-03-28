package com.example.jarchess.match.resignation;

import com.example.jarchess.match.result.ResignationResult;

public interface ResignationReciever {
    ResignationResult recieveNextResignation() throws InterruptedException;
}
