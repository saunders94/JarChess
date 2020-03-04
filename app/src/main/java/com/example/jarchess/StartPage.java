package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartPage extends Fragment {

    private Button startButton;

    public StartPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        startButton = view.findViewById(R.id.button_start);

        setupListeners();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } catch (NullPointerException e) {
            System.out.println("Couldn't find toolbar!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } catch (NullPointerException e) {
            System.out.println("Couldn't find toolbar!");
        }
    }

    private void setupListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MainMenu mainMenu = new MainMenu();
                transaction.replace(R.id.fragmentHole, mainMenu);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

}
