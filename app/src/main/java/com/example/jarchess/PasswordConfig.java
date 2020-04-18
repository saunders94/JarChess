package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PasswordConfig extends Fragment {

    private ConfigCommunicator callback;
    private Button changePasswordButton;
    private TextView originalPasswordArea;
    private TextView newPasswordArea;
    private TextView verifyNewPasswordArea;


    public PasswordConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_password_config, container, false);
        changePasswordButton = view.findViewById(R.id.button_change_password);
        originalPasswordArea = view.findViewById(R.id.text_area_original);
        newPasswordArea = view.findViewById(R.id.text_area_new);
        verifyNewPasswordArea = view.findViewById(R.id.text_area_verify_new);
        setupListeners();


        return view;
    }

    private void setupListeners() {
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Toast.LENGTH_SHORT;
                if (validate()) {
                    FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                    MainMenu mainMenu = new MainMenu();
                    transaction.replace(R.id.fragmentHole, mainMenu);
                    transaction.addToBackStack("MainMenu");
                    transaction.commit();
                    Toast toast = Toast.makeText(getContext(), "The password was successfully changed", duration);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Password change failed", duration);
                    toast.show();
                }

            }
        });

    }

    private boolean validate() {
        int duration = Toast.LENGTH_SHORT;
        boolean success = false;
        String oldPass = originalPasswordArea.getText().toString();
        String newPass = newPasswordArea.getText().toString();
        String validatePass = verifyNewPasswordArea.getText().toString();


        if (newPass.equals(validatePass)) {
            if (newPass.equals((oldPass))) {
                Toast toast = Toast.makeText(getContext(), "The new password can't match the old one", duration);
                toast.show();
            } else {
                success = callback.onPasswordChange(oldPass, newPass);
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "The new passwords don't match", duration);
            toast.show();
        }
        return success;
    }

    //This allows the main activity to share an instance for communication
    public void setCommunicator(ConfigCommunicator callback) {
        this.callback = callback;
    }

    public interface ConfigCommunicator {
        boolean onPasswordChange(String oldPass, String newPass);
    }
}