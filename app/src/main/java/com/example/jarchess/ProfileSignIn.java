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


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSignIn extends Fragment {

    private SignInCommunicator callback;
    private Button loginButton;
    private Button registerButton;
    private TextView usernameArea;
    private TextView passwordArea;


    public ProfileSignIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_sign_in, container, false);
        loginButton = view.findViewById(R.id.button_login);
        registerButton = view.findViewById(R.id.button_register);
        usernameArea = view.findViewById(R.id.usernameTextArea);
        passwordArea = view.findViewById(R.id.passwordTextArea);

        setupListeners();


        return view;
    }

    private void setupListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Toast.LENGTH_SHORT;

                if (callback.onLogin(usernameArea.getText(), passwordArea.getText())) {
                    MainActivity.fragmentManager.popBackStackImmediate();

                    Toast toast = Toast.makeText(v.getContext(), "Login Successful", duration);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Login Failed", duration);
                    toast.show();
                    //We could probably go more in-depth for why the login failed

                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Toast.LENGTH_SHORT;

                if (callback.onRegister(usernameArea.getText(), passwordArea.getText())) {
                    MainActivity.fragmentManager.popBackStackImmediate();

                    Toast toast = Toast.makeText(v.getContext()
                            , "Registration Successful, Logged In", duration);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Registration Failed", duration);
                    toast.show();
                    //We could probably go more in-depth for why the registration failed

                }
            }
        });

    }

    //This allows the main activity to share an instance for communication
    public void setCommunicator(SignInCommunicator callback) {
        this.callback = callback;
    }

    public interface SignInCommunicator {
        boolean onLogin(CharSequence username, CharSequence password);
        boolean onRegister(CharSequence username, CharSequence password);
    }

}