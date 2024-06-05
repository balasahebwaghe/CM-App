package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentReg extends Fragment {
    private Spinner idcourse, idyear;
    private EditText studentname, username, Password,pnumber;
    private CardView btnlogin;
    ArrayList<String> list;
    ArrayList<Integer> rollNo;
    ProgressDialog progressDialog;



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_student_reg, container, false);
        progressDialog = new ProgressDialog(getContext());

        idcourse = view.findViewById(R.id.idcourse);
        idyear = view.findViewById(R.id.idyear);
        studentname = view.findViewById(R.id.studentname);
        username = view.findViewById(R.id.userName);
        Password = view.findViewById(R.id.Password);
        btnlogin = view.findViewById(R.id.btnlogin);
        pnumber = view.findViewById(R.id.Pnumber);

        ArrayAdapter<CharSequence> Tadapter = ArrayAdapter.createFromResource(getContext(), R.array.year, android.R.layout.simple_spinner_item);
        Tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idyear.setAdapter(Tadapter);

        ArrayAdapter<CharSequence> Tadapter2 = ArrayAdapter.createFromResource(getContext(), R.array.branch, android.R.layout.simple_spinner_item);
        Tadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idcourse.setAdapter(Tadapter2);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = idcourse.getSelectedItem().toString().trim();
                String year = idyear.getSelectedItem().toString().trim();
                String studentName = studentname.getText().toString().trim();
                String userName = username.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String Pnumber = pnumber.getText().toString().trim();
                if (course.equals("Select Course")) {
                    Toast.makeText(getContext(), "Please select your course", Toast.LENGTH_SHORT).show();
                    return;
                } else if (year.equals("Select Year")) {
                    Toast.makeText(getContext(), "Please select your year", Toast.LENGTH_SHORT).show();
                    return;
                } else if (studentName.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter student Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userName.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter password number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Pnumber.length() != 10) {
                    Toast.makeText(getContext(), "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Pnumber.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter Phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                new StudentRegistrationTask().execute(course, year, studentName, userName, password,Pnumber);
            }
        });
        return view;
    }
    private int generateRandomRollNo() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }
    private class StudentRegistrationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String course = params[0];
            String year = params[1];
            String studentName = params[2];
            String userName = params[3];
            String password = params[4];
            int rollno = generateRandomRollNo();
            String Pnumber= params[5];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://yash.testproject.life/SASystem_studentRegistration.php?studentName=" + studentName + "&userName=" + userName + "&password=" + password + "&course=" + course + "&year=" + year+"&rollno="+rollno+"&Pnumber="+Pnumber)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equalsIgnoreCase("Registration Success.")) {
                    Toast.makeText(getContext(), "Student Registration Successfully.", Toast.LENGTH_SHORT).show();
                } else if (result.equalsIgnoreCase("already exists.")){
                    Toast.makeText(getContext(), "Student already exists. Please try another.", Toast.LENGTH_SHORT).show();
                } else if (result.equalsIgnoreCase("RMissing Parameters.")) {
                    Toast.makeText(getContext(), "Missing Parameters.", Toast.LENGTH_SHORT).show();
                } else if (result.startsWith("Error: ")) {
                    Toast.makeText(getContext(), "Error server side.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}