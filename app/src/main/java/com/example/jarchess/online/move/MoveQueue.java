package com.example.jarchess.online.move;

import com.example.jarchess.match.datapackage.Datapackege;

import java.util.concurrent.LinkedBlockingQueue;

public class MoveQueue {
    private LinkedBlockingQueue<Datapackege> inboundMove;
    private LinkedBlockingQueue<Datapackege> outboundMove;
    private String gameId;

    public MoveQueue(){
        this.inboundMove = new LinkedBlockingQueue<Datapackege>();
        this.outboundMove = new LinkedBlockingQueue<Datapackege>();
    }

    public void insertMove(Datapackege datapackege){
        try {
            outboundMove.put(datapackege);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Datapackege getMove(){
        try {
           return inboundMove.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
