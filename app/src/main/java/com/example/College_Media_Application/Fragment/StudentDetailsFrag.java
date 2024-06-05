package com.example.College_Media_Application.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.College_Media_Application.Adapter.StudentDetailAdapter;
import com.example.College_Media_Application.Model.StudentDetailModel;
import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StudentDetailsFrag extends Fragment {

    RecyclerView recyclerView;
    StudentDetailAdapter studentDetailAdapter;
    List<StudentDetailModel> studentDetailModels;
    ProgressDialog progressDialog;
    ImageView no_data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_student_details, container, false);
        no_data=view.findViewById(R.id.no_data_img);
        recyclerView=view.findViewById(R.id.recview);
        studentDetailModels=new ArrayList<>();
        progressDialog=new ProgressDialog(getContext());
        new FetchDataTask().execute();
        return view;
    }
    class FetchDataTask extends AsyncTask<Void, Void, List<StudentDetailModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
               //progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<StudentDetailModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_getSTDInfo.php")
                        .build();
                Response response = client.newCall(request).execute();

                if (response.code() == 200) {
                    String responseString = response.body().string();
                    JSONArray contacts = new JSONArray(responseString);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        StudentDetailModel studentDetailModel = new StudentDetailModel();

                        studentDetailModel.setName(c.getString("studentName").toString());
                        studentDetailModel.setCourse(c.getString("courseName").toString());
                        studentDetailModel.setYear(c.getString("year").toString());
                        studentDetailModel.setRollNo(c.getString("rollno").toString());
                        studentDetailModel.setPnumber(c.getString("Pnumber").toString());
                        studentDetailModels.add(studentDetailModel);
                    }
                } else {
                    Log.e("Present", "Server response code: " + response.code());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.e("Present", "Error: " + e.getMessage());
            }
            return studentDetailModels;
        }

        @Override
        protected void onPostExecute(List<StudentDetailModel> result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result != null && !result.isEmpty()) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                studentDetailAdapter = new StudentDetailAdapter(result, getContext());
                recyclerView.setAdapter(studentDetailAdapter);
            } else {
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "No data available", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }
    }
}