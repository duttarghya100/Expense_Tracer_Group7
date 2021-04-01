package com.example.expense_tracer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ActivityReportCategory extends AppCompatActivity {

    float incomeAmount = 0;
    float expenseAmount = 0;
    String incomeType;
    String expenseType;
    String label = "";


    ArrayList<Float> valueList = new ArrayList<Float>();
    ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_category);
        BarChart barChart = findViewById(R.id.barchart);

        SQLiteDatabase sqlDB = getApplicationContext().openOrCreateDatabase("ExpenseTacer.db",
                Context.MODE_PRIVATE, null);
        String query = "SELECT * FROM INCOME;";
        Cursor curs = sqlDB.rawQuery(query, null);


        curs.moveToFirst();
        //if (curs != null){
        //    while(!curs.isAfterLast()){
                valueList.add(Float.parseFloat(curs.getString(2)));
                label = curs.getString(1);
        //    }
        //}

        for (int i = 0; i < valueList.size(); i++){
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setValueTextSize(10f);
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();

    }
}