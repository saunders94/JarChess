package com.example.jarchess;

import android.util.Log;

enum RequestCode {
    FIND_RANDOM_OPPONENT_FOR_ONLINE_MATCH_REQUEST_CODE,
    FIND_FRIEND_OPPONENT_FOR_ONLINE_MATCH,
    START_MATCH;

    private static final String TAG = "RequestCode";

    public int getInt() {
        RequestCode[] values = values();

        for (int i = 0; i < values.length; i++) {
            if (values[i] == this) {
                return i;
            }
        }
        Log.wtf(TAG, JZStringBuilder.build("getInt: ", this, "is not a value of ", this.getClass().getSimpleName()));
        return -1;
    }


}
