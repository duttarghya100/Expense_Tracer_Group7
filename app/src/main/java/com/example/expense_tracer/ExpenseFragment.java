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

import java.util.ArrayList;
import java.util.List;


public class ExpenseFragment extends Fragment {


    private RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    List<Data> expenseList = new ArrayList<>();

    private TextView expenseSumResult;


    public ExpenseFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDetails();
        View myview= inflater.inflate(R.layout.fragment_expense, container, false);
        recyclerView=myview.findViewById(R.id.recycler_id_expense);
        ExpenseRecyclerViewAdapter expenseRecyclerViewAdapter=new ExpenseRecyclerViewAdapter(expenseList,getActivity(),sqLiteDatabase);
        expenseRecyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(expenseRecyclerViewAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        expenseSumResult=myview.findViewById(R.id.expense_txt_result);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        return myview;
    }

    public void onStart() {



        super.onStart();
        // showing the Expense total
        double expense=0;
        String expenseTotal="SELECT SUM(expense_amount) FROM Expense;";

        final Cursor cursorE = sqLiteDatabase.rawQuery(expenseTotal, null);

        if(cursorE.moveToFirst())
            expense = cursorE.getDouble(0);
        else
            expense=-1;
        cursorE.close();
        expenseSumResult.setText("- "+String.valueOf(expense));


        //
        // after populating, we need to call onclick listener on the view and inside that --- updateDataItem()
    }

    public void getDetails() {
        String expenseDetails = "SELECT * FROM EXPENSE;";
        Cursor cursor = sqLiteDatabase.rawQuery(expenseDetails, null);
        expenseList.clear();
        if(cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String ExpenseID = cursor.getString(cursor.getColumnIndex("expense_id"));
                String ExpenseType = cursor.getString(cursor.getColumnIndex("expense_type"));
                double ExpenseAmount = Double.parseDouble(cursor.getString(cursor.getColumnIndex("expense_amount")));
                String ExpenseNote = cursor.getString(cursor.getColumnIndex("expense_note"));
                String ExpenseDate = cursor.getString(cursor.getColumnIndex("expense_date"));

                Data expense = new Data(ExpenseID,ExpenseType,ExpenseAmount,ExpenseNote,ExpenseDate);
                expenseList.add(expense);
                cursor.moveToNext();
            }
        }
    }
}