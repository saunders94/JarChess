package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        setupToolbar();


        //Load the StartScreen fragment
        if (findViewById(R.id.fragmentHole) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            StartPage start = new StartPage();
            transaction.add(R.id.fragmentHole, start);
            transaction.commit();

        }
    }

    private void setupToolbar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            System.out.println("Toolbar couldn't be found!");
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }


}
