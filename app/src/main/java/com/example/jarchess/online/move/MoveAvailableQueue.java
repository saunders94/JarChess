package com.example.jarchess.online.move;

import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingQueue;

public class MoveAvailableQueue {
    private LinkedBlockingQueue<JSONObject> moveQueue;

    public MoveAvailableQueue(){
        this.moveQueue = new LinkedBlockingQueue<JSONObject>();
    }

    public void insert(JSONObject jsonObject){
        try {
            moveQueue.put(jsonObject);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JSONObject get(){
        try {
            JSONObject jsonObject = moveQueue.take();
            return jsonObject;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
