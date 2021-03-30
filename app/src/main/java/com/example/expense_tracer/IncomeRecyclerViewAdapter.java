package com.example.expense_tracer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_tracer.Model.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class IncomeRecyclerViewAdapter extends RecyclerView.Adapter<IncomeRecyclerViewAdapter.MyViewHolder> {
    List<Data> dataList;
    Context context;
    SQLiteDatabase database;
    //Update edit Text.
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;
    private EditText dateText;

    //button for Update and Delete
    private Button btnUpdate;
    private Button btnDelete;

    private String amount;
    private String type;
    private String note;
    private String date;
    double amountConv;
    private int Id;


    public IncomeRecyclerViewAdapter(List<Data> data, Context context, SQLiteDatabase database) {
        this.dataList = data;
        this.context = context;
        this.database = database;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = dataList.get(position);

            holder.setType(String.valueOf(data.getType()));
            holder.setAmount(String.valueOf(data.getAmount()));
            holder.setNote(String.valueOf(data.getNote()));
            holder.setDate(String.valueOf(data.getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id= Integer.parseInt(data.getId());

                amount= String.valueOf(data.getAmount());
                type=String.valueOf(data.getType());
                note=String.valueOf(data.getNote());
                date=String.valueOf(data.getDate());
                updateDataItem(data,Id, holder);


            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        private  void setType(String type){

            TextView mType= itemView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private  void setNote(String note){

            TextView mNote= itemView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void    setDate(String date)
        {
            TextView mDate=itemView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void    setAmount(String amount)
        {
            TextView mAmount=itemView.findViewById(R.id.amount_txt_income);
            String staamount=String.valueOf(amount);
            mAmount.setText(staamount);
        }



    }

    private void updateDataItem(Data data,int id, MyViewHolder holder){
        AlertDialog.Builder mydialog= new AlertDialog.Builder(context);
        LayoutInflater inflater= LayoutInflater.from(context);
        View myView=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myView);
        AlertDialog dialog=mydialog.create();
        dialog.setCancelable(false);
        edtAmount=myView.findViewById(R.id.amount_edit);
        edtType=myView.findViewById(R.id.type_edit);
        edtNote=myView.findViewById(R.id.note_edit);
        dateText=myView.findViewById(R.id.date_edit);
        edtAmount.setText(amount);
        edtType.setText(type);
        edtNote.setText(note);
        dateText.setText(date);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mydialog.getContext(),
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

        btnUpdate=myView.findViewById(R.id.btn_upd_Update);
        btnDelete=myView.findViewById(R.id.btnuPD_Delete);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount=edtAmount.getText().toString().trim();
                amountConv=Double.parseDouble(amount);
                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();
                date=dateText.getText().toString().trim();
                updateIncomeDetails(id);
                if (data.getId().equals(String.valueOf(Id))){
                    data.setAmount(amountConv);
                    data.setType(type);
                    data.setNote(note);
                    data.setDate(date);
                    dataList.set(holder.getAdapterPosition(), data);
                    notifyDataSetChanged();
                }

                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteIncomeDetails(id);
                if (data.getId().equals(String.valueOf(Id))){
                    dataList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updateIncomeDetails(int id) {
        String incomeDetails = "SELECT * FROM INCOME where income_id=?;";
        try {
            Cursor cursor = database.rawQuery(incomeDetails, new String[]{String.valueOf(id)});
            if(cursor != null) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    ContentValues val=new ContentValues();
                    val.put("income_type",type);
                    val.put("income_amount",amountConv);
                    val.put("income_note",note);
                    val.put("income_date",date);
                    database.update("INCOME",val,"income_id=?",new String[]{String.valueOf(id)});

                    Log.d("ExpenseTracer","Updated details for "+Id);
                    cursor.moveToNext();
                }
            }
        }catch (Exception ex){
            Log.d("ExpenseTracer","Error updating Income records"+ex.getMessage());
        }


    }

    private void deleteIncomeDetails(int id){
        try {
            int result=database.delete("INCOME","income_id=?",new String[]{String.valueOf(id)});

        }catch (Exception ex){
            Log.d("ExpenseTracer","Error deleting Income records"+ex.getMessage());
        }
    }
}
