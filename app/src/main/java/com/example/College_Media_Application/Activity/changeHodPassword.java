package com.example.College_Media_Application.Activity;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class changeHodPassword extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText newPasswprd;
    Button save;
    String oldpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_hod_password);

        oldpassword=getIntent().getStringExtra("password");
        progressDialog=new ProgressDialog(this);
        newPasswprd=findViewById(R.id.ET_new_pass);
        newPasswprd.setText(oldpassword);
        save=findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });
    }
    public void saveData() throws InterruptedException {

        String newpass=newPasswprd.getText().toString().trim();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_updateHodPassword.php?oldpass=" + oldpassword + "&newpass=" + newpass)
                    .build();
            Response response4 = client.newCall(request).execute();
            String responseString4 = response4.body().string();

            if (responseString4.equalsIgnoreCase("Update Success."))
            {
                Toast.makeText(this, "Update Success.", Toast.LENGTH_SHORT).show();
            }
            if (responseString4.equalsIgnoreCase("Missing Parametres ."))
            {
                Toast.makeText(this, "Missing Parameters .", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Error: Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //Toast.makeText(this, "Data Password Changed...", Toast.LENGTH_SHORT).show();
    }



    private class LoadDataTask extends AsyncTask<Void, Void, Void> {
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
                    try {
                        saveData();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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