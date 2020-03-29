package com.example.jarchess;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jarchess.match.activity.EasyAIMatchActivity;
import com.example.jarchess.match.activity.HardAIMatchActivity;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.testmode.TestMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class AiDifficulty extends Fragment {

    private Button easyButton;
    private Button hardButton;
    private static final String TAG = "AiDifficulty";

    public AiDifficulty() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ai_difficulty, container, false);
        easyButton = view.findViewById(R.id.button_easy);
        hardButton = view.findViewById(R.id.button_hard);

        setupListeners();


        return view;
    }

    private void setupListeners() {
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Easy button pressed");
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MainMenu mainMenu = new MainMenu();
                //transaction.replace(R.id.fragmentHole, mainMenu);
                //transaction.commit();)

                if (EasyAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {
                    Intent intent = new Intent(getActivity(), EasyAIMatchActivity.class);
                    startActivity(intent);
                } else {
                    Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(v.getContext(), "We don't have this mode working yet.", duration).show();
                }
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Hard button pressed");
                //FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                //MainMenu mainMenu = new MainMenu();
                //transaction.replace(R.id.fragmentHole, mainMenu);
                //transaction.commit();

                if (HardAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {

                    Intent intent = new Intent(getActivity(), HardAIMatchActivity.class);
                    startActivity(intent);

                } else {
                    Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(v.getContext(), "We don't have this mode working yet.", duration).show();
                }
            }
        });
    }


}
