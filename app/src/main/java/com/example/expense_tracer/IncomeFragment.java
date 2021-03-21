package com.example.expense_tracer;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    //Update edit Text.
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //button for Update and Delete
    private Button btnUpdate;
    private Button btnDelete;

    //Data item value
    private String type;
    private String note;
    private String amount;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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






    private void updateDataItem(){
        AlertDialog.Builder mydialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= LayoutInflater.from(getActivity());
        View myView=inflater.inflate(R.layout.update_data_item,null);

        edtAmount=myView.findViewById(R.id.amount_edit);
        edtType=myView.findViewById(R.id.type_edit);
        edtNote=myView.findViewById(R.id.note_edit);

        btnUpdate=myView.findViewById(R.id.btn_upd_Update);
        btnDelete=myView.findViewById(R.id.btnuPD_Delete);
        AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
            dialog.show();
    }

}


















