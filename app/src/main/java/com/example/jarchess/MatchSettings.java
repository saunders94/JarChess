package com.example.jarchess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jarchess.match.MatchBuilder;
import com.example.jarchess.match.activity.EasyAIMatchActivity;
import com.example.jarchess.match.activity.HardAIMatchActivity;
import com.example.jarchess.match.activity.LocalMultiplayerMatchActivity;
import com.example.jarchess.match.activity.OnlineMultiplayerMatchActivity;
import com.example.jarchess.match.clock.MatchClockChoice;
import com.example.jarchess.match.participant.EasyAIOpponent;
import com.example.jarchess.match.participant.HardAIOpponent;
import com.example.jarchess.testmode.TestMode;

import java.io.IOException;

import static com.example.jarchess.MainActivity.fragmentManager;


public class MatchSettings extends Fragment {
    private static final String TAG = "MatchSettings";

    private Switch speedRoundSwitch;
    private Switch enableCommitSwitch;
    private Switch alwaysPromoteToQueenSwitch;
    private Switch disablePausingSwitch;
    private ClockChoiceSpinner clockChoiceSpinner;
    private Button readyButton;

    public MatchSettings() {
        // Required empty public constructor
    }

    //this method chooses the match type selected by reviewing the backstack
    private void chooseDestination() {
        FragmentManager.BackStackEntry backEntry = fragmentManager
                .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String tag = backEntry.getName();
        try {
            switch (tag) {
                case "EASY":
                    startEasyMatch();
                    break;
                case "HARD":
                    startHardMatch();
                    break;
                case "LOCAL":
                    startLocal();
                    break;
                case "RANDOM":
                    startRandom();
                    break;
                case "FRIEND":
                    break;
                default:
                    System.err.println("Unknown match type");
            }
        } catch (NullPointerException e) {
            System.out.println("Null must've been added to the back stack");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final JarAccount jarAccount = JarAccount.getInstance();
        View view = inflater.inflate(R.layout.fragment_match_settings, container, false);

        FragmentManager.BackStackEntry backEntry = fragmentManager
                .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String tag = backEntry.getName();


        speedRoundSwitch = view.findViewById(R.id.switch_speed_round);//TODO remove this eventually


        clockChoiceSpinner = new ClockChoiceSpinner(view);
        if (tag.equals("RANDOM")) {
            clockChoiceSpinner.setSelectedItem(MatchClockChoice.CLASSIC_FIDE_MATCH_CLOCK);
            clockChoiceSpinner.hide();
        }

        enableCommitSwitch = view.findViewById(R.id.switch_enable_commit);
        enableCommitSwitch.setChecked(jarAccount.getCommitButtonClickIsRequired());
        enableCommitSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAccount.setCommitButtonClickIsRequired(enableCommitSwitch.isChecked());
            }
        });

        alwaysPromoteToQueenSwitch = view.findViewById(R.id.switch_default_to_queening);
        alwaysPromoteToQueenSwitch.setChecked(jarAccount.isAutomaticQueening());
        alwaysPromoteToQueenSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAccount.setAutomaticQueening(alwaysPromoteToQueenSwitch.isChecked());
            }
        });

        disablePausingSwitch = view.findViewById(R.id.switch_disable_pause);
        disablePausingSwitch.setChecked(jarAccount.isPausingDisabled());
        disablePausingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAccount.setPausingDisabled(disablePausingSwitch.isChecked());
            }
        });

        readyButton = view.findViewById(R.id.button_ready);

        setupListeners();
        return view;
    }

    private void saveSettings() {
        MatchBuilder.getInstance().setMatchClockChoice(clockChoiceSpinner.getSelectedItem());
    }

    private void setupListeners() {
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                chooseDestination();
            }
        });
    }

    private void startEasyMatch() {
        if (EasyAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {
            Intent intent = new Intent(getActivity(), EasyAIMatchActivity.class);
            startActivity(intent);
        } else {
            Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "We don't have this mode working yet:EASY", duration).show();
        }
    }

    private void startHardMatch() {
        if (HardAIOpponent.IS_IMPLEMENTED || TestMode.isOn()) {

            Intent intent = new Intent(getActivity(), HardAIMatchActivity.class);
            startActivity(intent);

        } else {
            Log.i(TAG, "onClick: Easy AI Opponent is not implemented");
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "We don't have this mode working yet:HARD", duration).show();
        }
    }

    private void startLocal() {
        MatchBuilder.getInstance().setMatchClockChoice(JarAccount.getInstance().getPreferredMatchClock());
        Intent intent = new Intent(getActivity(), LocalMultiplayerMatchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        Log.d(TAG, "onActivityResult is running on thread: " + Thread.currentThread().getName());
        switch (requestCode) {
            case 0:
                boolean canceled = data.getBooleanExtra("CANCELED", true);
                Log.i(TAG, "onActivityResult: canceled = " + canceled);
                if (!canceled) {
                    Intent intent = new Intent(getActivity(), OnlineMultiplayerMatchActivity.class);
                    startActivity(intent);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    private void startRandom() {
        try {
            if (JarAccount.getInstance().isLoggedIn()) {
                //start matchmaking
                Intent intent = new Intent(getActivity(), MatchMakingActivity.class);
                startActivityForResult(intent, 0);

            } else {
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getContext(), "Login Required to play Online", duration).show();
            }
        } catch (IOException e) {
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(), "Cannot connect to the server. Please make sure you have internet access.", duration).show();
        }

    }
}
