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
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchSettings matchSettings = new MatchSettings();
                transaction.replace(R.id.fragmentHole, matchSettings);
                transaction.addToBackStack("EASY");
                transaction.commit();

            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchSettings matchSettings = new MatchSettings();
                transaction.replace(R.id.fragmentHole, matchSettings);
                transaction.addToBackStack("HARD");
                transaction.commit();

            }
        });
    }


}