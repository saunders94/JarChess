package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jarchess.online.networking.Controller;


public class MatchmakingType extends Fragment {

    private Button randomOpponentButton;
    private Button selectFriendButton;

    public MatchmakingType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_matchmaking_type, container, false);
        randomOpponentButton = view.findViewById(R.id.button_random_opponent);
        selectFriendButton = view.findViewById(R.id.button_select_friend);

        Controller networkController = new Controller();
        //networkController.testSend();

        setupListeners();


        return view;
    }

    private void setupListeners() {

        randomOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchSettings matchSettings = new MatchSettings();
                transaction.replace(R.id.fragmentHole, matchSettings);
                transaction.addToBackStack("RANDOM");
                transaction.commit();

            }
        });

        selectFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                FriendSelector friendSelector = new FriendSelector();
                transaction.replace(R.id.fragmentHole, friendSelector);
                transaction.addToBackStack("FriendSelector");
                transaction.commit();

            }
        });


    }

}
