package com.example.jarchess;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.jarchess.jaraccount.JarAccount;
import com.example.jarchess.match.clock.MatchClockChoice;

public class ClockChoiceSpinner {

    private static final String TAG = "ClockChoiceSpinner";
    private final Spinner spinner;
    private final TextView labelTextView;

    public ClockChoiceSpinner(View view) {
        spinner = view.findViewById(R.id.spinner_match_clock);
        labelTextView = view.findViewById(R.id.text_view_match_clock);
        SpinnerAdapter adapter = new ArrayAdapter<MatchClockChoice>(spinner.getContext(), R.layout.spinner_item_style, MatchClockChoice.values());
        spinner.setAdapter(adapter);
        Log.d(TAG, "ClockChoiceSpinner: jarAccountInt = " + JarAccount.getInstance().getPreferredMatchClock().getIntValue());
        spinner.setSelection(JarAccount.getInstance().getPreferredMatchClock().getIntValue(), true);
        Log.d(TAG, "ClockChoiceSpinner: is set to " + spinner.getSelectedItemPosition());
    }

    public MatchClockChoice getSelectedItem() {
        Object o = spinner.getSelectedItem();
        if (o instanceof MatchClockChoice) {
            return (MatchClockChoice) o;
        } else {
            throw new IllegalStateException("selected object was not a MatchClockChoice");
        }
    }

    public void setSelectedItem(MatchClockChoice matchClockChoice) {
        spinner.setSelection(matchClockChoice.getIntValue(), true);
    }

    public void hide() {
        labelTextView.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }
}
