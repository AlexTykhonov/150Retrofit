package com.tae.a150retrofit;


import android.database.Observable;

import java.util.ArrayList;

import retrofit2.http.GET;

public interface NbuInterface {


        @GET("exchange?json")
        Observable<ArrayList<PojoVal>> getNbuData();
    }

