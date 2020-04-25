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
        transaction.add(R.id.match_making_frame_hole, new MatchMakerLauncher());
        transaction.commit();
    }

    public static class MatchMakerLauncher extends Fragment {

        private OnlineMatchInfoBundle onlineMatchInfoBundle;
        private Button cancelMatchMakingButton;

        public MatchMakerLauncher() {
        }

        public void cancel() {
            Log.d(TAG, "cancel() called");
            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());
            OnlineMatchMaker.getInstance().cancel();
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("CANCELED", true);
                        getActivity().setResult(0, intent);
                        getActivity().finish();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // start a new thread to launch the online match maker.
            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i(TAG, "Creating onlinematchMakerBundle");
                        onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
                        Log.i(TAG, "Online match info bundle = " + onlineMatchInfoBundle);
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
    }


}