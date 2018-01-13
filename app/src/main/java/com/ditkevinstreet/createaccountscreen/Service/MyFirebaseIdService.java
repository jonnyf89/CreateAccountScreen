package com.ditkevinstreet.createaccountscreen.Service;

import android.util.Log;

import com.ditkevinstreet.createaccountscreen.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Admin on 11/01/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Common.currentToken = refreshedToken;

        Log.e("MyToken",  Common.currentToken);

    }
}
