/*package com.example.student_attendance_system;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
public class Dashboard extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public NavigationView nav_view;
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        ImageView splashLogo = findViewById(R.id.image);

        Animation translateAnimation = new TranslateAnimation(0, 0, -300, 0);
        translateAnimation.setDuration(1000);
        splashLogo.startAnimation(translateAnimation);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        setSupportActionBar(toolbar);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_section1) {
                   Intent intent=new Intent(Dashboard.this,CourseMaster.class);
                    startActivity(intent);
              }
                else if (itemId == R.id.menu_item1) {
                    Intent intent=new Intent(Dashboard.this,TeacherRegistration.class);
                    startActivity(intent);
               } else if (itemId == R.id.menu_item2) {
                    Intent intent=new Intent(Dashboard.this,StudentRegistration.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}*/
//
package com.example.College_Media_Application.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar; // Correct import



import com.example.College_Media_Application.Fragment.*;
import com.example.College_Media_Application.R;
import com.google.android.material.navigation.NavigationView;
public class TeacherDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    String uname,pword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar); // Use androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar);

        uname=getIntent().getStringExtra("uname");
        pword=getIntent().getStringExtra("pword");

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.nav_dashboard)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboradFragment()).commit();
        }
        else if (item.getItemId()==R.id.nav_Notes)
        {

            Intent intent = new Intent(TeacherDashboard.this, UploadPdfActivity.class);
            intent.putExtra("uname",uname);
            intent.putExtra("pword",pword);
            startActivity(intent);

        }

        else if (item.getItemId()==R.id.nav_Notice)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            NoticeForStdFrag noticeForStdFrag = new NoticeForStdFrag();
            noticeForStdFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noticeForStdFrag).commit();
        }
        else if (item.getItemId()==R.id.nav_teacherPost)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            Create_Post_for_std_Frag createPostForStdFrag = new Create_Post_for_std_Frag();
            createPostForStdFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createPostForStdFrag).commit();
        }
        else if (item.getItemId()==R.id.nav_ViewHodPost)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            ViewHODPostFrag viewHODPostFrag = new ViewHODPostFrag();
            viewHODPostFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewHODPostFrag).commit();
        }
        else if (item.getItemId()==R.id.nav_ApplyLeave)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            ApplyLeaveFrag applyLeaveFrag = new ApplyLeaveFrag();
            applyLeaveFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, applyLeaveFrag).commit();
        }
        else if (item.getItemId()==R.id.nav_ViewTimeTable)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            ViewTimeTableFrag viewTimeTableFrag = new ViewTimeTableFrag();
            viewTimeTableFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewTimeTableFrag).commit();
        }

        else if (item.getItemId() == R.id.nav_profile)
        {
            Bundle bundle1 = new Bundle();
            bundle1.putString("uname", uname);
            bundle1.putString("pword", pword);
            TeacherProfileFrag teacherProfileFrag = new TeacherProfileFrag();
            teacherProfileFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,teacherProfileFrag).commit();
        }
         else if (item.getItemId() == R.id.nav_logout)
        {
            Toast.makeText(this, "Logout..!", Toast.LENGTH_SHORT).show();
            finishAffinity();
            Intent intent=new Intent(TeacherDashboard.this, Login.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}

