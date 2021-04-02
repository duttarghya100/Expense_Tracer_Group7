package com.example.expense_tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
private FrameLayout frameLayout;

//Fragments
private DashboardFragment dashboardFragment;
private IncomeFragment incomeFragment;
private ExpenseFragment expenseFragment;
private ReportOptionsFragment optionsFragment;
private FirebaseAuth mAuth;

//DB
public SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expense Tracer");
        openDataBase();

        mAuth=FirebaseAuth.getInstance();
        //  setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open
        ,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.NavView);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView=findViewById(R.id.bottomNavBar);
        frameLayout=findViewById(R.id.main_frame);
        dashboardFragment=new DashboardFragment(sqLiteDatabase);
        incomeFragment=new IncomeFragment(sqLiteDatabase,this);
        expenseFragment=new ExpenseFragment(sqLiteDatabase);
        optionsFragment = new ReportOptionsFragment(sqLiteDatabase);
        setFragment(dashboardFragment);

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.dashboard:
                       setFragment(dashboardFragment);
                       bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                       return true;
                   case R.id.income:
                       setFragment(incomeFragment);
                       bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                       return true;

                   case R.id.expense:
                       setFragment(expenseFragment);
                       bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                       return true;


                   default:
                       return false;
               }

           }

       });


       createTables();

    }

    private void openDataBase() {
        try {
            sqLiteDatabase = openOrCreateDatabase("ExpenseTacer.db", MODE_PRIVATE, null);
        }catch (Exception ex){
            Log.d("ExpenseTracer", ex.getMessage());
        }

    }
    private void createTables() {
        String incomeTable = "CREATE TABLE IF NOT EXISTS INCOME" +
                " ( income_id INTEGER PRIMARY KEY, " +
                " income_type TEXT NOT NULL, " +
                "income_amount INTEGER NOT NULL," +
                "income_note TEXT NOT NULL," +
                "income_date TEXT NOT NULL);";
        String expenseTable = "CREATE TABLE IF NOT EXISTS EXPENSE" +
                " ( expense_id INTEGER PRIMARY KEY, " +
                " expense_type TEXT NOT NULL, " +
                "expense_amount INTEGER NOT NULL," +
                " expense_note TEXT NOT NULL," +
                "expense_date TEXT NOT NULL);";
        sqLiteDatabase.execSQL(incomeTable);
        sqLiteDatabase.execSQL(expenseTable);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer((GravityCompat.END));
        }else {
            super.onBackPressed();
        }

    }

    public void displaySelectedListener(int itemId){
        Fragment fragment=null;
        switch ((itemId)){
            case R.id.dashboard:
                fragment=new DashboardFragment(sqLiteDatabase);
                break;
            case R.id.income:
                fragment=new IncomeFragment(sqLiteDatabase,this);
                break;
            case R.id.expense:
                fragment=new ExpenseFragment(sqLiteDatabase);
                break;
            case R.id.report:
                fragment=new ReportOptionsFragment(sqLiteDatabase);
                break;

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

        }

        if ((fragment!=null)){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }


}