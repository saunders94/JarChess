package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jarchess.match.styles.avatar.PlayerAvatarStyles;


public class AvatarSelection extends Fragment {

    private AvatarCommunicator callback;
    private ListView avatarList;
    private Button selectButton;
    private int indexOfLastSelected;

    public AvatarSelection() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avatar_selection, container, false);
        indexOfLastSelected = -1;
        avatarList = view.findViewById(R.id.list_avatar);
        selectButton = view.findViewById(R.id.button_select_avatar);
//        avatarArray = new int[PlayerAvatarStyles.values().length];
        //demo for assigning avatar selections
//        for (int i = 0; i < PlayerAvatarStyles.values().length; i++) {
//            avatarArray[i] = R.drawable.honeycomb;
//        }

        setupListeners();
        AvatarAdapter avatarAdapter = new AvatarAdapter(view.getContext());
        avatarList.setAdapter(avatarAdapter);

        return view;
    }

    private void setupListeners() {
        final int duration = Toast.LENGTH_SHORT;
        avatarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indexOfLastSelected = position;
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexOfLastSelected < 0) {
                    Toast toast = Toast.makeText(v.getContext(),
                            "Please Select a Avatar", duration);
                    toast.show();
                } else {
                    if (callback.onAvatarSelected(PlayerAvatarStyles.getFromInt(indexOfLastSelected))) {
                        Toast toast = Toast.makeText(v.getContext(),
                                "Avatar Selected", duration);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(v.getContext(),
                                "Avatar Selection Failed", duration);
                        toast.show();
                    }
                }
            }
        });
    }

    //This allows the main activity to share an instance for communication
    public void setCommunicator(AvatarCommunicator callback) {
        this.callback = callback;
    }

    public interface AvatarCommunicator {
        boolean onAvatarSelected(PlayerAvatarStyles style);
    }
}
