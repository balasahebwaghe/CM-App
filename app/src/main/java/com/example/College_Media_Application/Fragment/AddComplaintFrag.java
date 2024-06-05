package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddComplaintFrag extends Fragment {

    EditText faculty,complaint;
    CardView addComplaint;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_complaint, container, false);

        progressDialog=new ProgressDialog(getContext());

        faculty=view.findViewById(R.id.faculty);
        complaint=view.findViewById(R.id.complaint);
        addComplaint=view.findViewById(R.id.addComplaint);

        addComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });
        return view;
    }
    public void submitData()
    {
        String FacultyName=faculty.getText().toString().trim();
        String Complaint=complaint.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String formattedDate = sdf.format(calendar.getTime());

        if (FacultyName!=null && Complaint!=null)
        {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_addComplaint.php?faculty=" + FacultyName + "&complaint=" + Complaint + "&date="+formattedDate)
                        .build();
                Response response4 = client.newCall(request).execute();
                String responseString4 = response4.body().string();

                if (responseString4.equalsIgnoreCase("Fail: Faculty does not exist."))
                {
                    Toast.makeText(getContext(), "Faculty does not exist.", Toast.LENGTH_SHORT).show();
                }
                if (responseString4.equalsIgnoreCase("success."))
                {
                    faculty.setText("");
                    complaint.setText("");
                    Toast.makeText(getContext(), "Complaint success.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Error: Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            Toast.makeText(getContext(), "Please Enter FacultyName/Complaint.", Toast.LENGTH_SHORT).show();
        }
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

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
                    submitData();
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