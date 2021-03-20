package com.example.expense_tracer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_tracer.Model.Data;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    List<Data> dataList;
    Context context;
    SQLiteDatabase database;


    public RecyclerViewAdapter(List<Data> data, Context context, SQLiteDatabase database) {
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


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        private  void setType(String type){

            TextView mType= mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        private  void setNote(String note){

            TextView mNote= mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        private void    setDate(String date)
        {
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        private void    setAmount(String amount)
        {
            TextView mAmount=mView.findViewById(R.id.amount_txt_income);
            String staamount=String.valueOf(amount);
            mAmount.setText(staamount);
        }



    }
}
