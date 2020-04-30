package com.example.jarchess;


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
import android.widget.Toast;

import com.example.jarchess.jaraccount.JarAccount;
import com.example.jarchess.match.styles.avatar.PlayerAvatarStyles;
import com.example.jarchess.online.JSONCompiler.JSONAccount;
import com.example.jarchess.online.networking.DataSender;
import com.example.jarchess.online.usermanagement.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProfileSignIn.SignInCommunicator,
        ProfileMenu.signOutCommunicator, FriendSelector.FriendSelectorCommunicator,
        Leaderboard.LeaderboardCommunicator, PasswordConfig.ConfigCommunicator,
        FriendManager.FriendManagerCommunicator, FriendAdder.AdderCommunicator,
        AvatarSelection.AvatarCommunicator {


    private static final String DEFAULT_USERNAME_LABEL = "Logged Out";
    private static final JarAccount jarAccount = JarAccount.getInstance();
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

    //(This only adds one notification at a time)
    //type 0 = new friend match request
    //type 1 = new friend request
    //type 2 = new friend added to list
    //type 3 = new avatar unlocked
    private void addNotification(int type) {
        MenuItem menuItem = null;
        switch (type) {
            case 0:
                menuItem = notificationItem.getSubMenu()
                        .findItem(R.id.notification_friend_match);
                if (!menuItem.isVisible()) {
                    menuItem.setVisible(true);
                    unseenNotificationQuantity++;
                }
                break;
            case 1:
                menuItem = notificationItem.getSubMenu()
                        .findItem(R.id.notification_friend_request);
                if (!menuItem.isVisible()) {
                    menuItem.setVisible(true);
                    unseenNotificationQuantity++;
                }
                break;
            case 2:
                menuItem = notificationItem.getSubMenu()
                        .findItem(R.id.notification_added_friend);
                if (!menuItem.isVisible()) {
                    menuItem.setVisible(true);
                    unseenNotificationQuantity++;
                }
                break;
            case 3:
                menuItem = notificationItem.getSubMenu()
                        .findItem(R.id.notification_avatar);
                if (!menuItem.isVisible()) {
                    menuItem.setVisible(true);
                    unseenNotificationQuantity++;
                }
                break;
        }

        if (unseenNotificationQuantity == 0) {
            notificationItem.setActionView(null);
        } else {
            notificationItem.setActionView(R.layout.notification_badge);
            View view = notificationItem.getActionView();
            unseenNotificationView = view.findViewById(R.id.notification_quantity);
            unseenNotificationView.setText(String.valueOf(unseenNotificationQuantity));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(notificationItem);
                }
            });
        }
    }

    @Override
    public boolean onAddFriend(String friendName) {
        JSONObject addFriend = new JSONAccount().sendFriendReq(JarAccount.getInstance().getName(),
                JarAccount.getInstance().getSignonToken(), friendName);
        DataSender sender = new DataSender();
        JSONObject responseObject = null;
        String resultStr = "";
        try {
            responseObject = sender.send(addFriend);
            resultStr = responseObject.getString("status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e3) {
            e3.printStackTrace();
            return false;
        }

        boolean result = false;
        if (resultStr.equals("success")) {
            result = true;
        }

        return result;
    }

    @Override
    public boolean onAvatarSelected(PlayerAvatarStyles style) {
        avatarIcon.setIcon(style.getAvatarStyle().getAvatarResourceID());
        jarAccount.setAvatarStyle(style);
        return true;
    }

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
        usernameLabel = findViewById(R.id.toolbarTextView);
        jarAccount.loadFromLocal(this);

        // check if the account is logged in
        signonStatus = jarAccount.isLoggedIn();
        Log.i(TAG, "onCreate: signonStatus " + signonStatus);

        // setup the username label on the toolbar
        if (signonStatus) {
            usernameLabel.setText(jarAccount.getName());
        } else {
            usernameLabel.setText(DEFAULT_USERNAME_LABEL);
        }
        setupListeners();
        setupToolbar();


        fragmentManager = getSupportFragmentManager();

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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        notificationItem = menu.findItem(R.id.notification_menu);
        avatarIcon = menu.findItem(R.id.profile_menu);

        // setup the avatar icon
        avatarIcon.setIcon(jarAccount.getAvatarStyle().getAvatarResourceID());
        unseenNotificationQuantity = 0;

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
        boolean loginStatus = false;
        int duration = Toast.LENGTH_SHORT;
        loginStatus = JarAccount.getInstance().isLoggedIn();
        switch (item.getItemId()) {

            case R.id.profile_menu: {
                openLoginAssociatedPage();
                return true;

            }
            case R.id.notification_menu: {
                item.setActionView(null);
                return true;

            }
            case R.id.notification_friend_match: {
                notificationItem.getSubMenu()
                        .findItem(R.id.notification_friend_match).setVisible(false);
                if (loginStatus) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    FriendSelector friendSelector = new FriendSelector();
                    transaction.replace(R.id.fragmentHole, friendSelector);
                    transaction.addToBackStack("FriendSelector");
                    transaction.commit();
                } else {
                    Toast.makeText(this,
                            "Login Required to play Online", duration).show();
                }
                if (unseenNotificationQuantity > 0) {
                    unseenNotificationQuantity--;
                }
                return true;
            }
            case R.id.notification_friend_request: {
                notificationItem.getSubMenu()
                        .findItem(R.id.notification_friend_request).setVisible(false);
                if (loginStatus) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    FriendManager friendManager = new FriendManager();
                    transaction.replace(R.id.fragmentHole, friendManager);
                    transaction.addToBackStack("FriendManager");
                    transaction.commit();
                } else {
                    Toast.makeText(this,
                            "Login Required to View Friend List", duration).show();
                }
                if (unseenNotificationQuantity > 0) {
                    unseenNotificationQuantity--;
                }
                return true;
            }
            case R.id.notification_added_friend: {
                notificationItem.getSubMenu()
                        .findItem(R.id.notification_added_friend).setVisible(false);
                if (loginStatus) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    FriendManager friendManager = new FriendManager();
                    transaction.replace(R.id.fragmentHole, friendManager);
                    transaction.addToBackStack("FriendManager");
                    transaction.commit();
                } else {
                    Toast.makeText(this,
                            "Login Required to View Friend List", duration).show();
                }
                if (unseenNotificationQuantity > 0) {
                    unseenNotificationQuantity--;
                }
                return true;
            }
            case R.id.notification_avatar: {
                notificationItem.getSubMenu()
                        .findItem(R.id.notification_avatar).setVisible(false);
                if (loginStatus) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    AvatarSelection avatarSelection = new AvatarSelection();
                    transaction.replace(R.id.fragmentHole, avatarSelection);
                    transaction.addToBackStack("AvatarSelection");
                    transaction.commit();
                } else {
                    Toast.makeText(this,
                            "Login Required to View Avatar Selection", duration).show();
                }
                if (unseenNotificationQuantity > 0) {
                    unseenNotificationQuantity--;
                }
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
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
    public boolean onAcceptFriendRequest(String friendName) {
        JSONObject addFriend = new JSONAccount().acceptFriendReq(JarAccount.getInstance().getName(),
                JarAccount.getInstance().getSignonToken(), friendName);
        DataSender sender = new DataSender();
        JSONObject responseObject = null;
        String resultStr = "";
        System.out.println("this onAcceptFriendRequest method is actually being used");
        try {
            responseObject = sender.send(addFriend);
            resultStr = responseObject.getString("status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e3) {
            e3.printStackTrace();
            return false;
        }

        boolean result = false;
        if (resultStr.equals("true")) {
            result = true;
        }

        return result;
    }

    @Override
    //use this method to get login details, return true if login succeeded
    public boolean onLogin(CharSequence username, CharSequence password) {


        signonStatus = jarAccount.signIn(username.toString(), password.toString());
        if (signonStatus) {
            usernameLabel.setText(username);
            ArrayList<String> friendsList = new Account().getFriendsList();
            for (int i = 0; i < friendsList.size(); i++) {
                addNotification(1);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onLogout() {
        boolean logoutStatus = jarAccount.signOut();
        signonStatus = false;
        usernameLabel.setText(DEFAULT_USERNAME_LABEL);
        avatarIcon.setIcon(jarAccount.getAvatarStyle().getAvatarResourceID());

        return logoutStatus;
    }

    @Override
    public ArrayList<String> onManagerLoad() {
        return null;
    }

    @Override
    public boolean onRemoveFriend(String friendName) {
        JSONObject removeFriend = new JSONAccount().removeFriend(JarAccount.getInstance().getName(), friendName);
        DataSender sender = new DataSender();
        JSONObject responseObject = null;
        String resultStr = "";
        try {
            responseObject = sender.send(removeFriend);
            resultStr = responseObject.getString("status");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        } catch (NullPointerException e3) {
            e3.printStackTrace();
            return false;
        }

        boolean result = false;
        if (resultStr.equals("success")) {
            result = true;
        }

        return result;
    }

    @Override
//use this method to get registration details, return true if registration succeeded
    public boolean onRegister(CharSequence username, CharSequence password) {

        boolean registerStatus = new Account().registerAccount(String.valueOf(username),
                String.valueOf(password));
        if (registerStatus) {
            onLogin(username, password);
        }
        Log.i(TAG, "registerStatus: " + registerStatus);
        return registerStatus;
    }

    @Override
    public boolean onPasswordChange(String oldPass, String newPass) {
        boolean status = new Account().changePassword(oldPass, newPass);
        return status;
    }

    @Override
    public ArrayList<String> onSelectorLoad() {
        //call method to get friend list information here!
        //null can be sent to represent nothing found
        return null;
    }

    @Override
    public void onFriendMatchChosen(String name) {
        //this system assumes that the player's friend list hasn't changed since they last loaded
        //the friend selector page

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

    public void resetUnseenNotificationQuantity() {
        setUnseenNotificationQuantity(0);
    }

    public void setUnseenNotificationQuantity(int unseenNotificationQuantity) {
        this.unseenNotificationQuantity = unseenNotificationQuantity;
    }

    private void setupListeners() {

        usernameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginAssociatedPage();
            }
        });
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