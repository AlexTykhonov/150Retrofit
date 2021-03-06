package com.tae.vkursi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {


    Date today;
    Retrofit retrofit;
    RecyclerView recyclerView;
    PostsAdapter recyclerViewAdapter;
    RetrofitClient retrofitClient;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    recyclerView = findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewAdapter = new PostsAdapter(this);
    recyclerView.setAdapter(recyclerViewAdapter);

    // date
        today = new Date();
        DateFormat dateformat = new SimpleDateFormat("dd.MM.YY");
        System.out.println(dateformat.format(today));
        data = findViewById(R.id.datatoday);
        String t= dateformat.format(today);
        data.setText(t);

        Dispatcher dispatcher=new Dispatcher();
        dispatcher.setMaxRequests(3);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        client.dispatcher().setMaxRequestsPerHost(3);

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
        for (int i=0; i<pojoNbu.size();i++) {
            if (pojoNbu.get(i).getR030()== 840) {
                pojoNbu.get(i).setPriority(1);
            }
            else if (pojoNbu.get(i).getR030()==978) {
                pojoNbu.get(i).setPriority(2);
                }

            else if (pojoNbu.get(i).getR030()==826) {
                pojoNbu.get(i).setPriority(3);
            }
            else if (pojoNbu.get(i).getR030()==985) {
                pojoNbu.get(i).setPriority(4);
            }
            else
                    {pojoNbu.get(i).setPriority(5);}
        }

        class PriorityComparator implements Comparator <PojoVal> {
            @Override
            public int compare(PojoVal o1, PojoVal o2) {
                return o1.compareTo(o2);
            }
        }

            Collections.sort(pojoNbu,new PriorityComparator());

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~=================================OUR FULL LIST"+pojoNbu);
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

