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


    private Spinner sortSpinner;
    private ListView leaderboardList;
    private String TAG = "Leaderboard";

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

        ArrayList<String> listItems = getLeaderboard();

//        listItems.add("(1)    100  wins - KyleIsNeat");
//        listItems.add("(2)    45   wins - Goober03");
//        listItems.add("(3)    23   wins - earthDude21");
//        listItems.add("(4)    9    wins - kratos69");

        //getLeaderboard();

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
