package com.example.jarchess;


import com.example.jarchess.online.usermanagement.Account;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProfileSignIn.SignInCommunicator,
        ProfileMenu.signOutCommunicator, FriendSelector.FriendSelectorCommunicator,
        Leaderboard.LeaderboardCommunicator, PasswordConfig.ConfigCommunicator,
        FriendManager.FriendManagerCommunicator, FriendAdder.AdderCommunicator,
        AvatarSelection.AvatarCommunicator {




    public static FragmentManager fragmentManager;
    private FriendSelector friendSelector;
    private AvatarSelection avatarSelection;
    //I made signonStatus more visible to get rid of the other redundant variable: loggedIn
    private boolean signonStatus;
    private int unseenNotificationQuantity;
    private MenuItem notificationItem;
    private MenuItem avatarIcon;
    private TextView usernameLabel;
    private TextView unseenNotificationView;
    private String TAG = "MainActivity";

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // cancel matchmaking
        //OnlineMatchMaker.getInstance().cancel();
    }


    @Override//This sets up fragment communicators
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ProfileSignIn) {
            ProfileSignIn signInFragment = (ProfileSignIn) fragment;
            signInFragment.setCommunicator(this);
        } else if (fragment instanceof ProfileMenu) {
            ProfileMenu profileMenuFragment = (ProfileMenu) fragment;
            profileMenuFragment.setCommunicator(this);
        } else if (fragment instanceof FriendSelector) {
            friendSelector = (FriendSelector) fragment;
            friendSelector.setCommunicator(this);
        } else if (fragment instanceof Leaderboard) {
            Leaderboard leaderboard = (Leaderboard) fragment;
            leaderboard.setCommunicator(this);
        } else if (fragment instanceof PasswordConfig) {
            PasswordConfig passwordConfig = (PasswordConfig) fragment;
            passwordConfig.setCommunicator(this);
        } else if (fragment instanceof FriendManager) {
            FriendManager friendManager = (FriendManager) fragment;
            friendManager.setCommunicator(this);
        } else if (fragment instanceof FriendAdder) {
            FriendAdder friendAdder = (FriendAdder) fragment;
            friendAdder.setCommunicator(this);
        } else if (fragment instanceof AvatarSelection) {
            avatarSelection = (AvatarSelection) fragment;
            avatarSelection.setCommunicator(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CharSequence usernameDefault = "Logged Out";
        usernameLabel = findViewById(R.id.toolbarTextView);
        usernameLabel.setText(usernameDefault);
        signonStatus = false;

        unseenNotificationQuantity = 3;

        fragmentManager = getSupportFragmentManager();
        setupListeners();
        setupToolbar();


        //Load the StartScreen fragment
        if (findViewById(R.id.fragmentHole) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //The only transaction that doesn't need to be backstack'd
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

            if (signonStatus && !tag.equals("ProfileMenu") && !tag.equals("ProfileSignIn")) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                ProfileMenu profileMenu = new ProfileMenu();
                transaction.replace(R.id.fragmentHole, profileMenu);

                transaction.addToBackStack("ProfileMenu");
                transaction.commit();

            } else if (!signonStatus && !tag.equals("ProfileSignIn")) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                ProfileSignIn profileSignIn = new ProfileSignIn();
                transaction.replace(R.id.fragmentHole, profileSignIn);

                transaction.addToBackStack("ProfileSignIn");
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
        avatarIcon = menu.findItem(R.id.profile_menu);

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


    public void setUnseenNotificationQuantity(int unseenNotificationQuantity) {
        this.unseenNotificationQuantity = unseenNotificationQuantity;
    }


    @Override
    //use this method to get login details, return true if login succeeded
    public boolean onLogin(CharSequence username, CharSequence password) {

        Account account = new Account();
        signonStatus = new Account().signin(String.valueOf(username),
                String.valueOf(password));
        if (signonStatus) {
            usernameLabel.setText(username);
            return true;
        }
        return false;
    }

    @Override
//use this method to get registration details, return true if registration succeeded
    public boolean onRegister(CharSequence username, CharSequence password) {

        boolean registerStatus = new Account().registerAccount(String.valueOf(username),
                String.valueOf(password));
        if(registerStatus){
            onLogin(username, password);
        }
        Log.i(TAG, "registerStatus: " + registerStatus);
        return registerStatus;
    }




    @Override
    public ArrayList<String> onSelectorLoad() {
        //call method to get friend list information here!
        //null can be sent to represent nothing found
        return null;
    }

    @Override
    public void onFriendMatchChosen(int index) {
        //this system assumes that the player's friend list hasn't changed since they last loaded
        //the friend selector page
        System.out.println("The chosen index was " + index);
    }


    public void onFriendMatchPaired() {
        friendSelector.proceedToMatch();
    }



    @Override
    public ArrayList<String> onLeaderboardUpdate(int criteriaType) {
        //call a method that gets leaderboard arraylist here
        //null can be sent to represent nothing found
        return null;
    }

    @Override
    public boolean onPasswordChange(String oldPass, String newPass) {
        //put something in here that changes teh password
        return true;
    }

    @Override
    public ArrayList<String> onManagerLoad() {
        return null;
    }
    
    @Override
    public boolean onLogout() {
        usernameLabel.setText("Logged Out");

        boolean logoutStatus = new Account().signout(JarAccount.getInstance().getName(),
                JarAccount.getInstance().getSignonToken());
        return logoutStatus;
    }


    @Override
    public boolean onRemoveFriend(int index) {
        return false;
    }

    @Override
    public boolean onAddFriend(String friendName) {
        return false;
    }

    @Override
    public boolean onAvatarSelected(int index) {
        avatarIcon.setIcon(avatarSelection.getAvatarArray()[index]);
        return true;
    }
}