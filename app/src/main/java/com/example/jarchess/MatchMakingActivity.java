package com.example.jarchess;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jarchess.match.MatchBuilder;
import com.example.jarchess.online.OnlineMatchInfoBundle;
import com.example.jarchess.online.OnlineMatchMaker;

import java.io.IOException;


public class MatchMakingActivity extends AppCompatActivity {

    private static final String TAG = "MatchMakingActivity";
    public static volatile boolean cancelIsInProgress = false;
    private static MatchMakingActivity activity;
    private final MatchMakerLauncher matchMakerLauncher = new MatchMakerLauncher();

    public void finishWith(Result result, boolean wasCanceled) {
        Log.d(TAG, "finishWith() called with: result = [" + result + "], wasCanceled = [" + wasCanceled + "]");
        Log.d(TAG, "finishWith is running on thread: " + Thread.currentThread().getName());
        Intent intent = new Intent();
        intent.putExtra("CANCELED", wasCanceled);
        setResult(result.getInt(), intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        matchMakerLauncher.cancel();

        // cancel matchmaking
        //OnlineMatchMaker.getInstance().cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_match_making);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.match_making_frame_hole, matchMakerLauncher);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called");
        Log.d(TAG, "onStart is running on thread: " + Thread.currentThread().getName());
        super.onStart();
        if (cancelIsInProgress) {

            finishWith(Result.CANCEL_WAS_PROCESSING, true);
        } else {
            String opponentUsername = getIntent().getStringExtra("opponentUsername");
            matchMakerLauncher.startAction(opponentUsername);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public enum Result {
        SUCCESS,
        CANCELED,
        CANCEL_WAS_PROCESSING;

        private static final String TAG = "RequestCode";

        public int getInt() {
            Result[] values = values();

            for (int i = 0; i < values.length; i++) {
                if (values[i] == this) {
                    return i;
                }
            }
            Log.wtf(TAG, JZStringBuilder.build("getInt: ", this, "is not a value of ", this.getClass().getSimpleName()));
            return -1;
        }
    }

    public static class MatchMakerLauncher extends Fragment {

        private OnlineMatchInfoBundle onlineMatchInfoBundle;
        private Button cancelMatchMakingButton;

        public MatchMakerLauncher() {
        }

        public void cancel() {
            Log.d(TAG, "cancel() called");
            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());
            if (cancelIsInProgress) {
                Log.i(TAG, "cancel: ignoring because cancel is in progress");
                return;
            }
//            Runnable r = new Runnable() {
//                @Override
//                public void run() {
//                    OnlineMatchMaker.getInstance().cancel();
//                }
//            };
//            Thread t = new Thread(r);
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


            Thread t = new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    OnlineMatchMaker.getInstance().cancel();
                    cancelIsInProgress = false;
                }
            }, "cancelThread");
            cancelIsInProgress = true;
            t.start();

            activity.finishWith(Result.CANCELED, true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.fragment_match_maker_launcher, container, false);
            cancelMatchMakingButton = view.findViewById(R.id.cancel_match_making_button);

            cancelMatchMakingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "cancel button clicked");
                    cancel();
                }
            });


            return view;
        }

        public void startAction(@Nullable final String opponentUsername) {
            Log.d(TAG, "startAction: onlineMatchInfoBundle set to null");
            onlineMatchInfoBundle = null;
            // start a new thread to launch the online match maker.
            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i(TAG, "Creating onlinematchMakerBundle");
                        if (opponentUsername != null) {
                            onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle(opponentUsername);
                        } else {
                            onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
                        }
                        Log.i(TAG, "Online match info bundle set to " + onlineMatchInfoBundle);
                        MatchBuilder.getInstance().multiplayerSetup(onlineMatchInfoBundle);
                        activity.finishWith(Result.SUCCESS, false);

                    } catch (OnlineMatchMaker.SearchCanceledException e) {
                    } catch (IOException e) {
                        final String msg = "Connection Failure";
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Log.e(TAG, "onCreateView's run caught: ", e);

                        cancel();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "onCreateView's run caught: ", e);

                        cancel();
                    }
                }
            }, "matchMakerLauncherThread").start();
        }

        public void startAction() {
            startAction(null);
        }
    }


}