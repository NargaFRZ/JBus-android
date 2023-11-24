package com.FairuzMuhammadJBusRA.jbus_android.request;

public class UtilsApi {
    public static final String BASE_URL_API = "http://192.168.8.124:5000/";
    public static BaseApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}