package com.example.jarchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jarchess.match.MatchBuilder;
import com.example.jarchess.match.activity.EasyAIMatchActivity;
import com.example.jarchess.match.activity.HardAIMatchActivity;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.online.OnlineMatchInfoBundle;
import com.example.jarchess.online.OnlineMatchMaker;
import com.example.jarchess.testmode.TestMode;

import java.io.IOException;

import static com.example.jarchess.MainActivity.fragmentManager;


public class MatchSettings extends Fragment {
    private static final String TAG = "MatchSettings";

    private Switch speedRoundSwitch;//FIXME Still needs to be determined how this will be conveyed
    private Switch enableCommitSwitch;//FIXME Still needs to be determined how this will be conveyed
    private Button readyButton;

    public MatchSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_settings, container, false);

        speedRoundSwitch = view.findViewById(R.id.switch_speed_round);
        enableCommitSwitch = view.findViewById(R.id.switch_enable_commit);
        readyButton = view.findViewById(R.id.button_ready);

        setupListeners();
        return view;
    }

    private void setupListeners() {
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDestination();
            }
        });
    }

    //this method chooses the match type selected by reviewing the backstack
    private void chooseDestination() {
        FragmentManager.BackStackEntry backEntry = fragmentManager
                .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String tag = backEntry.getName();
        try {
            switch (tag) {
                case "EASY":
                    startEasyMatch();
                    break;
                case "HARD":
                    startHardMatch();
                    break;
                case "LOCAL": startLocal();
                    break;
                case "RANDOM": startRandom();
                    break;
                case "FRIEND":
                    break;
                default:
                    System.err.println("Unknown match type");
            }
        } catch (NullPointerException e) {
               System.out.println("Null must've been added to the back stack");
        }
    }

    private void startEasyMatch() {
        if (EasyAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {
            Intent intent = new Intent(getActivity(), EasyAIMatchActivity.class);
            startActivity(intent);
        } else {
            Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "We don't have this mode working yet:EASY", duration).show();
        }
    }

    private void startHardMatch() {
        if (HardAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {

            Intent intent = new Intent(getActivity(), HardAIMatchActivity.class);
            startActivity(intent);

        } else {
            Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "We don't have this mode working yet:HARD", duration).show();
        }
    }

    private void startLocal() {
        MatchBuilder.getInstance().setMatchClockChoice(JarAccount.getInstance().getPreferredMatchClock());
        Intent intent = new Intent(getActivity(), LocalMultiplayerMatchActivity.class);
        startActivity(intent);
    }

    private void startRandom() {
        try {
            if (JarAccount.getInstance().isLoggedIn()) {
            //start matchmaking
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchMakerLauncher matchMakerLauncher = new MatchMakerLauncher();
                transaction.replace(R.id.fragmentHole, matchMakerLauncher);
                transaction.addToBackStack("MatchMakerLauncher");

                transaction.commit();

            } else {
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getContext(), "Login Required to play Online", duration).show();
            }
        } catch (IOException e) {
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "Cannot connect to the server. Please make sure you have internet access.", duration).show();
        }

    }

    public static class MatchMakerLauncher extends android.support.v4.app.Fragment {

        private OnlineMatchInfoBundle onlineMatchInfoBundle;
        private Button cancelMatchMakingButton;

        public MatchMakerLauncher() {
        }

        public void cancel() {
            Log.d(TAG, "cancel() called");
            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());
            OnlineMatchMaker.getInstance().cancel();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // start a new thread to launch the online match maker.
            new LoggedThread(TAG, new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i(TAG, "Creating onlinematchMakerBundle");
                        onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
                        Log.i(TAG, "Online match info bundle created");
                        MatchBuilder.getInstance().multiplayerSetup(onlineMatchInfoBundle);

                        Intent intent = new Intent(getActivity(), OnlineMultiplayerMatchActivity.class);
                        startActivity(intent);
                    } catch (OnlineMatchMaker.SearchCanceledException e) {
                        Log.d(TAG, "onCreateView's run caught:", e);
                    } catch (IOException e) {
                        Log.e(TAG, "onCreateView's run caught: ", e);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "onCreateView's run caught: ", e);
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
                    getActivity().onBackPressed();
                }
            });


            return view;
        }
    }
}
