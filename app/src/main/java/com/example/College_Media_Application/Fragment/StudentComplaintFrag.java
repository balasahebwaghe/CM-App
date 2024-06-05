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


import com.example.College_Media_Application.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.College_Media_Application.Model.*;
import com.example.College_Media_Application.Adapter.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StudentComplaintFrag extends Fragment {

    RecyclerView recyclerView;
    StdComplaintAdapter stdComplaintAdapter;
    List<StdCompaintModel> stdCompaintModels;
    //ProgressBar progressBar;
    ProgressDialog progressDialog;
    ImageView no_data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_student_complaint, container, false);

        recyclerView = view.findViewById(R.id.recview);
        progressDialog=new ProgressDialog(getContext());
        no_data=view.findViewById(R.id.no_data_img);
        stdCompaintModels=new ArrayList<>();

        new FetchDataTask().execute();

        return view;
    }
    class FetchDataTask extends AsyncTask<Void, Void, List<StdCompaintModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
            }
        }
        @Override
        protected List<StdCompaintModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_getStdComplaint.php")
                        .build();
                Response response = client.newCall(request).execute();

                Log.e("re",response.toString());
                //System.out.println("Response from PHP : "+response.toString());
                if (response.code() == 200) {
                    String responseString = response.body().string();
                    JSONArray contacts = new JSONArray(responseString);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        StdCompaintModel stdCompaintModel = new StdCompaintModel();

                        stdCompaintModel.setFacultyName(c.getString("faculty").toString());
                        stdCompaintModel.setComplaint(c.getString("complaint").toString());
                        stdCompaintModel.setDate(c.getString("date").toString());
                        stdCompaintModels.add(stdCompaintModel);

                    }
                } else {
                    Log.e("Present", "Server response code: " + response.code());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.e("Present", "Error: " + e.getMessage());
            }
            return stdCompaintModels;
        }

        @Override
        protected void onPostExecute(List<StdCompaintModel> result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result != null && !result.isEmpty()) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                stdComplaintAdapter = new StdComplaintAdapter(result, getContext());
                recyclerView.setAdapter(stdComplaintAdapter);
            } else {
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_LONG).show();
            }
        }
    }
}