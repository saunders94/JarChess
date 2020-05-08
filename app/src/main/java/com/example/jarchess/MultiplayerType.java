package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jarchess.jaraccount.JarAccount;


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

        setupListeners();


        return view;
    }

    private void setupListeners() {
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchSettings matchSettings = new MatchSettings();
                transaction.replace(R.id.fragmentHole, matchSettings);
                transaction.addToBackStack("LOCAL");
                transaction.commit();

            }
        });

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                try {
                if (JarAccount.getInstance().isLoggedIn()) {

                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                    MatchmakingType matchmakingType = new MatchmakingType();
                    transaction.replace(R.id.fragmentHole, matchmakingType);
                    transaction.addToBackStack("matchMakingType");
                    transaction.commit();


//                    //TODO remove this when Friend games are working
//                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
//                    MatchSettings matchSettings = new MatchSettings();
//                    transaction.replace(R.id.fragmentHole, matchSettings);
//                    transaction.addToBackStack("RANDOM");
//                    transaction.commit();

                } else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(v.getContext(), "Login Required to play Online", duration).show();
                }
//                } catch (IOException e) {
//                    int duration = Toast.LENGTH_LONG;
//                    Toast.makeText(v.getContext(), "Cannot connect to the server. " +
//                            "Please make sure you have internet access.", duration).show(); }
            }
        });
    }

}
