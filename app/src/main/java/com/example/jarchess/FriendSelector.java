package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jarchess.jaraccount.JarAccount;
import com.example.jarchess.online.JSONCompiler.JSONAccount;
import com.example.jarchess.online.networking.DataSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class FriendSelector extends Fragment {

    private FriendSelectorCommunicator callback;
    private ArrayAdapter<String> listAdapter;
    private Button requestMatchButton;
    private ListView friendList;
    private int indexOfLastSelected;
    private boolean listIsEmpty;
    private ArrayList<String> supposedlyEmptyList;
    private ArrayList<String> friendsList;
    private String TAG = "FriendSelector";


    public FriendSelector() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_selector, container, false);
        supposedlyEmptyList = new ArrayList<>();
        populateList();

        requestMatchButton = view.findViewById(R.id.button_request_match);
        friendList = view.findViewById(R.id.list_friend);
        indexOfLastSelected = -1;

        //this is only loading friends once, we may need a refresh button
        ArrayList<String> friendInfo = callback.onSelectorLoad();
        listAdapter = new ArrayAdapter<>
                (view.getContext(), R.layout.list_item_style,supposedlyEmptyList);

        if(friendInfo == null){
            listIsEmpty = true;
        } else {
            listAdapter.clear();
            listAdapter.addAll(friendInfo);
            listIsEmpty = false;
        }

        friendList.setAdapter(listAdapter);
        setupListeners();

        return view;
    }

    private void setupListeners() {
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexOfLastSelected = position;
            }
        });

        requestMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listIsEmpty) {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Cannot Select From Empty List", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (indexOfLastSelected < 0) {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Please Select a Friend", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String name = friendsList.get(indexOfLastSelected);
                    callback.onFriendMatchChosen(name);
                }
            }
        });

    }

    public void proceedToMatch() {
        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
        MatchSettings matchSettings = new MatchSettings();
        transaction.replace(R.id.fragmentHole, matchSettings);

        transaction.addToBackStack("FRIEND");
        transaction.commit();
    }


    public void setCommunicator(FriendSelectorCommunicator callback) {
        this.callback = callback;
    }

    public interface FriendSelectorCommunicator {
        ArrayList<String> onSelectorLoad();
        void onFriendMatchChosen(String name);
    }

    private void populateList() {

        supposedlyEmptyList = new ArrayList<>();
        friendsList = new ArrayList<>();
        //supposedlyEmptyList.add("No Friends Were Found");
        JSONObject requestObject = new JSONObject();
        JSONObject data = null;
        JSONObject user = null;

        DataSender sender = new DataSender();

        try {
            requestObject = sender.send(new JSONAccount().getFriendsList(JarAccount.getInstance().getName()));
            Log.i(TAG, requestObject.toString());
            data = new JSONObject(requestObject.getString("friends"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        int count = 0;
        try {
            count = Integer.parseInt(requestObject.getString("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < count; i++) {

            try {
                user = new JSONObject(data.getString("friend" + i));
                String username = user.getString("username");
                String displayString = (i + 1) + ")    " + username;
                friendsList.add(username);
                supposedlyEmptyList.add(displayString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}

