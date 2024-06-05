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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherReg extends Fragment
{

    private Spinner idcourse, idyear;
    private EditText teachername, username, Password,Phone;
    private CardView btnlogin;

    ProgressDialog progressDialog;
        @SuppressLint("MissingInflatedId")
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_teacher_reg, container, false);

            progressDialog = new ProgressDialog(getContext());
            idcourse = view.findViewById(R.id.idcourse);
            idyear = view.findViewById(R.id.idyear);
            teachername = view.findViewById(R.id.teacherName);
            Phone = view.findViewById(R.id.phone);
            username = view.findViewById(R.id.username);
            Password = view.findViewById(R.id.password);
            btnlogin = view.findViewById(R.id.btnlogin);


            ArrayAdapter<CharSequence> Tadapter = ArrayAdapter.createFromResource(getContext(), R.array.year, android.R.layout.simple_spinner_item);
            Tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            idyear.setAdapter(Tadapter);


            ArrayAdapter<CharSequence> Tadapter3 = ArrayAdapter.createFromResource(getContext(), R.array.branch, android.R.layout.simple_spinner_item);
            Tadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            idcourse.setAdapter(Tadapter3);


            /* new LoadDataTask().execute();*/

            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String course = idcourse.getSelectedItem() != null ? idcourse.getSelectedItem().toString().trim() : "";
                    String year = idyear.getSelectedItem() != null ? idyear.getSelectedItem().toString().trim() : "";
                    String teacherName = teachername.getText().toString().trim();
                    String userName = username.getText().toString().trim();
                    String password = Password.getText().toString().trim();
                    String phone = Phone.getText().toString().trim();

                    // Regular expression to match a valid phone number (10 digits)
                    String phonePattern = "\\d{10}";

                    if (course.equals("Select Course")) {
                        Toast.makeText(getContext(), "Please select your Course", Toast.LENGTH_SHORT).show();
                    } else if (course.isEmpty()) {
                        Toast.makeText(getContext(), "Please select your Course", Toast.LENGTH_SHORT).show();
                    } else if (year.equals("Select Year")) {
                        Toast.makeText(getContext(), "Please select your Year", Toast.LENGTH_SHORT).show();
                    } else if (teacherName.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter teacher Name", Toast.LENGTH_SHORT).show();
                    } else if (userName.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
                    } else if (phone.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter Phone Number", Toast.LENGTH_SHORT).show();
                    } else if (!phone.matches(phonePattern)) {
                        Toast.makeText(getContext(), "Please enter a valid 10-digit Phone Number", Toast.LENGTH_SHORT).show();
                    } else {
                        // Check if any of the selections are null
                        if (course == null || year == null || teacherName == null || userName == null || password == null) {
                            Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                        } else {
                            new TeacherRegistrationTask().execute(course, year, teacherName, userName, password, phone);

                        }
                    }
                }
            });
            return view;
        }

            class TeacherRegistrationTask extends AsyncTask<String, Void, String> {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        String course = params[0];
                        String year = params[1];
                        String teacherName = params[2];
                        String userName = params[3];
                        String password = params[4];
                        String phone = params[5];
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://yash.testproject.life/SASystem_teacherRegistration.php?teacherName=" + teacherName + "&userName=" + userName + "&password=" + password + "&course=" + course + "&year=" + year + "&phone=" + phone)
                                .build();
                        Response response = client.newCall(request).execute();
                        return response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Error: " + e.getMessage();
                    }
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    handleRegistrationResult(result);
                }

                private void handleRegistrationResult(String result) {
                    getActivity().runOnUiThread(() -> {
                        if (result != null) {
                            if (result.equalsIgnoreCase("Registration Success.")) {
                                Toast.makeText(getContext(), "Teacher Registration Successfully.", Toast.LENGTH_SHORT).show();
                            } else if (result.equalsIgnoreCase("Username already exists.")) {
                                Toast.makeText(getContext(), "Teacher already exists. Please try another.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Network error occurred
                            Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
}