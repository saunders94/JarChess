package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.jarchess.online.JSONCompiler.JSONLeaderboard;
import com.example.jarchess.online.networking.DataSender;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class Leaderboard extends Fragment implements AdapterView.OnItemSelectedListener {

    private LeaderboardCommunicator callback;
    private Spinner sortSpinner;
    private ListView leaderboardList;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> supposedlyEmptyList;

    private String TAG = "Leaderboard";


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


        ArrayList<String> listItems = getLeaderboard();
        listAdapter = new ArrayAdapter<>
                (view.getContext(), R.layout.list_item_style, listItems);
        leaderboardList.setAdapter(listAdapter);

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

    private ArrayList<String> getLeaderboard(){
        ArrayList<String> listItems = new ArrayList<>();

        JSONObject requestObject = new JSONObject();
        JSONObject data = null;
        JSONObject user = null;

        DataSender sender = new DataSender();

        try {
            requestObject = sender.send(new JSONLeaderboard().getMostGamesWon(10));
            Log.i(TAG, requestObject.toString());
            data = new JSONObject(requestObject.getString("data"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }catch (JSONException e2){
            e2.printStackTrace();
        }

        for(int i = 0; i < 10; i++){

            try {
                user = new JSONObject(data.getString("user" + String.valueOf(i)));
                String username = user.getString("username");
                String gamesWon = user.getString("games_won");
                String displayString = String.valueOf(i+1) + ")    " + gamesWon + " wins  -  " + username;
                listItems.add(displayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return listItems;
    }


}

