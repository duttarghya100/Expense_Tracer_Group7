package com.example.expense_tracer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ActivityReportCategory extends AppCompatActivity {

    String label = "";
    ArrayList<Float> valueList = new ArrayList<Float>();
    ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
    ArrayList<String> labelList = new ArrayList<String>();
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

                if (radBtnIncome.isChecked() == false && radBtnExpense.isChecked() == false){

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else if (radBtnIncome.isChecked() == true){

                    valueList.clear();
                    entries.clear();
                    barChart.clear();
                    barChart.invalidate();

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

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setDrawLabels(false);
                    BarDataSet barDataSet = new BarDataSet(entries, label);
                    barDataSet.setColor(Color.rgb(134, 202, 239));
                    barDataSet.setValueTextSize(10f);
                    BarData data = new BarData(barDataSet);
                    barChart.setData(data);
                    barChart.invalidate();

                } else if (radBtnExpense.isChecked() == true){

                    valueList.clear();
                    entries.clear();
                    barChart.clear();
                    barChart.invalidate();

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

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setDrawLabels(false);
                    BarDataSet barDataSet = new BarDataSet(entries, label);
                    barDataSet.setColor(Color.rgb(247, 133, 148));
                    barDataSet.setValueTextSize(10f);
                    BarData data = new BarData(barDataSet);
                    barChart.setData(data);
                    barChart.invalidate();
                }
            }
        });
    }
}
