package com.example.jarchess.match;

import android.util.Log;

import com.example.jarchess.LoggedThread;
import com.example.jarchess.match.events.MatchEndingEvent;
import com.example.jarchess.match.events.MatchEndingEventManager;
import com.example.jarchess.match.participant.RemoteOpponent;
import com.example.jarchess.match.result.ChessMatchResult;
import com.example.jarchess.match.turn.Turn;
import com.example.jarchess.match.turn.TurnReceiver;
import com.example.jarchess.match.turn.TurnSender;
import com.example.jarchess.online.datapackage.ChessMatchResultSender;
import com.example.jarchess.online.datapackage.Datapackage;
import com.example.jarchess.online.datapackage.DatapackageReceiver;
import com.example.jarchess.online.datapackage.DatapackageSender;
import com.example.jarchess.online.datapackage.DatapackageType;
import com.example.jarchess.online.move.DatapackageQueue;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class MatchNetworkIO {


    public static class Sender implements TurnSender, ChessMatchResultSender, DrawResponseSender, DrawRequestSender, PauseResponseSender, PauseRequestSender, Closeable {

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
                        Log.e(TAG, "close: ", e);
                        ioExceptions.add(e);
                    }
                }
                closeables.clear();
            }

            try {
                for (IOException e : ioExceptions) {
                    throw e;
                }
            } finally {
                ioExceptions.clear();
            }
        }

        @Override
        public void send(Turn turn) {
            if (turn != null) {
                Log.d(TAG, "send() called with: turn = [" + turn + "]");
                Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());

                Log.d(TAG, "send: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "send: got lock");
                    outGoingDatapackages.add(new Datapackage(turn, destinationIP, destinationPort));
                    lock.notifyAll();
                }
                Log.d(TAG, "send() returned: ");
            }
        }

        @Override
        public void send(ChessMatchResult chessMatchResult) {
            if (chessMatchResult != null) {
                Log.d(TAG, "send() called with: chessMatchResult = [" + chessMatchResult + "]");
                Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());

                Log.d(TAG, "send: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "send: got lock");
                    Datapackage datapackage = new Datapackage(chessMatchResult, destinationIP, destinationPort);
                    outGoingDatapackages.add(datapackage);
                    lock.notifyAll();
                }
                Log.d(TAG, "send() returned ");
            }
        }

        @Override
        public void send(DrawResponse drawResponse) {
            if (drawResponse != null) {
                Log.d(TAG, "send() called with: drawResponse = [" + drawResponse + "]");
                Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
                DatapackageType datapackageType;
                if (drawResponse.isAccepted()) {
                    datapackageType = DatapackageType.DRAW_ACCEPT;
                } else {
                    datapackageType = DatapackageType.DRAW_REJECT;
                }
                Log.d(TAG, "send: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "send: got lock");
                    Datapackage datapackage = new Datapackage(datapackageType, destinationIP, destinationPort);
                    outGoingDatapackages.add(datapackage);
                    lock.notifyAll();
                }
                Log.d(TAG, "send() returned ");
            }
        }

        @Override
        public void send(PauseResponse pauseResponse) {

            if (pauseResponse != null) {
                Log.d(TAG, "send() called with: pauseResponse = [" + pauseResponse + "]");
                Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
                DatapackageType datapackageType;
                if (pauseResponse.isAccepted()) {
                    datapackageType = DatapackageType.PAUSE_ACCEPT;
                } else {
                    datapackageType = DatapackageType.PAUSE_REJECT;
                }
                Log.d(TAG, "send: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "send: got lock");
                    Datapackage datapackage = new Datapackage(datapackageType, destinationIP, destinationPort);
                    outGoingDatapackages.add(datapackage);
                    lock.notifyAll();
                }
                Log.d(TAG, "send() returned ");
            }
        }

        @Override
        public void sendDrawRequest() {
            Log.d(TAG, "sendDrawRequest() called");
            Log.d(TAG, "sendDrawRequest is running on thread: " + Thread.currentThread().getName());
            Log.d(TAG, "send: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "send: got lock");
                Datapackage datapackage = new Datapackage(DatapackageType.DRAW_REQUEST, destinationIP, destinationPort);
                outGoingDatapackages.add(datapackage);
                lock.notifyAll();
            }
            Log.d(TAG, "send() returned ");
        }

        @Override
        public void sendPauseRequest() {
            Log.d(TAG, "sendPauseRequest() called");
            Log.d(TAG, "sendPauseRequest is running on thread: " + Thread.currentThread().getName());
            Log.d(TAG, "send: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "send: got lock");
                Datapackage datapackage = new Datapackage(DatapackageType.PAUSE_REQUEST, destinationIP, destinationPort);
                outGoingDatapackages.add(datapackage);
                lock.notifyAll();
            }
            Log.d(TAG, "send() returned ");

        }

        public void sendResumeRequest() {
            Log.d(TAG, "sendResumeRequest() called");
            Log.d(TAG, "sendResumeRequest is running on thread: " + Thread.currentThread().getName());
            Log.d(TAG, "send: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "send: got lock");
                Datapackage datapackage = new Datapackage(DatapackageType.RESUME_REQUEST, destinationIP, destinationPort);
                outGoingDatapackages.add(datapackage);
                lock.notifyAll();
            }
            Log.d(TAG, "send() returned ");
        }

        private synchronized void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            Log.d(TAG, "waitWhileEmpty() called with: queue = [" + queue + "]");
            Log.d(TAG, "waitWhileEmpty is running on thread: " + Thread.currentThread().getName());

            Log.d(TAG, "waitWhileEmpty: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "waitWhileEmpty: got lock");
                while (queue.isEmpty()) {
                    lock.wait();
                }
            }
            Log.d(TAG, "waitWhileEmpty() returned");
        }
    }

    public static class Receiver implements TurnReceiver, Closeable {

        private static final String TAG = "MatchNetworkIO.Receiver";
        private final Collection<Closeable> closeables = new LinkedList<>();
        private final DatapackageReceiver datapackageReceiver;
        private final Queue<Turn> incomingTurns = new LinkedList<>();
        private final Queue<DrawResponse> incomingDrawResponses = new LinkedList<>();
        private final Queue<PauseResponse> incomingPauseResponses = new LinkedList<>();
        private final Object lock = new Object();
        private final RemoteOpponent listener;
        private final LoggedThread thread;
        private boolean isAlive;
        private Queue<ResumeRequest> incomingResumeRequests;

        public Receiver(final DatapackageReceiver datapackageReceiver, RemoteOpponent remoteOpponent) {
            this.listener = remoteOpponent;
            Log.d(TAG, "Receiver() called with: datapackageReceiver = [" + datapackageReceiver + "]");
            Log.d(TAG, "Receiver is running on thread: " + Thread.currentThread().getName());
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
                            Log.d(TAG, "run: waiting for lock");
                            synchronized (lock) {
                                Log.d(TAG, "run: got lock");
                                switch (datapackage.getDatapackageType()) {

                                    case TURN:
                                        Log.d(TAG, "run: add turn to incoming turns queue");
                                        incomingTurns.add(datapackage.getTurn());
                                        lock.notifyAll();
                                        break;

                                    case MATCH_RESULT:
                                        Log.d(TAG, "run: handling received match result");
                                        MatchEndingEventManager.getInstance().notifyAllListeners(new MatchEndingEvent(datapackage.getMatchResult()));
                                        try {
                                            close();
                                        } catch (IOException e) {
                                            Log.e(TAG, "run: ", e);
                                        }
                                        lock.notifyAll();
                                        break;

                                    case PAUSE_REQUEST:
                                        Log.d(TAG, "run: handling received pause request");
                                        new LoggedThread(TAG, new Runnable() {
                                            @Override
                                            public void run() {
                                                listener.processRemotePauseRequest();
                                            }
                                        }, "pauseRequestThread").start();
                                        break;

                                    case PAUSE_ACCEPT:
                                        Log.d(TAG, "run: handling received pause request acceptance");
                                        if (incomingPauseResponses.isEmpty()) {
                                            incomingPauseResponses.add(PauseResponse.ACCEPT);
                                        }
                                        break;

                                    case PAUSE_REJECT:
                                        Log.d(TAG, "run: handling received pause request rejection");
                                        if (incomingPauseResponses.isEmpty()) {
                                            incomingPauseResponses.add(PauseResponse.REJECT);
                                        }
                                        break;

                                    case DRAW_REQUEST:
                                        Log.d(TAG, "run: handling received draw request");
                                        new LoggedThread(TAG, new Runnable() {
                                            @Override
                                            public void run() {
                                                listener.processRemoteDrawRequest();
                                            }
                                        }, "drawRequestThread").start();
                                        break;

                                    case DRAW_ACCEPT:
                                        Log.d(TAG, "run: handling received draw request acceptance");
                                        if (incomingDrawResponses.isEmpty()) {
                                            incomingDrawResponses.add(DrawResponse.ACCEPT);
                                        }
                                        break;

                                    case DRAW_REJECT:
                                        Log.d(TAG, "run: handling received draw request rejection");
                                        if (incomingDrawResponses.isEmpty()) {
                                            incomingDrawResponses.add(DrawResponse.REJECT);
                                        }
                                        break;

                                    case RESUME_REQUEST:
                                        Log.d(TAG, "run: handling received resume request rejection");
                                        if (incomingDrawResponses.isEmpty()) {
                                            incomingDrawResponses.add(DrawResponse.REJECT);
                                        }
                                        break;

                                    default:
                                        throw new IllegalStateException("Unexpected datapackage type: " + datapackage.getDatapackageType());
                                }
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

            thread = new LoggedThread(TAG, runnable, "MatchNetworkReceiver");
            thread.start();
        }

        @Override
        public void close() throws IOException {
            Log.d(TAG, "close() called");
            Log.d(TAG, "close is running on thread: " + Thread.currentThread().getName());
            Queue<IOException> ioExceptions = new LinkedList<IOException>();

            Log.d(TAG, "close: waiting for lock");
            synchronized (lock) {
                Log.d(TAG, "close: got lock");

                isAlive = false;

                for (Closeable closeable : closeables) {
                    try {
                        if (closeable instanceof Flushable) {
                            ((Flushable) closeable).flush();
                        }
                        closeable.close();
                    } catch (IOException e) {
                        ioExceptions.add(e);
                        Log.e(TAG, "close: ", e);
                    }
                }

                closeables.clear();
            }

            try {
                for (IOException e : ioExceptions) {
                    throw e;
                }
            } finally {
                try {
                    thread.interrupt();
                } finally {

                    ioExceptions.clear();
                }
            }
        }

        public DrawResponse receiveNextDrawResponse() throws InterruptedException {
            Log.d(TAG, "receiveNextDrawRequest() called");
            Log.d(TAG, "receiveNextDrawRequest is running on thread: " + Thread.currentThread().getName());
            DrawResponse drawResponse = null;
            while (drawResponse == null) {
                Log.d(TAG, "receiveNextDrawRequest: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "receiveNextDrawRequest: got lock");
                    Log.d(TAG, "receiveNextDrawRequest: waiting while empty");
                    waitWhileEmpty(incomingDrawResponses);
                    drawResponse = incomingDrawResponses.remove();
                    Log.d(TAG, "receiveNextDrawRequest: drawResponse = " + drawResponse);
                }
            }
            return drawResponse;
        }

        public PauseResponse receiveNextPauseResponse() throws InterruptedException {
            Log.d(TAG, "receiveNextPauseRequest() called");
            Log.d(TAG, "receiveNextPauseRequest is running on thread: " + Thread.currentThread().getName());
            PauseResponse pauseResponse = null;
            while (pauseResponse == null) {
                Log.d(TAG, "receiveNextPauseRequest: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "receiveNextPauseRequest: got lock");
                    Log.d(TAG, "receiveNextPauseRequest: waiting while empty");
                    waitWhileEmpty(incomingPauseResponses);
                    pauseResponse = incomingPauseResponses.remove();
                    Log.d(TAG, "receiveNextPauseRequest: pauseResponse = " + pauseResponse);
                }
            }
            return pauseResponse;
        }

        public ResumeRequest receiveNextResumeRequest() throws InterruptedException {
            Log.d(TAG, "receiveNextResumeRequest() called");
            Log.d(TAG, "receiveNextResumeRequest is running on thread: " + Thread.currentThread().getName());
            ResumeRequest resumeRequest = null;
            while (resumeRequest == null) {
                Log.d(TAG, "receiveNextResumeRequest: waiting for lock");
                synchronized (lock) {
                    Log.d(TAG, "receiveNextResumeRequest: got lock");
                    Log.d(TAG, "receiveNextResumeRequest: waiting while empty");
                    waitWhileEmpty(incomingResumeRequests);
                    resumeRequest = incomingResumeRequests.remove();
                    Log.d(TAG, "receiveNextResumeRequest: resumeRequest = " + resumeRequest);
                }
            }
            return resumeRequest;
        }

        @Override
        public Turn receiveNextTurn() throws InterruptedException {

            Log.d(TAG, "receiveNextTurn() called");
            Log.d(TAG, "receiveNextTurn is running on thread: " + Thread.currentThread().getName());
            Turn turn = null;
            while (turn == null) {
                synchronized (lock) {
                    waitWhileEmpty(incomingTurns);
                    turn = incomingTurns.remove();

                }
            }
            return turn;
        }


        private void waitWhileEmpty(Queue<?> queue) throws InterruptedException {
            Log.d(TAG, "waitWhileEmpty() called with: queue = [" + queue + "]");
            Log.d(TAG, "waitWhileEmpty is running on thread: " + Thread.currentThread().getName());
            synchronized (lock) {
                while (queue.isEmpty()) {
                    lock.wait();
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
            Datapackage tmp = queue.getClientBoundDatapackage();

            Log.d(TAG, "recieveNextDatapackage() returned: " + tmp);
            return tmp;

        }

        @Override
        public void send(Datapackage datapackage) {
            Log.d(TAG, "send() called with: datapackage = [" + datapackage + "]");
            Log.d(TAG, "send is running on thread: " + Thread.currentThread().getName());
            queue.insertServerBoundDatapackageQueue(datapackage);
            Log.d(TAG, "send() returned: ");
        }
    }
}
