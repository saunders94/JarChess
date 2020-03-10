package com.example.jarchess.match.resignation;

public interface ResignationReciever {
    Resignation recieveNextResignation() throws InterruptedException;
}
