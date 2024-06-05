package com.example.College_Media_Application.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.College_Media_Application.Activity.changeHodPassword;
import com.example.College_Media_Application.Activity.updateHodProfile;
import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HODProfileFrag extends Fragment {

    TextView name,department,year,username,password;
    Button update,changePass;

    ProgressDialog progressDialog;
    String uname,pword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_h_o_d_profile, container, false);

        progressDialog=new ProgressDialog(getContext());

        update=view.findViewById(R.id.btn_update);
        changePass=view.findViewById(R.id.btn_change_pass);
        name=view.findViewById(R.id.ET_hodName);
        department=view.findViewById(R.id.ET_hod_department);
        year=view.findViewById(R.id.ET_hod_year);
        username=view.findViewById(R.id.ET_username);
        password=view.findViewById(R.id.ET_password);

        Bundle args = getArguments();
        if (args != null) {
            uname = args.getString("uname1");
            pword = args.getString("pword1");
            System.out.println("UserName is: "+uname);
            System.out.println("Password is: "+pword);
        }
        new load().execute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), updateHodProfile.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("department",department.getText().toString());
                intent.putExtra("year",year.getText().toString());
                startActivity(intent);
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), changeHodPassword.class);
                intent.putExtra("password",password.getText().toString());
                intent.putExtra("department",department.getText().toString());
                startActivity(intent);
            }
        });
        return view;
    }
    public void getProfileData(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SAsystem_getHodProfileData.php?username=" + uname + "&password=" + pword )
                    .build();

            Response response4 = client.newCall(request).execute();
            String responseString4 = response4.body().string();

            if (responseString4.equals("Fail")) {
                Toast.makeText(getContext(), "Failed to retrieve profile data", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray contacts = new JSONArray(responseString4);
            for (int i = 0; i < contacts.length(); i++)
            {
                JSONObject c = contacts.getJSONObject(i);

                String  hodname = c.getString("hodName");
                String hodDepartment = c.getString("hodDepartment");
                String  Year = c.getString("year");
                String  userName = c.getString("userName");
                String  Password = c.getString("Password");

                name.setText(hodname);
                department.setText(hodDepartment);
                year.setText(Year);
                username.setText(userName);
                password.setText(Password);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public class load extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getProfileData();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
        }
    }
}