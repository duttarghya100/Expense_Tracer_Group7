package com.example.expense_tracer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
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
    float incomeTotal;
    float expenseTotal;
    ArrayList<Entry> dataValues1 = new ArrayList<>();
    ArrayList<Entry> dataValues2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_timeline);

        lineChart = (LineChart) findViewById(R.id.lineChart);
        //Get Database
        SQLiteDatabase sqlDB = getApplicationContext().openOrCreateDatabase("ExpenseTacer.db",
                   Context.MODE_PRIVATE, null);
        //Retrieving income/expense VALUE and income/expense DATE
        String getIncome = "SELECT * FROM INCOME, EXPENSE;";
        Cursor curs = sqlDB.rawQuery(getIncome, null);
        dataValues1.add(new Entry(0, 0));
        dataValues2.add(new Entry(0,0));

        if (curs != null){
            curs.moveToFirst();
            while(!curs.isAfterLast()){

                String[] incomeValues = new String[2];
                String[] expenseValues = new String[2];
                incomeValues[0] = curs.getString(2);
                expenseValues[0] = curs.getString(curs.getColumnIndex("expense_amount"));
                incomeValues[1] = curs.getString(4);
                expenseValues[1] = curs.getString(curs.getColumnIndex("expense_date"));
                incomeAmount = Float.parseFloat(incomeValues[0]);
                incomeDate = incomeValues[1].substring(incomeValues[1].length() - 2);
                expenseAmount = Float.parseFloat(expenseValues[0]);
                expenseDate = expenseValues[1].substring(expenseValues[1].length() - 2);
                incomeTotal += incomeAmount;
                dataValues1.add(new Entry(Float.parseFloat(incomeDate), incomeTotal));

                expenseTotal += expenseAmount;

                dataValues2.add(new Entry(Float.parseFloat(expenseDate), incomeTotal));
                curs.moveToNext();
            }
        }

        //Sets the presets for the income AND expense graph lines to correct settings
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1, "Income");
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