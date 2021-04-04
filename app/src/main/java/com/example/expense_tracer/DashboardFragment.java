package com.example.expense_tracer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expense_tracer.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DashboardFragment extends Fragment {


    private FloatingActionButton fab_main_btn;

    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    private TextView income_set_result;
    private TextView expense_set_result;
    private TextView balance_set_result;
    private SQLiteDatabase sqLiteDatabase;
    // Recycler View
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    List<Data> IncomeList = new ArrayList<>();
    List<Data> ExpenseList = new ArrayList<>();

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private boolean isOpen;

    private Animation FadeOpen,FadeClose;


    public DashboardFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_dashboard, container, false);
        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        IncomeFragment incomeFragment=new IncomeFragment(sqLiteDatabase,getActivity());
        incomeFragment.getDetails();
        IncomeList=incomeFragment.incomeList;
        DashboardRecyclerViewAdapter incomeListRecyclerViewAdapter=new DashboardRecyclerViewAdapter(IncomeList,getActivity(),sqLiteDatabase);
        incomeListRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerIncome.setAdapter(incomeListRecyclerViewAdapter);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);
        ExpenseFragment expenseFragment=new ExpenseFragment(sqLiteDatabase);
        expenseFragment.getDetails();
        ExpenseList=expenseFragment.expenseList;
        DashboardRecyclerViewAdapter expenseListRecyclerViewAdapter=new DashboardRecyclerViewAdapter(ExpenseList,getActivity(),sqLiteDatabase);
        expenseListRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerExpense.setAdapter(expenseListRecyclerViewAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManager);
        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_ft_btn);
        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        //Animation..
        FadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);
        //Recycler View Connect for fragments in DAshBoard

        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);



        income_set_result = myview.findViewById(R.id.income_set_result);
       expense_set_result = myview.findViewById(R.id.expense_set_result);
       balance_set_result=myview.findViewById(R.id.balance_set_result);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               addData();
                if (isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen=false;
                }else {
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadeOpen);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen=true;
                }

            }
        });
        // showing the Income total Arghya

        double income=0;

        String incomeTotal="SELECT SUM(income_amount) FROM Income;";

        final Cursor cursorI = sqLiteDatabase.rawQuery(incomeTotal, null);

        if(cursorI.moveToFirst())
         income = cursorI.getDouble(0);
        else
            income=-1;
        cursorI.close();
        income_set_result.setText(String.valueOf(income));


        // showing the Expense total
        double expense=0;
        String expenseTotal="SELECT SUM(expense_amount) FROM Expense;";

        final Cursor cursorE = sqLiteDatabase.rawQuery(expenseTotal, null);

        if(cursorE.moveToFirst())
            expense = cursorE.getDouble(0);
        else
            expense=-1;
        cursorE.close();
        expense_set_result.setText("- "+String.valueOf(expense));

        double balance=income-expense;
        balance_set_result.setText(String.valueOf(balance));

        // Recycler view in Dashboard

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setReverseLayout(true);
        layoutManagerIncome.setStackFromEnd(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;
    }

    private void addData(){

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();


            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();

            }


        });
    }


    public void incomeDataInsert(){
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myview);
        AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);
        TextView txtTitle=myview.findViewById(R.id.incOrExpTitle);
        txtTitle.setText("INCOME DETAILS");

        EditText editAmount=myview.findViewById(R.id.amount_edit);
        EditText editType=myview.findViewById(R.id.type_edit);
        EditText editNote=myview.findViewById(R.id.note_edit);
        EditText dateText=myview.findViewById(R.id.date_edit);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(myDialog.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                LocalDate pickedDate=LocalDate.of(year, monthOfYear+1, dayOfMonth);
                                String pickedDateStr= DateTimeFormatter.ofPattern("dd-MM-yyyy").format(pickedDate);
                                dateText.setText(pickedDateStr);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=editType.getText().toString().trim();
                String stramount=editAmount.getText().toString().trim();
                String note=editNote.getText().toString().trim();
                String date=dateText.getText().toString().trim();

                if (TextUtils.isEmpty(stramount)){
                    editAmount.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(type)){
                    editType.setError("Required Field..");
                    return;
                }


                //double ourAmountInt=Double.parseDouble(stramount);
                if (TextUtils.isEmpty(note)){
                    editNote.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(date)){
                    dateText.setError("Required Field..");
                    return;
                }

                double amount=Double.parseDouble(stramount);
                ContentValues contentValues = new ContentValues(); // bundling
                contentValues.put("income_type", type);
                contentValues.put("income_amount", amount);
                contentValues.put("income_note", note);
                contentValues.put("income_date",date);

                sqLiteDatabase.insert("INCOME", null, contentValues);
                Toast.makeText(getActivity(),"Data Added",Toast.LENGTH_SHORT).show();
                dialog.dismiss();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    private void expenseDataInsert() {
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myview);
        AlertDialog dialog=myDialog.create();
        dialog.setCancelable(false);

        TextView txtTitle=myview.findViewById(R.id.incOrExpTitle);
        txtTitle.setText("EXPENSE DETAILS");

        EditText editAmount=myview.findViewById(R.id.amount_edit);
        EditText editType=myview.findViewById(R.id.type_edit);
        EditText editNote=myview.findViewById(R.id.note_edit);
        EditText dateText=myview.findViewById(R.id.date_edit);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(myDialog.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                LocalDate pickedDate=LocalDate.of(year, monthOfYear+1, dayOfMonth);
                                String pickedDateStr= DateTimeFormatter.ofPattern("dd-MM-yyyy").format(pickedDate);
                                dateText.setText(pickedDateStr);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type=editType.getText().toString().trim();
                String stramount=editAmount.getText().toString().trim();
                String note=editNote.getText().toString().trim();
                String date=dateText.getText().toString().trim();

                if (TextUtils.isEmpty(stramount)){
                    editAmount.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(type)){
                    editType.setError("Required Field..");
                    return;
                }

               // double ourAmountInt=Double.parseDouble(stramount);
                if (TextUtils.isEmpty(note)){
                    editNote.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(date)){
                    dateText.setError("Required Field..");
                    return;
                }
                double amount=Double.parseDouble(stramount);
                ContentValues contentValues = new ContentValues();
                contentValues.put("expense_type", type);
                contentValues.put("expense_amount", amount);
                contentValues.put("expense_note", note);
                contentValues.put("expense_date",date);

                sqLiteDatabase.insert("EXPENSE", null, contentValues);
                Toast.makeText(getActivity(),"Data Added",Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }




}