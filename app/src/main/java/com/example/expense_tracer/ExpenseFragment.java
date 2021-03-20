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


public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    List<Data> expenseList = new ArrayList<>();
    private FirebaseAuth mAuth;

    private TextView expenseSumResult;
    private DatabaseReference mExpenseDatabse;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpenseFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getTasks();
        View myview= inflater.inflate(R.layout.fragment_expense, container, false);
        recyclerView=myview.findViewById(R.id.recycler_id_expense);
        RecyclerViewAdapter expenseRecyclerViewAdapter=new RecyclerViewAdapter(expenseList,getActivity(),sqLiteDatabase);
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

    public void getTasks() {
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