package com.example.jarchess;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartPage extends Fragment {

    private Button startButton;
    private LinearLayout layout;

    public StartPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        int orientation = view.getResources().getConfiguration().orientation;
        startButton = view.findViewById(R.id.button_start);
        layout = view.findViewById(R.id.startLinearLayout);
        setupListeners();
        setOrientation(orientation);


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

    @Override//This method allows things to be changed when the phone is turned
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setOrientation(newConfig.orientation);
    }

    private void setOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.setOrientation(LinearLayout.HORIZONTAL);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            layout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    private void setupListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MainMenu mainMenu = new MainMenu();
                transaction.replace(R.id.fragmentHole, mainMenu);
                transaction.addToBackStack("str");
                transaction.commit();
            }
        });
    }

}
