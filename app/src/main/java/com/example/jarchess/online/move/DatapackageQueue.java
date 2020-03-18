package com.example.jarchess.online.move;

import android.util.Log;

import com.example.jarchess.online.datapackage.UnsignedDatapackage;

import java.util.concurrent.LinkedBlockingQueue;

public class DatapackageQueue {
    private LinkedBlockingQueue<UnsignedDatapackage> inboundUnsignedDatapackage;
    private LinkedBlockingQueue<UnsignedDatapackage> outboundUnsignedDatapackage;
    private String gameId;

    public DatapackageQueue() {
        this.inboundUnsignedDatapackage = new LinkedBlockingQueue<UnsignedDatapackage>();
        this.outboundUnsignedDatapackage = new LinkedBlockingQueue<UnsignedDatapackage>();
    }

    public UnsignedDatapackage getDatapackage() {
        try {
            UnsignedDatapackage unsignedDatapackage = inboundUnsignedDatapackage.take();
            Log.d("DatapackageQueue", "getDatapackage: " + unsignedDatapackage);
            return unsignedDatapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertDatapackage(UnsignedDatapackage unsignedDatapackage) {
        try {
            outboundUnsignedDatapackage.put(unsignedDatapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
