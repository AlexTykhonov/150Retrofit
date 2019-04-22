package com.tae.a150retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {
    PojoVal pojoParcel;
    String currClick;
    Double valCur;
    Retrofit retrofit;
    RetrofitClient retrofitClient;
    Date today;
    Date yesterday;
    Date datebeforeyesterday;

    ArrayList<PojoVal> pojoVals = new ArrayList<PojoVal>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Bundle bundle = getIntent().getExtras();
        retrofit= retrofitClient.callRetrofit();
        NbuInterface nbuInterface = retrofit.create(NbuInterface.class);

        if (bundle!=null)
            pojoParcel = bundle.getParcelable("Parcel");
        System.out.println(pojoParcel.getCc());
        currClick = pojoParcel.getCc();
        System.out.println(pojoParcel.getRate());
        valCur = pojoParcel.getRate();

        today = new Date();
        DateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        System.out.println(dateformat.format(today));

        // dates~~~~~~~~~

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        yesterday= calendar.getTime();
        System.out.println(dateformat.format(yesterday)+ "THIS IS YESTERDAY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        calendar.add(Calendar.DATE, -1);
        datebeforeyesterday= calendar.getTime();
        System.out.println(dateformat.format(datebeforeyesterday)+ "THIS IS DATE BEFORE YESTERDAY ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        //dates~~~~~~~~~

        //Загрузка данных на сегодня
        nbuInterface.getHistory(currClick,dateformat.format(today),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        //Загрузка данных на вчера
        nbuInterface.getHistory(currClick, dateformat.format(yesterday),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        //Загрузка данных на позавчера
        nbuInterface.getHistory(currClick, dateformat.format(datebeforeyesterday),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        System.out.println(pojoParcel.getRate()+ " this is the currency rate for chart!!!!");
    }

    public void createGraphView () {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 1),
                new DataPoint(2, 2),
                new DataPoint(3, 3)
        });
        series.appendData(new DataPoint(2,2), true,10);
        graph.addSeries(series);
        System.out.println(pojoVals.size()+"   SIZE! ! ! !!!!  ! ! ! ! ++++++++++++++++++++++++++++++++++++++");
    }

    public void handleResults(ArrayList<PojoVal> pojoNbu) {
        if (pojoNbu != null ) {
            Toast toast = Toast.makeText(this,pojoNbu.toString(),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0 );
            System.out.println(pojoNbu.get(0).toString()+"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ POJO SIZE");

            pojoVals.add(pojoNbu.get(0));

            toast.show();

            createGraphView();

        } else
            {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void handleError(Throwable t){
        System.out.println(t+"!!!!!!!!!!!!!!!  ERROR  !!!!!!!!!!!!!!");
    }
}


// создать новый проект с помощью графью в котором данные выводятся в реальном времени (рандом)
// взять из примера и поиграть с методами