package com.tae.a150retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
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
import com.jjoe64.graphview.Viewport;


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
    GraphView graph;
    DataPoint[] dataPoints = new DataPoint[3];
    LineGraphSeries<DataPoint> series;

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

        series = new LineGraphSeries<DataPoint>();
        graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(series);

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
        //Загрузка данных на позавчера

        nbuInterface.getHistory(currClick, dateformat.format(datebeforeyesterday),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        //Загрузка данных на вчера
        nbuInterface.getHistory(currClick, dateformat.format(yesterday),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        //Загрузка данных на сегодня
        nbuInterface.getHistory(currClick,dateformat.format(today),"json").subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);

        Toast.makeText(this,pojoParcel.getCc()+ " - this is the currency rate",Toast.LENGTH_LONG).show();
    }

    public void createGraphView (DataPoint dataPoint) {
        series.appendData(dataPoint, false, 3);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Дати");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        DateFormat dateformat1 = new SimpleDateFormat("dd.MM");

        staticLabelsFormatter.setHorizontalLabels(new String[] {
                dateformat1.format(datebeforeyesterday),
                dateformat1.format(yesterday),
                dateformat1.format(today)});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    public void handleResults(ArrayList<PojoVal> pojoNbu) {
        if (pojoNbu != null ) {
            int j=0;
            PojoVal pojoVal = pojoNbu.get(j++);
            pojoVals.add(pojoNbu.get(0));
            int i = 0;
            DataPoint dataPoint = new DataPoint(pojoVals.size(), pojoVal.getRate());
            createGraphView(dataPoint);
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