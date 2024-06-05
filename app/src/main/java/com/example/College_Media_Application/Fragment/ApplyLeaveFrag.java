package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.College_Media_Application.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ApplyLeaveFrag extends Fragment {
    String uname,pword;
    private TextView dateTextView;
    private Calendar selectedDate;
    EditText reason;
    TextInputEditText faculty,phone;
    CardView apply;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_apply_leave, container, false);


        dateTextView = view.findViewById(R.id.dateTextView);
        reason=view.findViewById(R.id.reason);
        faculty=view.findViewById(R.id.faculty);
        phone=view.findViewById(R.id.phone);
        selectedDate = Calendar.getInstance();
        apply=view.findViewById(R.id.apply);
        progressDialog=new ProgressDialog(getContext());

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new load().execute();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            uname = args.getString("uname");
            pword = args.getString("pword");

            System.out.println("UserName is: "+uname);
            System.out.println("Password is: "+pword);
        }
        return view;
    }

    private void showDatePickerDialog() {
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.set(year, monthOfYear, dayOfMonth);
                String formattedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                dateTextView.setText(formattedDate);
            }
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void applyLeave() {
        String FacultyName=faculty.getText().toString().trim();
        String date=dateTextView.getText().toString().trim();
        String Reason=reason.getText().toString().trim();
        String Phone=phone.getText().toString().trim();
        String phonePattern = "\\d{10}";
        String Status="waiting";


        if (FacultyName!=null && date!="Select Date" && Reason!=null && Status!=null && uname!=null && !Status.isEmpty() && !uname.isEmpty() && !Phone.isEmpty() && Phone.matches(phonePattern))
        {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_applyLeave.php?teachername=" + FacultyName + "&date=" + date+"&reason=" + Reason+ "&status="+Status+"&username="+uname+"&phone="+Phone)
                        .build();
                Response response4 = client.newCall(request).execute();
                String responseString4 = response4.body().string();

                if (responseString4.equalsIgnoreCase("Leave send."))
                {
                    reason.setText("");
                    dateTextView.setText("Select Date");
                    faculty.setText("");
                    Toast.makeText(getContext(), "apply request added.", Toast.LENGTH_SHORT).show();
                }
                if (responseString4.equalsIgnoreCase("Missing Parametres ."))
                {
                    Toast.makeText(getContext(), "Missing Parameters .", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Please Enter all details.", Toast.LENGTH_SHORT).show();
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
                    applyLeave();
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