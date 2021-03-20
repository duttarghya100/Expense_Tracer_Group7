package com.example.expense_tracer;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class IncomeFragment extends Fragment {

    private DatabaseReference mIncomeDatabse;
    private RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    List<Data> incomeList = new ArrayList<>();
    private TextView incomeTotalSum;
    private FirebaseAuth mAuth;

    public IncomeFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getTasks();
        View myview = inflater.inflate(R.layout.fragment_income, container, false);
        recyclerView=myview.findViewById(R.id.recycler_id_income);
        RecyclerViewAdapter incomeRecyclerViewAdapter=new RecyclerViewAdapter(incomeList,getActivity(),sqLiteDatabase);
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

    public void onStart() {



        super.onStart();
    }

    public void getTasks() {
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


















