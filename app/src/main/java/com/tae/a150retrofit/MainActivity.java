package com.tae.a150retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    RecyclerView recyclerView;
    PostsAdapter recyclerViewAdapter;
    RetrofitClient retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    recyclerView = findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewAdapter = new PostsAdapter(this);
    recyclerView.setAdapter(recyclerViewAdapter);



    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


    retrofit= retrofitClient.callRetrofit();
    NbuInterface nbuInterface = retrofit.create(NbuInterface.class);

        nbuInterface.getNbuData().subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResults, this::handleError);
        }

    public void handleResults(ArrayList<PojoVal> pojoNbu)
    {
        if (pojoNbu != null ) {
            System.out.println("&&*^&*^&*^*^^*&^*^*&^*&^^&*^**&^^&* THIS IS POJO NBU!!!!!    ---> "+pojoNbu);
            recyclerViewAdapter.setData(pojoNbu);

        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void handleError(Throwable t){
        System.out.println(t+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }


}

