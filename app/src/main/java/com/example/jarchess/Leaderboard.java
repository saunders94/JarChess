package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class Leaderboard extends Fragment implements AdapterView.OnItemSelectedListener {


    private Spinner sortSpinner;
    private ListView leaderboardList;

    public Leaderboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        sortSpinner = view.findViewById(R.id.sortingSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sortLabels, R.layout.spinner_item_style);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);
        //handle onclick listener by using onItemSelected and onNothingSelected


        //Here's a demo to show the leaderboard list

        ArrayList<String> listItems = new ArrayList<>();

        listItems.add("(1)    100  wins - KyleIsNeat");
        listItems.add("(2)    45   wins - Goober03");
        listItems.add("(3)    23   wins - earthDude21");
        listItems.add("(4)    9    wins - kratos69");



        ArrayAdapter<String> listAdapter = new ArrayAdapter<>
                (view.getContext(), R.layout.leaderboard_item_style,listItems);
        leaderboardList = view.findViewById(R.id.leaderList);
        leaderboardList.setAdapter(listAdapter);


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
