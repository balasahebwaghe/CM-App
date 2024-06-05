package com.example.College_Media_Application.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.College_Media_Application.Adapter.HODDetailsAdapter;
import com.example.College_Media_Application.Model.HODDetailsModel;
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


public class HODDetailsFrag extends Fragment implements HODDetailsAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    HODDetailsAdapter hodDetailsAdapter;
    List<HODDetailsModel> hodDetailsModels;
    ProgressDialog progressDialog;
    ImageView no_data;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_h_o_d_details, container, false);
        hodDetailsModels=new ArrayList<>();
        recyclerView=view.findViewById(R.id.recview);
        no_data=view.findViewById(R.id.no_data_img);
        progressDialog=new ProgressDialog(getContext());

        new FetchDataTask(this).execute();
        return view;
    }

    @Override
    public void onDeleteClick(int position) {
        // Check if the position is within the bounds of the hodDetailsModels list
        if (position >= 0 && position <= hodDetailsModels.size()) {
            HODDetailsModel modelToDelete = hodDetailsModels.get(position);
            String hodName = modelToDelete.getHodName();

            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://yash.testproject.life/SASystem_deleteHod.php?hodName=" + hodName)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            return response.body().string();
                        } else {
                            return null; // or some error message
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null; // or some error message
                    }
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    progressDialog.dismiss();
                    if (result != null && result.equals("Delete Success.")) {
                        // Remove the item from the list only if the deletion was successful
                        hodDetailsModels.remove(position);
                        hodDetailsAdapter.notifyItemRemoved(position);
                        Toast.makeText(getContext(), "Record deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete record", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        } else {
            Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditClick(int position) {
        HODDetailsModel modelToEdit = hodDetailsModels.get(position);
        System.out.println("Edit btn clicked data"+modelToEdit.toString());
        showEditDialog(modelToEdit);
    }



    private void showEditDialog(HODDetailsModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_dialog_layout, null);

        EditText hodNameEditText = dialogView.findViewById(R.id.edit_hod_name);
        EditText courseEditText = dialogView.findViewById(R.id.edit_course);
        EditText yearEditText = dialogView.findViewById(R.id.edit_year);

        String oldName = model.getHodName();
        String oldcourse = model.getCourse();
        hodNameEditText.setText(model.getHodName());
        courseEditText.setText(model.getCourse());
        yearEditText.setText(model.getYear());
        builder.setView(dialogView)
                .setTitle("")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        String newHodName = hodNameEditText.getText().toString();
                        String newCourse = courseEditText.getText().toString();
                        String newYear = yearEditText.getText().toString();
                        model.setHodName(newHodName);
                        model.setCourse(newCourse);
                        model.setYear(newYear);
                        // Perform networking operation asynchronously
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder().url("http://yash.testproject.life/SASystem_updateHod.php?hodName=" + newHodName + "&course=" + newCourse + "&year=" + newYear + "&oldHodName=" + oldName + "&oldcourse=" + oldcourse)
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();
                                    return response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                if (result != null && result.equals("Update Success.")) {
                                    Toast.makeText(getContext(), "Update success..", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                                hodDetailsAdapter.notifyDataSetChanged();
                            }
                        }.execute();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    class FetchDataTask extends AsyncTask<Void, Void, List<HODDetailsModel>> {
        private HODDetailsAdapter.OnItemClickListener mListener;

        public FetchDataTask(HODDetailsAdapter.OnItemClickListener listener) {
            this.mListener = listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            }
        }
        @Override
        protected List<HODDetailsModel> doInBackground(Void... voids) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_getHodDetails.php")
                        .build();
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    String responseString = response.body().string();
                    JSONArray contacts = new JSONArray(responseString);
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        HODDetailsModel studentDetailModel = new HODDetailsModel();

                        studentDetailModel.setHodName(c.getString("hodName").toString());
                        studentDetailModel.setCourse(c.getString("hodDepartment").toString());
                        studentDetailModel.setYear(c.getString("year").toString());
                        hodDetailsModels.add(studentDetailModel);
                    }
                } else {
                    Log.e("Present", "Server response code: " + response.code());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Log.e("Present", "Error: " + e.getMessage());
            }
            return hodDetailsModels;
        }
        @Override
        protected void onPostExecute(List<HODDetailsModel> result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (result != null && !result.isEmpty()) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                hodDetailsAdapter = new HODDetailsAdapter(result, getContext(), mListener);
                recyclerView.setAdapter(hodDetailsAdapter);
            } else {
                no_data.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "No data available", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
    }
}