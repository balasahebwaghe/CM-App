package com.example.College_Media_Application.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.College_Media_Application.Fragment.AddComplaintFrag;
import com.example.College_Media_Application.Fragment.DashboradFragment;
import com.example.College_Media_Application.Fragment.StudentProfileFrag;
import com.example.College_Media_Application.Fragment.ViewPostFrag;
import com.example.College_Media_Application.R;
import com.google.android.material.navigation.NavigationView;
import com.example.College_Media_Application.Fragment.ViewTimeTableFrag;
public class StudentDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    String uname,pword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboradFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         uname = getIntent().getStringExtra("uname");
         pword = getIntent().getStringExtra("pword");

        if (item.getItemId() == R.id.nav_dashboard)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboradFragment()).commit();
        }
        else if (item.getItemId()==R.id.nav_Profile)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            StudentProfileFrag studentProfileFrag = new StudentProfileFrag();
            studentProfileFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentProfileFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_viewNotes)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            ViewNotes viewNotes = new ViewNotes();
            viewNotes.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewNotes).commit();

        }
        else if (item.getItemId()==R.id.nav_viewPost)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            ViewPostFrag viewPostFrag = new ViewPostFrag();
            viewPostFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPostFrag).commit();

        }
        else if (item.getItemId() == R.id.nav_timeTable)
        {
            /*Intent intent=new Intent(StudentDashboard.this,TimeTable.class);
            intent.putExtra("uname",uname);
            intent.putExtra("pword",pword);
            startActivity(intent);*/
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            ViewTimeTableFrag viewTimeTableFrag = new ViewTimeTableFrag();
            viewTimeTableFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewTimeTableFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_addComplaint)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            AddComplaintFrag addComplaintFrag = new AddComplaintFrag();
            addComplaintFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addComplaintFrag).commit();

        }
        else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout..!", Toast.LENGTH_SHORT).show();
            finishAffinity();
            Intent intent = new Intent(StudentDashboard.this, Login.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
