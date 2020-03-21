package com.example.jarchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jarchess.match.MatchStarter;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.online.OnlineMatch;
import com.example.jarchess.online.OnlineMatchMaker;
import com.example.jarchess.online.networking.Controller;

import java.io.IOException;

import static android.support.constraint.Constraints.TAG;


public class MultiplayerType extends Fragment {
    private Button localButton;
    private Button onlineButton;

    public MultiplayerType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_multiplayer_type, container, false);
        localButton = view.findViewById(R.id.button_Local);
        onlineButton = view.findViewById(R.id.button_online);

        Controller networkController = new Controller();
        networkController.testSend();
        setupListeners();


        return view;
    }

    private void setupListeners() {
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Local button pressed");
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MainMenu mainMenu = new MainMenu();
                //transaction.replace(R.id.fragmentHole, mainMenu);
                //transaction.commit();


                Intent intent = new Intent(getActivity(), LocalMultiplayerMatchActivity.class);
                startActivity(intent);

            }
        });

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Online button pressed");
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MainMenu mainMenu = new MainMenu();
                //transaction.replace(R.id.fragmentHole, mainMenu);
                //transaction.commit();

                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchMakerLauncher matchMakerLauncher = new MatchMakerLauncher();
                transaction.replace(R.id.fragmentHole, matchMakerLauncher);
                transaction.addToBackStack(null);
                transaction.commit();

                //TODO show waiting for match dialog with a cancel button that will end the search



                // Username
                // IP Address
                // Port
                // Chosen Avatar Style (int 1)
                // Starting color
                Object lock = new Object();
                OnlineMatch onlineMatch = null;
                boolean canceled = false;

            }
        });
    }

    public static class MatchMakerLauncher extends Fragment {

        private OnlineMatch onlineMatch;
        private Button cancelMatchMakingButton;

        public MatchMakerLauncher() {
        }

        public void cancel() {
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OnlineMatchMaker.getInstance().getOnlineMatch();
                        MatchStarter.getInstance().multiplayerSetup(onlineMatch);

                        Intent intent = new Intent(getActivity(), OnlineMultiplayerMatchActivity.class);
                        startActivity(intent);
                    } catch (OnlineMatchMaker.SearchCanceledException e) {
                        Log.d(TAG, "onCreateView's run: online match maker was cancled");
                    } catch (IOException e) {
                        Log.e(TAG, "onCreateView's run: ", e);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "onCreateView's run: ", e);
                    }
                }
            }, "matchMakerLauncherThread").start();

            // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.fragment_match_maker_launcher, container, false);
            cancelMatchMakingButton = view.findViewById(R.id.cancel_match_making_button);

            cancelMatchMakingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                    getActivity().onBackPressed();
                }
            });


            return view;
        }
    }
}
