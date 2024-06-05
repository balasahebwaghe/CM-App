package com.example.College_Media_Application.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentProfileFrag extends Fragment {

    ProgressDialog progressDialog;
    String uname2;
    String pword2;
    TextView rollno,name,mobile,year,course,username,password;
    Button edit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_stuednt_profile, container, false);
       progressDialog=new ProgressDialog(getContext());

        rollno=view.findViewById(R.id.ET_rollNumber);
        name=view.findViewById(R.id.ET_studentName);
        mobile=view.findViewById(R.id.ET_mobileNumber);
        year=view.findViewById(R.id.ET_year);
        username=view.findViewById(R.id.ET_username);
        password=view.findViewById(R.id.ET_password);
        course=view.findViewById(R.id.ET_courseName);
        edit=view.findViewById(R.id.btn_edit);


        Bundle args = getArguments();
        if (args != null) {
            uname2 = args.getString("uname1");
            pword2 = args.getString("pword1");
            if (uname2 != null && pword2 != null) {
                new LoadDataTask().execute();
            }
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
        return view;
    }
    public void  getData(String uname, String pword)
    {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_getSTDInfo.php?uname=" + uname + "&pword=" + pword + "")
                    .build();

            Response response4 = client.newCall(request).execute();
            String responseString4 = response4.body().string();

            JSONArray contacts = new JSONArray(responseString4);
            for (int i = 0; i < contacts.length(); i++)
            {
                JSONObject c = contacts.getJSONObject(i);

                String  Sname = c.getString("studentName");
                String Rollno = c.getString("rollno");
                String Course = c.getString("courseName");
                String  Year = c.getString("year");
                String  number = c.getString("Pnumber");
                String UserName = c.getString("userName");
                String Password = c.getString("password");

                rollno.setText(String.valueOf(Rollno));
                name.setText(Sname);
                mobile.setText(number);
                year.setText(Year);
                course.setText(Course);
                username.setText(UserName);
                password.setText(Password);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    private void showEditDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_student_profile, null);

        EditText editStudentName = dialogView.findViewById(R.id.edit_student_name);
        EditText editRollNumber = dialogView.findViewById(R.id.edit_roll_number);
        EditText editCourseName = dialogView.findViewById(R.id.edit_course_name);
        EditText editYear = dialogView.findViewById(R.id.edit_year);
        EditText editMobileNumber = dialogView.findViewById(R.id.edit_mobile_number);
        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);

        editStudentName.setText(name.getText().toString());
        editRollNumber.setText(rollno.getText().toString());
        editCourseName.setText(course.getText().toString());
        editYear.setText(year.getText().toString());
        editMobileNumber.setText(mobile.getText().toString());
        editUsername.setText(username.getText().toString());
        editPassword.setText(password.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.show();

        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSave.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                String updatedStudentName = editStudentName.getText().toString();
                String updatedRollNumber = editRollNumber.getText().toString();
                String updatedCourseName = editCourseName.getText().toString();
                String updatedYear = editYear.getText().toString();
                String updatedMobileNumber = editMobileNumber.getText().toString();
                String updatedUsername = editUsername.getText().toString();
                String updatedPassword = editPassword.getText().toString();

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://yash.testproject.life/SASystem_editStudentProfile.php?" +
                                    "studentname=" + updatedStudentName +
                                    "&rollno=" + updatedRollNumber +
                                    "&course=" + updatedCourseName +
                                    "&year=" + updatedYear +
                                    "&mobile=" + updatedMobileNumber +
                                    "&username=" + updatedUsername +
                                    "&password=" + updatedPassword +
                                    "&oldusername=" + uname2 +
                                    "&oldpassword=" + pword2)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    if (responseString.equals("Update Success.")) {
                        Toast.makeText(getActivity(), "Update Success.", Toast.LENGTH_SHORT).show();
                        name.setText(updatedStudentName);
                        rollno.setText(updatedRollNumber);
                        course.setText(updatedCourseName);
                        year.setText(updatedYear);
                        mobile.setText(updatedMobileNumber);
                        username.setText(updatedUsername);
                        password.setText(updatedPassword);
                        dialog.dismiss();
                    } else if (responseString.equalsIgnoreCase("Missing Parameters.")) {
                        Toast.makeText(getActivity(), "Missing Parameters.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), "Error: Something went wrong.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Canceled..", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
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
                    getData(uname2, pword2);
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