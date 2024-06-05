package com.example.College_Media_Application.Activity;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.College_Media_Application.R;

public class updateHodProfile extends AppCompatActivity {

    EditText name,department,year;
    Button update;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hod_profile);

        progressDialog=new ProgressDialog(this);

        update=findViewById(R.id.btn_update);

        name=findViewById(R.id.ET_hodName);
        department=findViewById(R.id.ET_hod_department);
        year=findViewById(R.id.ET_hod_year);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });

    }
    public void updateData() throws InterruptedException {
        sleep(2000);
        Toast.makeText(this, "Sleep is over", Toast.LENGTH_SHORT).show();
        sleep(2000);
        Toast.makeText(this, "Date is Updated", Toast.LENGTH_SHORT).show();
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
                        updateData();
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