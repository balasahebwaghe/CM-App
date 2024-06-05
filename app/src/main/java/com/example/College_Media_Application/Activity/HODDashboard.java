package com.example.College_Media_Application.Activity;
import com.example.College_Media_Application.Fragment.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.College_Media_Application.R;
import com.google.android.material.navigation.NavigationView;

public class HODDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    String uname,pword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoddashboard);
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
        else if (item.getItemId()==R.id.nav_profile)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            HODProfileFrag hodProfileFrag = new HODProfileFrag();
            hodProfileFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, hodProfileFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_facultyDetails)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            FacultyDetailsFrag facultyDetailsFrag = new FacultyDetailsFrag();
            facultyDetailsFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, facultyDetailsFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_stdDetails)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            StudentDetailsFrag studentDetailsFrag = new StudentDetailsFrag();
            studentDetailsFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentDetailsFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_post)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            HOD_Create_PostFrag hodPostFrag = new HOD_Create_PostFrag();
            hodPostFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, hodPostFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_teacherLeave)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            AcceptTeacherLeaveFrag acceptTeacherLeaveFrag = new AcceptTeacherLeaveFrag();
            acceptTeacherLeaveFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, acceptTeacherLeaveFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_stdComplaint)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            StudentComplaintFrag studentComplaintFrag = new StudentComplaintFrag();
            studentComplaintFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentComplaintFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_principlePost)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            ViewPrinciplePostFrag viewPrinciplePostFrag = new ViewPrinciplePostFrag();
            viewPrinciplePostFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPrinciplePostFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_teacherRegister)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            TeacherReg teacherReg = new TeacherReg();
            teacherReg.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, teacherReg).commit();

        }
        else if (item.getItemId()==R.id.nav_generatetimetable)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            GenerateTimeTableFrag generateTimeTableFrag = new GenerateTimeTableFrag();
            generateTimeTableFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, generateTimeTableFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_stdRegister)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            StudentReg studentReg = new StudentReg();
            studentReg.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, studentReg).commit();

        }
        else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout..!", Toast.LENGTH_SHORT).show();
            finishAffinity();
            Intent intent = new Intent(HODDashboard.this, Login.class);
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