    package com.example.expense_tracer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expense_tracer.Model.Data;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

    public class ActivityReportTimeline extends AppCompatActivity {
    LineChart lineChart;
    float incomeAmount = 0;
    float expenseAmount = 0;
    String incomeDate = "";
    String expenseDate = "";
    ArrayList<Entry> dataValues1 = new ArrayList<>();
    ArrayList<Entry> dataValues2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_timeline);
        lineChart =  findViewById(R.id.lineChart);
        SQLiteDatabase sqlDB = getApplicationContext().openOrCreateDatabase("ExpenseTacer.db",
                   Context.MODE_PRIVATE, null);

        dataValues1.add(new Entry(0, 0));
        dataValues2.add(new Entry(0,0));

            String incomeDetails = "SELECT SUM(income_amount) as total,substr(income_date, 4, 2) as month FROM INCOME group by substr(income_date, 4, 2);";
            Cursor incomeCursor = sqlDB.rawQuery(incomeDetails, null);
        if (incomeCursor != null){
            incomeCursor.moveToFirst();
            while(!incomeCursor.isAfterLast()){

                incomeAmount = Float.parseFloat(incomeCursor.getString(incomeCursor.getColumnIndex("total")));
                incomeDate = incomeCursor.getString(incomeCursor.getColumnIndex("month"));
                    dataValues1.add(new Entry(Float.parseFloat(incomeDate), incomeAmount));
                incomeCursor.moveToNext();
            }
        }

        String expenseDetails = "SELECT SUM(expense_amount) as total,substr(expense_date, 4, 2) as month FROM EXPENSE group by substr(expense_date, 4, 2);";
        Cursor expenseCursor = sqlDB.rawQuery(expenseDetails, null);
        if (expenseCursor != null){
            expenseCursor.moveToFirst();
            while(!expenseCursor.isAfterLast()){

                expenseAmount = Float.parseFloat(expenseCursor.getString(expenseCursor.getColumnIndex("total")));
                expenseDate = expenseCursor.getString(expenseCursor.getColumnIndex("month"));
                dataValues2.add(new Entry(Float.parseFloat(expenseDate), expenseAmount));
                expenseCursor.moveToNext();
            }
        }

        LineDataSet lineDataSet1 = new LineDataSet(dataValues1, "Income");
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(5);
        lineDataSet1.setColor(Color.BLUE);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        lineDataSet1.setCircleRadius(5f);
        lineDataSet1.setLineWidth(2f);
        lineDataSet1.setValueTextSize(10f);

        LineDataSet lineDataSet2 = new LineDataSet(dataValues2, "Expense");
        lineDataSet2.setColor(Color.RED);
        dataSets.add(lineDataSet2);
        lineDataSet2.setCircleRadius(5f);
        lineDataSet2.setLineWidth(2f);
        lineDataSet2.setValueTextSize(10f);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }
}