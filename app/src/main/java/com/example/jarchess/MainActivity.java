package com.example.jarchess;

import com.example.jarchess.online.OnlineMatchMaker;
import com.example.jarchess.online.usermanagement.Account;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements ProfileSignIn.signInCommunicator,
    ProfileMenu.signOutCommunicator {




    public static FragmentManager fragmentManager;

    private boolean loggedIn;
    private int unseenNotificationQuantity;
    private MenuItem notificationItem;
    private TextView usernameLabel;
    private TextView unseenNotificationView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // cancel matchmaking
        OnlineMatchMaker.getInstance().cancel();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ProfileSignIn) {
            ProfileSignIn signInFragment = (ProfileSignIn) fragment;
            signInFragment.setCommunicator(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CharSequence usernameDefault = "Logged Out";
        usernameLabel = findViewById(R.id.toolbarTextView);
        usernameLabel.setText(usernameDefault);
        loggedIn = false;//update this later to check storage


        unseenNotificationQuantity = 3;

        fragmentManager = getSupportFragmentManager();
        setupListeners();
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

    private void setupListeners() {
        usernameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginAssociatedPage();
            }
        });
    }

    private void openLoginAssociatedPage() {
        FragmentManager.BackStackEntry backEntry = fragmentManager
                .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String tag = backEntry.getName();

        try {

            if (loggedIn && !(tag.equals("pro"))) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                ProfileMenu profileMenu = new ProfileMenu();
                transaction.replace(R.id.fragmentHole, profileMenu);

                transaction.addToBackStack("pro");
                transaction.commit();

            } else if (!tag.equals("sig")) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                ProfileSignIn profileSignIn = new ProfileSignIn();
                transaction.replace(R.id.fragmentHole, profileSignIn);

                transaction.addToBackStack("sig");
                transaction.commit();

            }


        } catch (NullPointerException e) {
            System.out.println("null must've been added to backStack");
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        notificationItem = menu.findItem(R.id.notification_menu);

        if (unseenNotificationQuantity <= 0) {
            notificationItem.setActionView(null);

        } else {
            notificationItem.setActionView(R.layout.notification_badge);
            View view = notificationItem.getActionView();
            unseenNotificationView = view.findViewById(R.id.notification_quantity);
            unseenNotificationView.setText(String.valueOf(unseenNotificationQuantity));

            //This has to be included so the icon with notification badge can be interacted with
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(notificationItem);
                }
            });

        }

        return true;
    }

    @Override
//use this method to get login details, return true if login succeeded
    public boolean onLogin(CharSequence username, CharSequence password) {

        //if(call to method that does logging in) {
        boolean signonStatus = new Account().signin(String.valueOf(username),
                String.valueOf(password));
        if (signonStatus) {
            usernameLabel.setText(username);
            loggedIn = true;
            return true;
        }
        return false;
    }

    @Override
//use this method to get registration details, return true if registration succeeded
    public boolean onRegister(CharSequence username, CharSequence password) {

        //if(call to method that does registration) {
        onLogin(username, password);

        return true;
        //} else {
        //return false;
        //}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.notification_menu: {
                item.setActionView(null);
                return true;

            } case R.id.profile_menu: {
                openLoginAssociatedPage();

                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void resetUnseenNotificationQuantity() {
        setUnseenNotificationQuantity(0);
    }



    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ProfileSignIn) {
            ProfileSignIn signInFragment = (ProfileSignIn) fragment;
            signInFragment.setCommunicator(this);
        } else if (fragment instanceof ProfileMenu) {
            ProfileMenu profileMenuFragment = (ProfileMenu) fragment;
            profileMenuFragment.setCommunicator(this);
        }
    }

    @Override
    public boolean onLogout() {
        usernameLabel.setText("Logged Out");
        loggedIn = false;
        return true;
    }


    public void setUnseenNotificationQuantity(int unseenNotificationQuantity) {
        this.unseenNotificationQuantity = unseenNotificationQuantity;
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
}
