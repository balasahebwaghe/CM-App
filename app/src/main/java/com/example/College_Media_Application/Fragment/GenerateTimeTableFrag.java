package com.example.College_Media_Application.Fragment;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.College_Media_Application.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenerateTimeTableFrag extends Fragment {
    private static final String[] TIMES = {"10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM", "01:00 PM - 02:00 PM", "02:00 PM - 03:00 PM", "03:00 PM - 04:00 PM", "04:00 PM - 05:00 PM"};
    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    private Button submit;
    private LinearLayout timeLayout, dayLayout, timeTableLayout;
    private ProgressDialog progressDialog;
    private int totalSubjects;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_time_table, container, false);

        progressDialog = new ProgressDialog(getContext());

        timeLayout = view.findViewById(R.id.LL_time);
        dayLayout = view.findViewById(R.id.LL_day);
        timeTableLayout = view.findViewById(R.id.LL_time_table);
        submit = view.findViewById(R.id.btn_submit);

        addTextViews(timeLayout, TIMES);
        /*addTextViews(dayLayout, DAYS);*/
        generateTimeTable();
        checkSubmitButtonEnabled();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubmitTimetableTask().execute();
            }
        });
        return view;
    }
    private void addTextViews(LinearLayout parentLayout, String[] texts) {
        for (String text : texts) {
            TextView textView = new TextView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            textView.setLayoutParams(params);
            textView.setText(text);
            textView.setPadding(10, 10, 10, 10);
            textView.setBackgroundResource(R.drawable.border);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            parentLayout.addView(textView);
        }
    }
    private void generateTimeTable() {
        for (String day : DAYS) {
            LinearLayout rowLayout = new LinearLayout(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(params);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView dayTextView = new TextView(requireContext());
            LinearLayout.LayoutParams dayParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            dayTextView.setLayoutParams(dayParams);
            dayTextView.setText(day);
            dayTextView.setPadding(10, 10, 10, 10);
            dayTextView.setBackgroundResource(R.drawable.border);
            dayTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowLayout.addView(dayTextView);

            for (String time : TIMES) {
                EditText editText = new EditText(requireContext());
                LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                editText.setLayoutParams(editTextParams);
                editText.setHint("Subject");
                editText.setPadding(10, 10, 10, 10);
                editText.setBackgroundResource(R.drawable.border);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        checkSubmitButtonEnabled();
                    }
                });
                rowLayout.addView(editText);
            }
            timeTableLayout.addView(rowLayout);
        }
    }
    private void checkSubmitButtonEnabled() {
        totalSubjects = DAYS.length * TIMES.length;
        int subjectsEntered = 0;
        for (int i = 0; i < timeTableLayout.getChildCount(); i++) {
            LinearLayout rowLayout = (LinearLayout) timeTableLayout.getChildAt(i);
            for (int j = 0; j < rowLayout.getChildCount(); j++) {
                if (rowLayout.getChildAt(j) instanceof EditText) {
                    EditText editText = (EditText) rowLayout.getChildAt(j);
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        subjectsEntered++;
                    }
                }
            }
        }
        submit.setEnabled(subjectsEntered == totalSubjects);
    }
    private class SubmitTimetableTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Construct the URL to delete all records from the table
            String deleteUrl = "http://yash.testproject.life/SASystem_deleteOldTimeTable.php";
            Request deleteRequest = new Request.Builder()
                    .url(deleteUrl)
                    .build();
            try {
                // Execute the delete request
                Response deleteResponse = client.newCall(deleteRequest).execute();
                String deleteResult = deleteResponse.body().string();
                // Check if the deletion was successful
                if (deleteResult != null && deleteResult.equalsIgnoreCase("Success.")) {
                    // Proceed with inserting new records
                    for (int i = 0; i < DAYS.length; i++) {
                        String day = DAYS[i];
                        LinearLayout rowLayout = (LinearLayout) timeTableLayout.getChildAt(i);

                        for (int j = 0; j < TIMES.length; j++) {
                            String time = TIMES[j];
                            EditText editText = (EditText) rowLayout.getChildAt(j + 1);
                            String subject = editText.getText().toString().trim();

                            // Construct the URL with proper encoding for the subject name
                            String insertUrl = "http://yash.testproject.life/SASystem_insertTimeTable.php" +
                                    "?day=" + day +
                                    "&time=" + time +
                                    "&subject=" + Uri.encode(subject); // Ensure proper URL encoding for the subject name
                            Request insertRequest = new Request.Builder()
                                    .url(insertUrl)
                                    .build();
                            // Execute the insert request
                            Response insertResponse = client.newCall(insertRequest).execute();
                            String insertResult = insertResponse.body().string();
                            // Check if the insertion was successful
                            if (insertResult != null && insertResult.equalsIgnoreCase("Success.")) {
                            } else {
                                return "Fail to submit";
                            }
                        }
                    }
                    return "Timetable submitted successfully";
                } else {
                    return "Failed to delete existing data";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String message) {
            progressDialog.dismiss();
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
