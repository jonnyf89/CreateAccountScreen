package com.ditkevinstreet.createaccountscreen;

import com.ditkevinstreet.createaccountscreen.Remote.APIDataService;
import com.ditkevinstreet.createaccountscreen.Remote.APIMessageService;
import com.ditkevinstreet.createaccountscreen.Remote.APIService;
import com.ditkevinstreet.createaccountscreen.Remote.RetrofitClient;

/**
 * Created by Admin on 11/01/2018.
 */

public class Common {
    public static String currentToken = "";

    public static String baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient (){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);

    }
    public static APIDataService getFCMDataClient (){
        return RetrofitClient.getClient(baseUrl).create(APIDataService.class);

    }

    public static APIMessageService getFCMMessageClient() {
        return RetrofitClient.getClient(baseUrl).create(APIMessageService.class);
    }
}
