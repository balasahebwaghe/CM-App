package com.example.College_Media_Application.Fragment;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherProfileFrag extends Fragment {

    TextView teacherName, teacherCourse, year, username, password;
    Button edit,btn_cancel;
    String uname, pword;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        progressDialog = new ProgressDialog(getContext());
        teacherName = view.findViewById(R.id.ET_teacherName);
        teacherCourse = view.findViewById(R.id.ET_teacher_course);
        year = view.findViewById(R.id.ET_teacher_year);
        username = view.findViewById(R.id.ET_username);
        password = view.findViewById(R.id.ET_password);
        edit = view.findViewById(R.id.btn_edit);

        Bundle args = getArguments();
        if (args != null) {
            uname = args.getString("uname");
            pword = args.getString("pword");
            new LoadDataTask().execute();
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });

        return view;
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_teacher_profile, null);

        EditText editTeacherName = dialogView.findViewById(R.id.edit_teacher_name);
        EditText editTeacherCourse = dialogView.findViewById(R.id.edit_teacher_course);
        EditText editTeacherYear = dialogView.findViewById(R.id.edit_teacher_year);
        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);

        editTeacherName.setText(teacherName.getText().toString());
        editTeacherCourse.setText(teacherCourse.getText().toString());
        editTeacherYear.setText(year.getText().toString());
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

                String updatedTeacherName = editTeacherName.getText().toString();
                String updatedTeacherCourse = editTeacherCourse.getText().toString();
                String updatedTeacherYear = editTeacherYear.getText().toString();
                String updatedUsername = editUsername.getText().toString();
                String updatedPassword = editPassword.getText().toString();

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://yash.testproject.life/SASystem_editTeacherProfile.php?" +
                                    "teachername=" + updatedTeacherName +
                                    "&username=" + updatedUsername +
                                    "&password=" + updatedPassword +
                                    "&course=" + updatedTeacherCourse +
                                    "&year=" + updatedTeacherYear +
                                    "&oldusername=" + uname +
                                    "&oldpassword=" + pword)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();

                    if (responseString.equals("Update Success.")) {
                        Toast.makeText(getActivity(), "Update Success.", Toast.LENGTH_SHORT).show();
                        teacherName.setText(updatedTeacherName);
                        teacherCourse.setText(updatedTeacherCourse);
                        year.setText(updatedTeacherYear);
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
                dialog.dismiss();
            }
        });
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
            getData(uname, pword);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private void getData(String uname, String pword) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_getTeacherProfileData.php?username=" + uname + "&password=" + pword)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            if (response.isSuccessful()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(responseString);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String teacherNameStr = jsonObject.getString("teacherName");
                            String courseStr = jsonObject.getString("course");
                            String yearStr = jsonObject.getString("year");
                            String usernameStr = jsonObject.getString("userName");
                            String passwordStr = jsonObject.getString("password");

                            teacherName.setText(teacherNameStr);
                            teacherCourse.setText(courseStr);
                            year.setText(yearStr);
                            username.setText(usernameStr);
                            password.setText(passwordStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
