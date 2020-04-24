package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendAdder extends Fragment {

    private AdderCommunicator callback;
    private Button addFriendButton;
    private TextView friendNameView;

    public FriendAdder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_adder, container, false);
        addFriendButton = view.findViewById(R.id.button_add_friend);
        friendNameView = view.findViewById(R.id.text_area_friend_name);

        setupListeners();

        return view;
    }

    private void setupListeners() {
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Toast.LENGTH_SHORT;

                String friendName = friendNameView.getText().toString();
                if (callback.onAddFriend(friendName)) {
                    MainActivity.fragmentManager.popBackStackImmediate();
                    Toast toast = Toast.makeText(v.getContext(),
                            "Friend Request Added", duration);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Failed To Send Friend Request", duration);
                    toast.show();
                }

            }
        });

    }

    //This allows the main activity to share an instance for communication
    public void setCommunicator(AdderCommunicator callback) {
        this.callback = callback;
    }

    public interface AdderCommunicator {
        boolean onAddFriend(String friendName);
    }
}
