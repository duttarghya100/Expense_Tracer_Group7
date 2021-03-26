package com.example.expense_tracer;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expense_tracer.Model.Data;

import java.util.ArrayList;
import java.util.List;


public class IncomeFragment extends Fragment {

    private RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    List<Data> incomeList = new ArrayList<>();
    Context context;
    private TextView incomeTotalSum;

    public IncomeFragment(SQLiteDatabase sqLiteDatabase, Context context) {
        this.context = context;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    //test pushes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDetails();
        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        recyclerView=myview.findViewById(R.id.recycler_id_income);
        IncomeRecyclerViewAdapter incomeRecyclerViewAdapter=new IncomeRecyclerViewAdapter(incomeList,context,sqLiteDatabase);
        incomeRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(incomeRecyclerViewAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        incomeTotalSum=myview.findViewById(R.id.income_txt_result);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        return myview;



    }


// populate the data here
    public void onStart() {



        super.onStart();
        // showing the Income total

        double income=0;

        String incomeTotal="SELECT SUM(income_amount) FROM Income;";

        final Cursor cursorI = sqLiteDatabase.rawQuery(incomeTotal, null);

        if(cursorI.moveToFirst())
            income = cursorI.getDouble(0);
        else
            income=-1;
        cursorI.close();
        incomeTotalSum.setText(String.valueOf(income));

        //
        // after populating, we need to call onclick listener on the view and inside that --- updateDataItem()
    }

    public void getDetails() {
        String incomeDetails = "SELECT * FROM INCOME;";
        Cursor cursor = sqLiteDatabase.rawQuery(incomeDetails, null);
        incomeList.clear();
        if(cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String IncomeID = cursor.getString(cursor.getColumnIndex("income_id"));
                String IncomeType = cursor.getString(cursor.getColumnIndex("income_type"));
                double IncomeAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("income_amount")));
                String IncomeNote = cursor.getString(cursor.getColumnIndex("income_note"));
                String IncomeDate = cursor.getString(cursor.getColumnIndex("income_date"));

                Data income = new Data(IncomeID,IncomeType,IncomeAmount,IncomeNote,IncomeDate);
                incomeList.add(income);
                cursor.moveToNext();
            }
        }

    }








}


















