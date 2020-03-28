package com.example.jarchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jarchess.match.MatchStarter;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.match.styles.YellowBlackYellowCircleAvatarStyle;
import com.example.jarchess.online.move.DatapackageQueue;
import com.example.jarchess.online.networking.Controller;


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

                Intent intent = new Intent(getActivity(), LocalMultiplayerMatchActivity.class);
                startActivity(intent);

            }
        });

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO SETUP Multiplayer Stuff
                DatapackageQueue queue = new DatapackageQueue();

                RemoteOpponentAccount remoteOpponentAccount = new RemoteOpponentAccount("Remote Opponent",
                        YellowBlackYellowCircleAvatarStyle.getInstance());

                MatchStarter.getInstance().multiplayerSetup(queue, remoteOpponentAccount);

                Intent intent = new Intent(getActivity(), OnlineMultiplayerMatchActivity.class);
                startActivity(intent);
            }
        });
    }
}
