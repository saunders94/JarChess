package com.example.jarchess.online.datapackage;

public interface DatapackageReceiver {

    Datapackage recieveNextDatapackage() throws InterruptedException;

}
