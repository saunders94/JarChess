package com.example.jarchess.match.datapackage;

import android.util.Log;

import com.example.jarchess.match.resignation.Resignation;
import com.example.jarchess.match.resignation.ResignationReciever;
import com.example.jarchess.match.resignation.ResignationSender;
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

import static com.example.jarchess.match.datapackage.Datapackage.DatapackageType.RESIGNATION;

public class MatchNetworkIO {
    private static void waitWhileEmpty(Queue<?> collection, Object lock) throws InterruptedException {
        synchronized (lock) {
            while (collection.isEmpty()) {
                lock.wait();
            }
        }
    }

    public static class Sender implements TurnSender, ResignationSender, Closeable {

        private static final String TAG = "MatchNetworkIO.Sender";
        private final Collection<Closeable> closeables = new LinkedList<Closeable>();
        private final DatapackageSender datapackageSender;
        private final Queue<Datapackage> outGoingDatapackages = new LinkedList<Datapackage>();
        private final Object lock = new Object();

        private boolean isAlive;

        public Sender(final DatapackageSender datapackageSender) {
            this.datapackageSender = datapackageSender;
            if (datapackageSender instanceof Closeable) {
                closeables.add((Closeable) datapackageSender);
            }

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        isAlive = true;
                    }
                    try {
                        synchronized (lock) {
                            while (isAlive) {
                                waitWhileEmpty(outGoingDatapackages, lock);
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
                outGoingDatapackages.add(new Datapackage(turn));
            }
        }

        @Override
        public void send(Resignation resignation) {
            synchronized (lock) {
                Datapackage datapackage = new Datapackage(RESIGNATION);
                outGoingDatapackages.add(datapackage);
            }
        }
    }

    public static class Reciever implements TurnReceiver, ResignationReciever, Closeable {

        private static final String TAG = "MatchNetworkIO.Receiver";
        private final Collection<Closeable> closeables = new LinkedList<Closeable>();
        private final DatapackageReceiver datapackageReceiver;
        private final Queue<Turn> incomingTurns = new LinkedList<Turn>();
        private final Queue<Resignation> incomingResignations = new LinkedList<Resignation>();
        private final Object lock = new Object();

        private boolean isAlive;

        public Reciever(final DatapackageReceiver datapackageReceiver) {
            this.datapackageReceiver = datapackageReceiver;
            if (datapackageReceiver instanceof Closeable) {
                closeables.add((Closeable) datapackageReceiver);
            }

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
                                case RESIGNATION:
                                    incomingResignations.add(datapackage.getResignation());
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
                waitWhileEmpty(incomingTurns, lock);
                return incomingTurns.remove();
            }
        }

        @Override
        public Resignation recieveNextResignation() throws InterruptedException {

            synchronized (lock) {
                waitWhileEmpty(incomingResignations, lock);
                return incomingResignations.remove();
            }
        }
    }

    public static class DatapackageQueueAddapter implements DatapackageSender, DatapackageReceiver {
        private final DatapackageQueue queue;

        public DatapackageQueueAddapter(DatapackageQueue queue) {
            this.queue = queue;
        }

        @Override
        public Datapackage recieveNextDatapackage() throws InterruptedException {
            return queue.getDatapackage();
        }

        @Override
        public void send(Datapackage datapackage) {
            queue.insertDatapackage(datapackage);
        }
    }
}
