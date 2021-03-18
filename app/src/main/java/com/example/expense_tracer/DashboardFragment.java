package com.example.expense_tracer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    private SQLiteDatabase sqLiteDatabase;

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private boolean isOpen;

    private Animation FadeOpen,FadeClose;

    FirebaseAuth mAuth;
    DatabaseReference mIncomeDatabase;
    DatabaseReference mExpenseDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DashboardFragment() {
        // Required empty public constructor
    }

    public DashboardFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View myview=inflater.inflate(R.layout.fragment_dashboard, container, false);
//        mAuth=FirebaseAuth.getInstance();
//        FirebaseUser mUser=mAuth.getCurrentUser();
//        String uId=mUser.getUid();
//        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uId);
//        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uId);


        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_ft_btn);
        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);
        FadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

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

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                String amount=editAmount.getText().toString().trim();
                String note=editNote.getText().toString().trim();
                String date=dateText.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    editAmount.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(type)){
                    editType.setError("Required Field..");
                    return;
                }


                double ourAmountInt=Double.parseDouble(amount);
                if (TextUtils.isEmpty(note)){
                    editNote.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(date)){
                    dateText.setError("Required Field..");
                    return;
                }

                ContentValues contentValues = new ContentValues();
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

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                String amount=editAmount.getText().toString().trim();
                String note=editNote.getText().toString().trim();
                String date=dateText.getText().toString().trim();

                if (TextUtils.isEmpty(amount)){
                    editAmount.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(type)){
                    editType.setError("Required Field..");
                    return;
                }

                double ourAmountInt=Double.parseDouble(amount);
                if (TextUtils.isEmpty(note)){
                    editNote.setError("Required Field..");
                    return;
                }

                if (TextUtils.isEmpty(date)){
                    dateText.setError("Required Field..");
                    return;
                }

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