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


public class AvatarSelection extends Fragment {

    private AvatarCommunicator callback;
    private ListView avatarList;
    private Button selectButton;
    private String[] avatarNameArray;
    private int[] avatarArray;
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
        avatarNameArray = new String[10];
        avatarArray = new int[10];
        //demo for assigning avatar selections
        for (int i = 0; i < 10; i++) {
            avatarNameArray[i] = "Honey Combs";
            avatarArray[i] = R.drawable.honeycomb;
        }

        setupListeners();
        AvatarAdapter avatarAdapter = new AvatarAdapter(view.getContext(),avatarNameArray, avatarArray);
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
                    if (callback.onAvatarSelected(indexOfLastSelected)) {
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

    public int[] getAvatarArray() {
        return avatarArray;
    }

    //This allows the main activity to share an instance for communication
    public void setCommunicator(AvatarCommunicator callback) {
        this.callback = callback;
    }

    public interface AvatarCommunicator {
        boolean onAvatarSelected(int index);
    }
}
