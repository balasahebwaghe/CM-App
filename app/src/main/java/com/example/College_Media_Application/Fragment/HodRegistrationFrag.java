package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HodRegistrationFrag extends Fragment {

    private Spinner idcourse, idyear;
    private EditText hodName, username, Password;
    private CardView btnlogin;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hod_registration, container, false);

        progressDialog = new ProgressDialog(getContext());

        idcourse = view.findViewById(R.id.idcourse);
        idyear = view.findViewById(R.id.idyear);
        hodName = view.findViewById(R.id.hodname);
        username = view.findViewById(R.id.userName);
        Password = view.findViewById(R.id.Password);
        btnlogin = view.findViewById(R.id.btnlogin);


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
                String studentName = hodName.getText().toString().trim();
                String userName = username.getText().toString().trim();
                String password = Password.getText().toString().trim();
                /*String Pnumber = pnumber.getText().toString().trim();*/
                if (course.equals("Select Course")) {
                    Toast.makeText(getContext(), "Please select your course", Toast.LENGTH_SHORT).show();
                    return;
                } else if (year.equals("Select Year")) {
                    Toast.makeText(getContext(), "Please select your year", Toast.LENGTH_SHORT).show();
                    return;
                } else if (studentName.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter HOD Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (userName.isEmpty()) {
                    Toast.makeText(getContext(), "Please UserName", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    new HodRegistrationTask().execute(course, year, studentName, userName, password);
                }
            }
        });
        return view;
    }

    private class HodRegistrationTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String course = params[0];
            String year = params[1];
            String HodName = params[2];
            String userName = params[3];
            String password = params[4];
            System.out.println("password is : "+password);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://yash.testproject.life/SASystem_hodRegistration.php?hodName=" + HodName + "&userName=" + userName + "&password=" + password + "&hodDepartment=" + course + "&year=" + year)
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
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Hod Registration Successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}