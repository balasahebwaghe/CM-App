package com.example.College_Media_Application.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.College_Media_Application.Model.FacultyDetailsModel;
import com.example.College_Media_Application.R;
import com.example.College_Media_Application.Adapter.facultyDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FacultyDetailsFrag extends Fragment {

    RecyclerView recyclerView;
    com.example.College_Media_Application.Adapter.facultyDetailsAdapter facultyDetailsAdapter;
    List<FacultyDetailsModel> facultyDetailsModels;
    ImageView no_data;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_faculty_details, container, false);
        progressDialog=new ProgressDialog(getContext());

        no_data=view.findViewById(R.id.no_data_img);
        recyclerView = view.findViewById(R.id.recview);

        facultyDetailsModels = new ArrayList<>();

        new GetDataTask().execute();

        return view;
    }
    class GetDataTask extends AsyncTask<Void, Void, List<FacultyDetailsModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected List<FacultyDetailsModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String TUname = getActivity().getSharedPreferences("TeacherLogin",0).getString("UName","");
            List<FacultyDetailsModel> models = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_getAllFacultyDetails.php")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                JSONArray contacts = new JSONArray(responseString);
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    FacultyDetailsModel facultyDetailsModel = new FacultyDetailsModel();
                    facultyDetailsModel.setTeacherName(c.getString("teacherName"));
                    facultyDetailsModel.setCourse(c.getString("course"));
                    facultyDetailsModel.setYear(c.getString("year"));
                    models.add(facultyDetailsModel);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onPostExecute(List<FacultyDetailsModel> models) {
            super.onPostExecute(models);

            if (models.isEmpty()) {
                progressDialog.dismiss();
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_LONG).show();
            } else {
                facultyDetailsAdapter = new facultyDetailsAdapter(models, getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(facultyDetailsAdapter);
            }
            progressDialog.dismiss();
        }
    }

}