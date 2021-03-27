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

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.MyViewHolder>{

    List<Data> dataList;
    Context context;
    SQLiteDatabase database;


    public DashboardRecyclerViewAdapter(List<Data> data, Context context, SQLiteDatabase database) {
        this.dataList = data;
        this.context = context;
        this.database = database;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list, parent, false);
       MyViewHolder viewHolder = new MyViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = dataList.get(position);

        holder.setType(String.valueOf(data.getType()));
        holder.setAmount(String.valueOf(data.getAmount()));
        holder.setDate(String.valueOf(data.getDate()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private  void setType(String type){

            TextView mType= itemView.findViewById(R.id.type_income_ds);
            mType.setText(type);
        }
        private void    setAmount(String amount)
        {
            TextView mAmount=itemView.findViewById(R.id.amount_income_ds);
            //String staamount=String.valueOf(amount);
            mAmount.setText(amount);
        }
        private void    setDate(String date)
        {
            TextView mDate=itemView.findViewById(R.id.date_income_ds);
            mDate.setText(date);
        }

    }
}
