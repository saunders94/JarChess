package com.example.jarchess.online.datapackage;

public interface DatapackageReceiver {

    UnsignedDatapackage recieveNextDatapackage() throws InterruptedException;

}
