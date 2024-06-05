package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.example.College_Media_Application.Adapter.PostAdapter;
import com.example.College_Media_Application.Model.PostModel;
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

public class ViewPostFrag extends Fragment {

    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<PostModel> postModels;
    //ProgressBar progressBar;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view=inflater.inflate(R.layout.fragment_view_post, container, false);
        recyclerView = view.findViewById(R.id.recview);
        progressDialog=new ProgressDialog(getContext());

        postModels = new ArrayList<>();

        new FetchDataTask().execute();

        return view;
    }
    class FetchDataTask extends AsyncTask<Void, Void, List<PostModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                //progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<PostModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_getTeacherPost.php")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    String responseString = response.body().string();
                    JSONArray contacts = new JSONArray(responseString);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        PostModel postModel = new PostModel();
                        postModel.setTitle(c.getString("title").toString());
                        postModel.setPost(c.getString("post").toString());
                        postModel.setDate(c.getString("date").toString());
                        postModels.add(postModel);
                    }
                } else {
                    Log.e("Present", "Server response code: " + response.code());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.e("Present", "Error: " + e.getMessage());
            }
            return postModels;
        }

        @Override
        protected void onPostExecute(List<PostModel> result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result != null && !result.isEmpty()) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                postAdapter = new PostAdapter(result, getContext());
                recyclerView.setAdapter(postAdapter);
            } else {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_LONG).show();
            }
        }
    }

}