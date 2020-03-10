package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenu extends Fragment {


    private Button singlePlayerButton;
    private Button multiplayerButton;
    private Button leaderboardButton;

    public MainMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        singlePlayerButton = view.findViewById(R.id.button_single_player);
        multiplayerButton = view.findViewById(R.id.button_multiplayer);
        leaderboardButton = view.findViewById(R.id.button_leaderboard);

        setupListeners();


        return view;
    }

    private void setupListeners() {
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                AiDifficulty aiDifficulty = new AiDifficulty();
                transaction.replace(R.id.fragmentHole, aiDifficulty);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        multiplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MultiplayerType multiplayerType = new MultiplayerType();
                transaction.replace(R.id.fragmentHole, multiplayerType);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                Leaderboard leaderboard = new Leaderboard();
                transaction.replace(R.id.fragmentHole, leaderboard);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

}
