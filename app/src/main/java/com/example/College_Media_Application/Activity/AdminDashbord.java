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


import com.example.College_Media_Application.Fragment.DashboradFragment;
import com.example.College_Media_Application.Fragment.Priciple_Create_PostFrag;
import com.example.College_Media_Application.Fragment.AdminProfileFrag;
import com.example.College_Media_Application.R;
import com.google.android.material.navigation.NavigationView;
import com.example.College_Media_Application.Fragment.HODDetailsFrag;
import com.example.College_Media_Application.Fragment.HodRegistrationFrag;
public class AdminDashbord extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    String uname,pword;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashbord);
         Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uname=getIntent().getStringExtra("uname").toString();
        pword=getIntent().getStringExtra("pword").toString();

        System.out.println("UserName is: "+uname);
        System.out.println("Password is: "+pword);

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
        else if (item.getItemId()==R.id.nav_hodDetails)
        {

            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            HODDetailsFrag viewPrinciplePostFrag = new HODDetailsFrag();
            viewPrinciplePostFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewPrinciplePostFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_post)
        {

            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            Priciple_Create_PostFrag priciple_create_postFrag = new Priciple_Create_PostFrag();
            priciple_create_postFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, priciple_create_postFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_profile)
        {

            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            AdminProfileFrag adminProfileFrag = new AdminProfileFrag();
            adminProfileFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adminProfileFrag).commit();

        }
        else if (item.getItemId()==R.id.nav_hodRegistration)
        {

            Bundle bundle1 = new Bundle();
            bundle1.putString("uname1", uname);
            bundle1.putString("pword1", pword);
            HodRegistrationFrag hodRegistrationFrag = new HodRegistrationFrag();
            hodRegistrationFrag.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, hodRegistrationFrag).commit();

        }

        else if (item.getItemId() == R.id.nav_logout)
        {
            Toast.makeText(this, "Logout..!", Toast.LENGTH_SHORT).show();
            finishAffinity();
            Intent intent=new Intent(AdminDashbord.this, Login.class);
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