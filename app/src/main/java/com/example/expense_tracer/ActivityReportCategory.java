package com.example.expense_tracer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.slider.LabelFormatter;

import java.util.ArrayList;

public class ActivityReportCategory extends AppCompatActivity {

    String label = "";
    ArrayList<Float> valueList = new ArrayList<>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();
    RadioButton radBtnIncome;
    RadioButton radBtnExpense;
    String message = "Please select what data you want to display";
    Button showBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_category);
        BarChart barChart = findViewById(R.id.barchart);

        SQLiteDatabase sqlDB = getApplicationContext().openOrCreateDatabase("ExpenseTacer.db",
                Context.MODE_PRIVATE, null);

        radBtnIncome = findViewById(R.id.radBtnShowIncome);
        radBtnExpense = findViewById(R.id.radBtnShowExpense);
        showBtn = findViewById(R.id.btnShowGraph);

        showBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                valueList.clear();
                entries.clear();
                labelList.clear();
                barChart.clear();
                barChart.invalidate();
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(null);
                YAxis leftAxis = barChart.getAxisLeft();
                if (radBtnIncome.isChecked() == false && radBtnExpense.isChecked() == false){

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (radBtnIncome.isChecked() == true){
                    String query = "SELECT SUM(income_amount) as total,income_type FROM INCOME group by income_type;";
                    Cursor curs = sqlDB.rawQuery(query, null);

                    curs.moveToFirst();
                    while (true){
                        valueList.add(Float.parseFloat(curs.getString(curs.getColumnIndex("total"))));
                        labelList.add(curs.getString(curs.getColumnIndex("income_type")));
                        curs.moveToNext();
                        if (curs.isAfterLast()){
                            break;
                        }
                    }

                    for (int i = 0; i < valueList.size(); i++){
                        BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
                        entries.add(barEntry);
                    }

                    xAxis.setGranularity(1f);
                    xAxis.setDrawGridLines(false);
                    xAxis.setLabelRotationAngle(-45);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setAxisMaximum(entries.size());
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelList));
                    BarDataSet barDataSet = new BarDataSet(entries, "Income");
                    barDataSet.setColor(Color.rgb(134, 202, 239));
                    barDataSet.setValueTextSize(10f);

                    leftAxis.removeAllLimitLines();
                    leftAxis.setTypeface(Typeface.DEFAULT);
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setTextColor(Color.BLACK);
                    leftAxis.setDrawGridLines(false);
                    barChart.getAxisRight().setEnabled(false);

                    BarData data = new BarData(barDataSet);
                    barChart.setData(data);
                    barChart.invalidate();

                } else if (radBtnExpense.isChecked() == true){

                    String queryy = "SELECT SUM(expense_amount) as total,expense_type FROM EXPENSE group by expense_type;";
                    Cursor curs = sqlDB.rawQuery(queryy, null);
                    curs.moveToFirst();
                    while (true){
                        valueList.add(Float.parseFloat(curs.getString(curs.getColumnIndex("total"))));
                        labelList.add(curs.getString(curs.getColumnIndex("expense_type")));
                        curs.moveToNext();
                        if (curs.isAfterLast()){
                            break;
                        }
                    }

                    for (int i = 0; i < valueList.size(); i++){
                        BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
                        entries.add(barEntry);
                    }

                    xAxis.setGranularity(1f);
                    xAxis.setDrawGridLines(false);
                    xAxis.setLabelRotationAngle(-45);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setAxisMaximum(entries.size());
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelList));
                    BarDataSet barDataSet = new BarDataSet(entries, "Expense");
                    barDataSet.setColor(Color.rgb(247, 133, 148));
                    barDataSet.setValueTextSize(10f);


                    leftAxis.removeAllLimitLines();
                    leftAxis.setTypeface(Typeface.DEFAULT);
                    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                    leftAxis.setTextColor(Color.BLACK);
                    leftAxis.setDrawGridLines(false);
                    barChart.getAxisRight().setEnabled(false);

                    BarData data = new BarData(barDataSet);
                    barChart.setData(data);
                    barChart.invalidate();
                }
            }
        });
    }
}
