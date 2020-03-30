package com.example.jarchess.online.datapackage;

import android.util.Log;

import com.example.jarchess.LoggedThread;
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
            Log.d(TAG, "Sender() called with: datapackageSender = [" + datapackageSender + "], destinationIP = [" + destinationIP + "], destinationPort = [" + destinationPort + "]");
            Log.d(TAG, "Sender is running on thread: " + Thread.currentThread().getName());

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
                                Log.d(TAG, "run: waiting wile outgoing datapackages are empty");
                                waitWhileEmpty(outGoingDatapackages);
                                Log.i(TAG, "run: ready to send");
                                datapackageSender.send(outGoingDatapackages.remove());
                                Log.i(TAG, "run: sent");
                            }
                        }
                    } catch (InterruptedException e1) {
                        Log.e(TAG, "got interrupted ", e1);
                    } finally {
                        Log.d(TAG, "run: closing anything we need to close");
                        try {
                            close();
                        } catch (IOException e2) {
                            Log.e(TAG, "exception on close ", e2);
                        }
                    }

                }
            };

            new LoggedThread(TAG, runnable, "MatchNetworkSender").start();
        }

        @Override
        public void close() throws IOException {
            Log.d(TAG, "close() called");
            Log.d(TAG, "close is running on thread: " + Thread.currentThread().getName());

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
            Log.d(TAG, "send() called with: turn = [" + turn + "]");
            Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());

            Log.d(TAG, "send: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "send: got lock");
                outGoingDatapackages.add(new Datapackage(turn, destinationIP, destinationPort));
            }
            Log.d(TAG, "send() returned: ");
        }

        @Override
        public void send(ResignationResult resignationResult) {
            Log.d(TAG, "send() called with: resignationResult = [" + resignationResult + "]");
            Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());

            synchronized (lock) {
                Datapackage datapackage = new Datapackage(RESIGNATION, destinationIP, destinationPort);
                outGoingDatapackages.add(datapackage);
                lock.notifyAll();
            }
        }

        private synchronized void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            Log.d(TAG, "waitWhileEmpty() called with: queue = [" + queue + "]");
            Log.d(TAG, "waitWhileEmpty is running on thread: " + Thread.currentThread().getName());

            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait(50);
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
            Log.d(TAG, "Receiver() called with: datapackageReceiver = [" + datapackageReceiver + "]");
            Log.d(TAG, "Receiver is running on thread: " + Thread.currentThread().getName());
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
            Log.d(TAG, "close() called");
            Log.d(TAG, "close is running on thread: " + Thread.currentThread().getName());
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
            Log.d(TAG, "receiveNextTurn() called");
            Log.d(TAG, "receiveNextTurn is running on thread: " + Thread.currentThread().getName());

            synchronized (lock) {
                waitWhileEmpty(incomingTurns);
                return incomingTurns.remove();
            }
        }

        @Override
        public ResignationResult recieveNextResignation() throws InterruptedException {
            Log.d(TAG, "recieveNextResignation() called");
            Log.d(TAG, "recieveNextResignation is running on thread: " + Thread.currentThread().getName());
            synchronized (lock) {
                waitWhileEmpty(incomingResignationResults);
                return incomingResignationResults.remove();
            }
        }


        private synchronized void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            Log.d(TAG, "waitWhileEmpty() called with: queue = [" + queue + "]");
            Log.d(TAG, "waitWhileEmpty is running on thread: " + Thread.currentThread().getName());
            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait(50);
                }
            }
        }
    }

    public static class DatapackageQueueAdapter implements DatapackageSender, DatapackageReceiver {
        private static final String TAG = "MatchNetworkIO.DQA";
        private final DatapackageQueue queue;


        public DatapackageQueueAdapter(DatapackageQueue queue) {
            this.queue = queue;
        }

        @Override
        public Datapackage recieveNextDatapackage() throws InterruptedException {
            Log.d(TAG, "recieveNextDatapackage() called");
            Log.d(TAG, "recieveNextDatapackage is running on thread: " + Thread.currentThread().getName());
            Datapackage tmp = queue.getLocalDatapackage();

            Log.d(TAG, "recieveNextDatapackage() returned: " + tmp);
            return tmp;

        }

        @Override
        public void send(Datapackage datapackage) {
            Log.d(TAG, "send() called with: datapackage = [" + datapackage + "]");
            Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
            queue.insertLocalDatapackageQueue(datapackage);
            Log.d(TAG, "send() returned: ");
        }
    }
}
