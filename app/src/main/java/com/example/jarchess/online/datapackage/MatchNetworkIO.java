package com.example.jarchess.online.datapackage;

import android.util.Log;

import com.example.jarchess.match.resignation.ResignationReciever;
import com.example.jarchess.match.resignation.ResignationSender;
import com.example.jarchess.match.result.ResignationResult;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.turn.TurnReceiver;
import com.example.jarchess.match.turn.TurnSender;
import com.example.jarchess.online.move.DatapackageQueue;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import static com.example.jarchess.online.datapackage.DatapackageType.RESIGNATION;

public class MatchNetworkIO {


    public static class Sender implements TurnSender, ResignationSender, Closeable {

        private static final String TAG = "MatchNetworkIO.Sender";
        private final Collection<Closeable> closeables = new LinkedList<Closeable>();
        private final DatapackageSender datapackageSender;
        private final Queue<Datapackage> outGoingDatapackages = new LinkedList<Datapackage>();
        private final Object lock = new Object();
        private final String destinationIP;
        private final int destinationPort;

        private boolean isAlive;

        public Sender(final DatapackageSender datapackageSender, String destinationIP, int destinationPort) {
            this.datapackageSender = datapackageSender;
            this.destinationIP = destinationIP;
            this.destinationPort = destinationPort;
            if (datapackageSender instanceof Closeable) {
                closeables.add((Closeable) datapackageSender);
            }

            //TODO Establish connection

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        isAlive = true;
                    }
                    try {
                        synchronized (lock) {
                            while (isAlive) {
                                waitWhileEmpty(outGoingDatapackages);
                                datapackageSender.send(outGoingDatapackages.remove());
                            }
                        }
                    } catch (InterruptedException e1) {
                        Log.e(TAG, "got interrupted ", e1);
                    } finally {
                        try {
                            close();
                        } catch (IOException e2) {
                            Log.e(TAG, "exception on close ", e2);
                        }
                    }

                }
            };

            new Thread(runnable, "MatchNetworkSender").start();
        }

        @Override
        public void close() throws IOException {
            Queue<IOException> ioExceptions = new LinkedList<IOException>();

            synchronized (lock) {

                isAlive = false;

                for (Closeable closeable : closeables) {
                    try {
                        if (closeable instanceof Flushable) {
                            ((Flushable) closeable).flush();
                        }
                        closeable.close();
                    } catch (IOException e) {
                        ioExceptions.add(e);
                    }
                }
            }

            for (IOException e : ioExceptions) {
                throw e;
            }
        }

        @Override
        public void send(Turn turn) {
            synchronized (lock) {
                outGoingDatapackages.add(new Datapackage(turn, destinationIP, destinationPort));
            }
        }

        @Override
        public void send(ResignationResult resignationResult) {
            synchronized (lock) {
                Datapackage datapackage = new Datapackage(RESIGNATION, destinationIP, destinationPort);
                outGoingDatapackages.add(datapackage);
            }
        }

        private synchronized void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait();
                }
            }
        }
    }

    public static class Receiver implements TurnReceiver, ResignationReciever, Closeable {

        private static final String TAG = "MatchNetworkIO.Receiver";
        private final Collection<Closeable> closeables = new LinkedList<Closeable>();
        private final DatapackageReceiver datapackageReceiver;
        private final Queue<Turn> incomingTurns = new LinkedList<Turn>();
        private final Queue<ResignationResult> incomingResignationResults = new LinkedList<ResignationResult>();
        private final Object lock = new Object();

        private boolean isAlive;

        public Receiver(final DatapackageReceiver datapackageReceiver) {
            this.datapackageReceiver = datapackageReceiver;
            if (datapackageReceiver instanceof Closeable) {
                closeables.add((Closeable) datapackageReceiver);
            }

            //TODO Establish connection
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    isAlive = true;
                    try {
                        while (isAlive) {
                            Datapackage datapackage = datapackageReceiver.recieveNextDatapackage();

                            switch (datapackage.getDatapackageType()) {

                                case TURN:
                                    incomingTurns.add(datapackage.getTurn());
                                    break;
                                case PAUSE_REQUEST:
//                                    incomingPauseRequest.add(datapackage.getPauseRequest());
//                                    break;
                                    throw new RuntimeException("Pause Request not implemented");
                                case PAUSE_ACCEPT:
//                                    incomingPauseAccept.add(datapackage.getPauseAccept());
//                                    break;
                                    throw new RuntimeException("Pause Accept not implemented");
                                case PAUSE_REJECT:
//                                    incomingPauseReject.add(datapackage.getPauseReject());
//                                    break;
                                    throw new RuntimeException("Pause Reject not implemented");

                                default:
                                    throw new IllegalStateException("Unexpected datapackage type: " + datapackage.getDatapackageType());
                            }
                        }
                    } catch (InterruptedException e1) {
                        Log.e(TAG, "got interrupted ", e1);
                    } finally {
                        try {
                            close();
                        } catch (IOException e2) {
                            Log.e(TAG, "exception on close ", e2);
                        }
                    }
                }
            };

            new Thread(runnable, "MatchNetworkReceiver").start();
        }

        @Override
        public void close() throws IOException {
            Queue<IOException> ioExceptions = new LinkedList<IOException>();

            synchronized (lock) {

                isAlive = false;

                for (Closeable closeable : closeables) {
                    try {
                        if (closeable instanceof Flushable) {
                            ((Flushable) closeable).flush();
                        }
                        closeable.close();
                    } catch (IOException e) {
                        ioExceptions.add(e);
                    }
                }
            }

            for (IOException e : ioExceptions) {
                throw e;
            }
        }

        @Override
        public Turn receiveNextTurn() throws InterruptedException {

            synchronized (lock) {
                waitWhileEmpty(incomingTurns);
                return incomingTurns.remove();
            }
        }

        @Override
        public ResignationResult recieveNextResignation() throws InterruptedException {

            synchronized (lock) {
                waitWhileEmpty(incomingResignationResults);
                return incomingResignationResults.remove();
            }
        }


        private synchronized void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait();
                }
            }
        }
    }

    public static class DatapackageQueueAdapter implements DatapackageSender, DatapackageReceiver {
        private final DatapackageQueue queue;

        public DatapackageQueueAdapter(DatapackageQueue queue) {
            this.queue = queue;
        }

        @Override
        public Datapackage recieveNextDatapackage() throws InterruptedException {
            return queue.getInboundDatapackage();
        }

        @Override
        public void send(Datapackage datapackage) {
            queue.insertOutboundDatapackage(datapackage);
        }
    }
}
