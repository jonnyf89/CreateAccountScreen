package com.ditkevinstreet.createaccountscreen.Remote;

import com.ditkevinstreet.createaccountscreen.NotificationStuff.DataSender;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.MyResponse;
import com.ditkevinstreet.createaccountscreen.NotificationStuff.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Admin on 12/01/2018.
 */

public interface APIDataService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAG82uixE:APA91bG9cTYAPK-nKnpncxQNuDpYiy0B7Omzv2rO5KJOgUvPY7JN1iALUQRbjFxX8LYOvXFNjzRwef1Mf9ctGvRZmUOUESK5D0UT8wKmYBztU4Q_K3M2lEtiaVahx3fFMYrOlPqmvqKD"
    })
    @POST("fcm/send")
    Call<MyResponse> sendDataMessage(@Body DataSender body);
}
