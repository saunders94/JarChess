package com.example.jarchess.match.datapackage;

public interface DatapackageReceiver {

    Datapackage recieveNextDatapackage() throws InterruptedException;

}
