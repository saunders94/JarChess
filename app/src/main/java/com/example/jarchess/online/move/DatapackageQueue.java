package com.example.jarchess.online.move;

import android.util.Log;

import com.example.jarchess.online.datapackage.Datapackage;

import java.util.concurrent.LinkedBlockingQueue;

public class DatapackageQueue {
    private LinkedBlockingQueue<Datapackage> inboundDatapackage;
    private LinkedBlockingQueue<Datapackage> outboundDatapackage;
    private String gameId;

    public DatapackageQueue() {


        this.inboundDatapackage = new LinkedBlockingQueue<Datapackage>();
        this.outboundDatapackage = new LinkedBlockingQueue<Datapackage>();
    }

    public Datapackage getInboundDatapackage() {
        try {
            Datapackage datapackage = inboundDatapackage.take();
            Log.d("DatapackageQueue", "getInboundDatapackage: " + datapackage);
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Datapackage getOutboundDatapackage() {
        try {
            Datapackage datapackage = outboundDatapackage.take();
            Log.d("DatapackageQueue", "getOutboundDatapackage: " + datapackage);
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertInboundDatapackage(Datapackage datapackage) {
        try {
            inboundDatapackage.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertOutboundDatapackage(Datapackage datapackage) {
        try {
            outboundDatapackage.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
