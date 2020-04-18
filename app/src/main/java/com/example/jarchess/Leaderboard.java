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

    private LeaderboardCommunicator callback;
    private Spinner sortSpinner;
    private ListView leaderboardList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> supposedlyEmptyList;

    public Leaderboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        supposedlyEmptyList = new ArrayList<>();
        supposedlyEmptyList.add("No Leaderboard Results Were Found");

        sortSpinner = view.findViewById(R.id.sortingSpinner);
        leaderboardList = view.findViewById(R.id.leaderList);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sortLabels, R.layout.spinner_item_style);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);


        listAdapter = new ArrayAdapter<>
                (view.getContext(), R.layout.list_item_style, supposedlyEmptyList);
        leaderboardList.setAdapter(listAdapter);

        displayOnLeaderboard(callback.onLeaderboardUpdate(0));
        return view;
    }

    private void displayOnLeaderboard(ArrayList<String> content) {
        listAdapter.clear();

        if(content == null){ listAdapter.addAll(supposedlyEmptyList);
        } else { listAdapter.addAll(content); }
    }


    @Override//For spinner
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        displayOnLeaderboard(callback.onLeaderboardUpdate(position));
    }

    @Override//For spinner
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setCommunicator(LeaderboardCommunicator callback) {
        this.callback = callback;
    }

    public interface LeaderboardCommunicator {
        ArrayList<String> onLeaderboardUpdate(int criteriaType);
    }


}

