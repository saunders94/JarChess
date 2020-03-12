package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileMenu extends Fragment {

    private Button changeAvatarButton;
    private Button friendListButton;
    private Button changePasswordButton;
    private Button logoutButton;

    public ProfileMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_menu, container, false);
        changeAvatarButton = view.findViewById(R.id.button_change_avatar);
        friendListButton = view.findViewById(R.id.button_friend_list);
        changePasswordButton = view.findViewById(R.id.button_change_password);
        logoutButton = view.findViewById(R.id.button_logout);

        setupListeners();


        return view;
    }

    private void setupListeners() {
        changeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //AiDifficulty aiDifficulty = new AiDifficulty();
                //transaction.replace(R.id.fragmentHole, aiDifficulty);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }
        });

        friendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //AiDifficulty aiDifficulty = new AiDifficulty();
                //transaction.replace(R.id.fragmentHole, aiDifficulty);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MultiplayerType multiplayerType = new MultiplayerType();
                //transaction.replace(R.id.fragmentHole, multiplayerType);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MainMenu mainMenu = new MainMenu();
                //transaction.replace(R.id.fragmentHole, mainMenu);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }
        });
    }

}