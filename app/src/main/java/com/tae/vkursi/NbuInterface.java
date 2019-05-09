package com.tae.vkursi;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NbuInterface {


        @GET("exchange?json")
        Observable<ArrayList<PojoVal>> getNbuData();

        @GET("exchange?")
        Observable<ArrayList<PojoVal>> getHistory(@Query("valcode") String curr, @Query("date") String date, @Query("json") String json);
    }

