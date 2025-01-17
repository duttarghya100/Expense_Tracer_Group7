package com.example.expense_tracer;

import android.content.Intent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportOptionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SQLiteDatabase sqLiteDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ReportOptionsFragment(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public ReportOptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportOptionsFragment.
     */


    //Arghya

    Button timeLineReportBtn;
    Button categoryReportBtn;
    // TODO: Rename and change types and number of parameters
    public static ReportOptionsFragment newInstance(String param1, String param2) {
        ReportOptionsFragment fragment = new ReportOptionsFragment();
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
        View view= inflater.inflate(R.layout.fragment_report_options, container, false);

        timeLineReportBtn = view.findViewById(R.id.timelineReportBtn);
        timeLineReportBtn.setBackgroundColor(Color.rgb(255, 179, 102));
        categoryReportBtn = view.findViewById(R.id.categoriesReportBtn);
        categoryReportBtn.setBackgroundColor(Color.rgb(255, 179, 102));

        categoryReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ActivityReportCategory.class));

            }
        });

        timeLineReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ActivityReportTimeline.class));
            }
        });
        return view;
    }
}