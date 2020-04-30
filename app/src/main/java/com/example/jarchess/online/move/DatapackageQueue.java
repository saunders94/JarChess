package com.example.jarchess.online.move;

import android.util.Log;

import com.example.jarchess.online.datapackage.Datapackage;

import java.util.concurrent.LinkedBlockingQueue;

public class DatapackageQueue {
    private final String TAG = "DatapackageQueue";
    private LinkedBlockingQueue<Datapackage> clientBoundDatapackageQueue;
    private LinkedBlockingQueue<Datapackage> serverBoundDatapackageQueue;
    private String gameId;

    public DatapackageQueue() {
        this.clientBoundDatapackageQueue = new LinkedBlockingQueue<Datapackage>();
        this.serverBoundDatapackageQueue = new LinkedBlockingQueue<Datapackage>();
    }

    public Datapackage getClientBoundDatapackage() {
        try {
            Datapackage datapackage = clientBoundDatapackageQueue.take();
            if (datapackage != null) {
                Log.i(TAG, "getClientBoundDatapackage: " + datapackage);
            }
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Datapackage getServerBoundDatapackage() {
        try {
            Datapackage datapackage = serverBoundDatapackageQueue.take();
            Log.i(TAG, "getServerBoundDatapackage: " + datapackage);
            return datapackage;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertClientBoundDatapackageQueue(Datapackage datapackage) {
        try {
            Log.i(TAG, "insertClientBoundDatapackageQueue: " + datapackage);
            clientBoundDatapackageQueue.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertServerBoundDatapackageQueue(Datapackage datapackage) {
        try {
            Log.i(TAG, "insertServerBoundDatapackageQueue: " + datapackage);
            serverBoundDatapackageQueue.put(datapackage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
