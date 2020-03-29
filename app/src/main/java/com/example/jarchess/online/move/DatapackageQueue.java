package com.example.jarchess.online.move;

import android.util.Log;

import com.example.jarchess.online.datapackage.Datapackage;

import java.util.concurrent.LinkedBlockingQueue;

public class DatapackageQueue {
    private LinkedBlockingQueue<Datapackage> localDatapackageQueue;
    private LinkedBlockingQueue<Datapackage> networkDatapackageQueue;
    private String gameId;

    public DatapackageQueue() {


        this.localDatapackageQueue = new LinkedBlockingQueue<Datapackage>();
        this.networkDatapackageQueue = new LinkedBlockingQueue<Datapackage>();
    }

    public Datapackage getLocalDatapackage() {
        try {
            Datapackage datapackage = localDatapackageQueue.take();
            Log.d("DatapackageQueue", "getInboundDatapackage: " + datapackage);
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Datapackage getNetworkDatapackageQueue() {
        try {
            Datapackage datapackage = networkDatapackageQueue.take();
            Log.d("DatapackageQueue", "getOutboundDatapackage: " + datapackage);
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertLocalDatapackageQueue(Datapackage datapackage) {
        try {
            localDatapackageQueue.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertNetworkDatapackageQueue(Datapackage datapackage) {
        try {
            networkDatapackageQueue.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
