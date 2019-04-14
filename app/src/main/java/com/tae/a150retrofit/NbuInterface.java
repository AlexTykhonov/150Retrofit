package com.tae.a150retrofit;

import java.util.ArrayList;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NbuInterface {


        @GET("exchange?json")
        Observable<ArrayList<PojoVal>> getNbuData();
    }

