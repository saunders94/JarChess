package com.example.jarchess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class FriendSelector extends Fragment {

    private FriendSelectorCommunicator callback;
    private ArrayAdapter<String> listAdapter;
    private Button requestMatchButton;
    private ListView friendList;
    private int indexOfLastSelected;
    private boolean listIsEmpty;

    public FriendSelector() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_selector, container, false);
        ArrayList<String> supposedlyEmptyList = new ArrayList<>();
        supposedlyEmptyList.add("No Friends Were Found");

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
                    callback.onFriendMatchChosen(indexOfLastSelected);
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
        void onFriendMatchChosen(int index);
    }


}

