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

    public static final int SUCCESS_RESULT_CODE = 0;
    private static final String TAG = "MatchMakingActivity";
    private static final int FAILURE_RESULT_CODE = 1;
    private final MatchMakerLauncher matchMakerLauncher = new MatchMakerLauncher();

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // cancel matchmaking
        //OnlineMatchMaker.getInstance().cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        matchMakerLauncher.startAction();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public static class MatchMakerLauncher extends Fragment {

        private OnlineMatchInfoBundle onlineMatchInfoBundle;
        private Button cancelMatchMakingButton;

        public MatchMakerLauncher() {
        }

        public void cancel() {
            Log.d(TAG, "cancel() called");
            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());
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

            try {
                OnlineMatchMaker.getInstance().cancel();
            } catch (InterruptedException e) {
                Intent intent = new Intent();
                intent.putExtra("CANCELED", true);
                getActivity().setResult(MatchMakingActivity.FAILURE_RESULT_CODE, intent);
                getActivity().finish();
                return;
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: cancel should be successful");
                        Intent intent = new Intent();
                        intent.putExtra("CANCELED", true);
                        getActivity().setResult(SUCCESS_RESULT_CODE, intent);
                        getActivity().finish();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // start a new thread to launch the online match maker.
//            new LoggedThread(TAG, new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Log.i(TAG, "Creating onlinematchMakerBundle");
//                        onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
//                        Log.i(TAG, "Online match info bundle = " + onlineMatchInfoBundle);
//                        MatchBuilder.getInstance().multiplayerSetup(onlineMatchInfoBundle);
//                        Intent intent = new Intent();
//                        intent.putExtra("CANCELED", false);
//                        getActivity().setResult(0, intent);
//                        getActivity().finish();
//
//                    } catch (OnlineMatchMaker.SearchCanceledException e) {
//                        // just get out... nothing more needs to be done;
//                    } catch (IOException e) {
//                        final String msg = "Connection Failure";
//                        if (getActivity() != null) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                        Log.e(TAG, "onCreateView's run caught: ", e);
//
//                        cancel();
//                    } catch (InterruptedException e) {
//                        Log.e(TAG, "onCreateView's run caught: ", e);
//
//                        cancel();
//                    }
//                }
//            }, "matchMakerLauncherThread").start();

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

        public void startAction() {
            Log.d(TAG, "startAction: onlineMatchInfoBundle set to null");
            onlineMatchInfoBundle = null;
            // start a new thread to launch the online match maker.
            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i(TAG, "Creating onlinematchMakerBundle");
                        onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
                        Log.i(TAG, "Online match info bundle set to " + onlineMatchInfoBundle);
                        MatchBuilder.getInstance().multiplayerSetup(onlineMatchInfoBundle);
                        Intent intent = new Intent();
                        intent.putExtra("CANCELED", false);
                        getActivity().setResult(0, intent);
                        getActivity().finish();

                    } catch (OnlineMatchMaker.SearchCanceledException e) {
                        // just get out... nothing more needs to be done;
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
    }


}