package com.example.College_Media_Application.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrincipleRegistrationActivity extends AppCompatActivity {

    EditText name,username,password;
    Button btnRegister;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principle_registration);

        progressDialog=new ProgressDialog(this);

        name=findViewById(R.id.name);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btnRegister=findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });

    }
    public void register()
    {
        String Name=name.getText().toString().trim();
        String UserName=username.getText().toString().trim();
        String Password=password.getText().toString().trim();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_principleRegistration.php?name=" + Name + "&username=" + UserName + "&password=" + Password)
                    .build();
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            if (result.equals("Registration Success.")) {
                Intent intent=new Intent(PrincipleRegistrationActivity.this, AdminDashbord.class);
                startActivity(intent);
                Toast.makeText(this, "Registration Success.", Toast.LENGTH_SHORT).show();
            } else if (result.equals("Missing Parameters .")){
                Toast.makeText(this, "Missing parameters..", Toast.LENGTH_SHORT).show();
                Log.e("Present", "Server response code: " + response.code());
            }
            else {
                Toast.makeText(this, "something went wrong..", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public class LoadDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    register();
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