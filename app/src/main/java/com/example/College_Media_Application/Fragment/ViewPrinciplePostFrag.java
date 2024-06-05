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
import com.example.College_Media_Application.Model.ViewPrinciplePostModel;
import com.example.College_Media_Application.Adapter.ViewPrinciplePostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewPrinciplePostFrag extends Fragment {

    RecyclerView recyclerView;
    ViewPrinciplePostAdapter viewPrinciplePostAdapter;
    List<ViewPrinciplePostModel> viewPrinciplePostModels;
    ProgressDialog progressDialog;
    ImageView no_data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_view_principle_post, container, false);
        progressDialog=new ProgressDialog(getContext());
        recyclerView=view.findViewById(R.id.recview);
        no_data=view.findViewById(R.id.no_data_img);
        viewPrinciplePostModels=new ArrayList<>();
        new FetchDataTask().execute();
        return view;
    }
    class FetchDataTask extends AsyncTask<Void, Void, List<ViewPrinciplePostModel>> {
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
        protected List<ViewPrinciplePostModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_getPrinciplePost.php")
                        .build();
                Response response = client.newCall(request).execute();

                if (response.code() == 200) {
                    String responseString = response.body().string();
                    JSONArray contacts = new JSONArray(responseString);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        ViewPrinciplePostModel viewPrinciplePostModel = new ViewPrinciplePostModel();
                        viewPrinciplePostModel.setTitle(c.getString("title").toString());
                        viewPrinciplePostModel.setPost(c.getString("post").toString());
                        viewPrinciplePostModel.setDate(c.getString("date").toString());
                        viewPrinciplePostModels.add(viewPrinciplePostModel);
                    }
                } else if(response.equals("No records found.")){
                    Toast.makeText(getContext(), "No records found.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "something went wrong.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.e("Present", "Error: " + e.getMessage());
            }
            return viewPrinciplePostModels;
        }
        @Override
        protected void onPostExecute(List<ViewPrinciplePostModel> result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result != null && !result.isEmpty()) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                viewPrinciplePostAdapter = new ViewPrinciplePostAdapter(result, getContext());
                recyclerView.setAdapter(viewPrinciplePostAdapter);
            } else {
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_LONG).show();
            }
        }
    }
}