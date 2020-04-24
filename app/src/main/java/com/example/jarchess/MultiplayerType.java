package com.example.jarchess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class MultiplayerType extends Fragment {

    private Button localButton;
    private Button onlineButton;

    public MultiplayerType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_multiplayer_type, container, false);
        localButton = view.findViewById(R.id.button_Local);
        onlineButton = view.findViewById(R.id.button_online);

        setupListeners();


        return view;
    }

    private void setupListeners() {
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                MatchSettings matchSettings = new MatchSettings();
                transaction.replace(R.id.fragmentHole, matchSettings);
                transaction.addToBackStack("LOCAL");
                transaction.commit();

            }
        });

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (JarAccount.getInstance().isLoggedIn()) {
                        FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
                        MatchmakingType matchmakingType = new MatchmakingType();
                        transaction.replace(R.id.fragmentHole, matchmakingType);
                        transaction.addToBackStack("matchMakingType");
                        transaction.commit();

                    } else {
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(v.getContext(), "Login Required to play Online", duration).show();
                    }
                } catch (IOException e) {
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(v.getContext(), "Cannot connect to the server. " +
                            "Please make sure you have internet access.", duration).show(); }
            }
        });
    }

//    public static class MatchMakerLauncher extends Fragment {
//
//        private OnlineMatchInfoBundle onlineMatchInfoBundle;
//        private Button cancelMatchMakingButton;
//
//        public MatchMakerLauncher() {
//        }
//
//        public void cancel() {
//            Log.d(TAG, "cancel() called");
//            Log.d(TAG, "cancel is running on thread: " + Thread.currentThread().getName());
//            OnlineMatchMaker.getInstance().cancel();
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getActivity().onBackPressed();
//                    }
//                });
//            }
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//            // start a new thread to launch the online match maker.
//            new LoggedThread(TAG, new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Log.i(TAG, "Creating onlinematchMakerBundle");
//                        onlineMatchInfoBundle = OnlineMatchMaker.getInstance().getOnlineMatchInfoBundle();
//                        Log.i(TAG, "Online match info bundle = " + onlineMatchInfoBundle);
//                        MatchBuilder.getInstance().multiplayerSetup(onlineMatchInfoBundle);
//                        final Activity activity = getActivity();
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                activity.onBackPressed();
//                            }
//                        });
//                        Intent intent = new Intent(activity, OnlineMultiplayerMatchActivity.class);
//                        startActivity(intent);
//                    } catch (OnlineMatchMaker.SearchCanceledException e) {
//                        // just get out... nothing more needs to be done;
//                    } catch (IOException e) {
//                        final String msg = "Connection Failure";
//                        if (getActivity() != null) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                        Log.e(TAG, "onCreateView's run caught: ", e);
//
//                        cancel();
//                    } catch (InterruptedException e) {
//                        Log.e(TAG, "onCreateView's run caught: ", e);
//
//                        cancel();
//                    }
//                }
//            }, "matchMakerLauncherThread").start();
//
//            // Inflate the layout for this fragment
//
//            View view = inflater.inflate(R.layout.fragment_match_maker_launcher, container, false);
//            cancelMatchMakingButton = view.findViewById(R.id.cancel_match_making_button);
//
//            cancelMatchMakingButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i(TAG, "cancel button clicked");
//                    cancel();
//                }
//            });
//
//
//            return view;
//        }
//    }


}
