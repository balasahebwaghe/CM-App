package com.example.College_Media_Application.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.College_Media_Application.Adapter.NoticeAdapter;
import com.example.College_Media_Application.R;

public class ViewNotes extends Fragment {

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private List<String> filenames;
    private Handler handler;
    Button search,all;
    Spinner year,sem,branch;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_notes, container, false);
        getActivity().setTitle("View Notes ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        year = view.findViewById(R.id.yearSpinner);
        sem = view.findViewById(R.id.semesterSpinner);
        branch = view.findViewById(R.id.branchSpinner);
        search = view.findViewById(R.id.btn_search);
        all = view.findViewById(R.id.btn_all);

        ArrayAdapter<CharSequence> Tadapter = ArrayAdapter.createFromResource(getContext(), R.array.year, android.R.layout.simple_spinner_item);
        Tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(Tadapter);

        ArrayAdapter<CharSequence> Tadapter2 = ArrayAdapter.createFromResource(getContext(), R.array.sem, android.R.layout.simple_spinner_item);
        Tadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem.setAdapter(Tadapter2);

        ArrayAdapter<CharSequence> Tadapter3 = ArrayAdapter.createFromResource(getContext(), R.array.branch, android.R.layout.simple_spinner_item);
        Tadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(Tadapter3);

        filenames = new ArrayList<>();
        adapter = new NoticeAdapter(filenames, getContext());
        recyclerView.setAdapter(adapter);
        handler = new Handler();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Year = year.getSelectedItem().toString().trim();
                String Branch = branch.getSelectedItem().toString().trim();
                String Sem = sem.getSelectedItem().toString().trim();

                if (Year.equals("Select year")) {
                    showToast("Please select year.");
                } else if (Branch.equals("Select branch")) {
                    showToast("Please Select branch");
                } else if (Sem.equals("Select sem")) {
                    showToast("Please select sem");
                } else {
                    startFetchingPostsUsingParam(Year, Branch, Sem);
                }
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFetchingPosts();
            }
        });

        startFetchingPosts();

        return view;
    }

    private void startFetchingPostsUsingParam(String Year, String Branch, String Sem) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchPostsUsingParam(Year, Branch, Sem);
            }
        }, 300);
    }

    private void startFetchingPosts() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchPosts();
            }
        }, 300);
    }

    private void fetchPosts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://yash.testproject.life/filename.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    showToast("Failed to fetch data: " + response.message());
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    getActivity().runOnUiThread(() -> {
                        try {
                            updateAdapter(jsonArray);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    showToast("Error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void fetchPostsUsingParam(String year, String branch, String sem) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://yash.testproject.life/SASystem_getFilenameUseParam.php?year=" + year + "&branch=" + branch + "&sem=" + sem)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    showToast("Failed to fetch data: " + response.message());
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    getActivity().runOnUiThread(() -> {
                        try {
                            updateAdapter(jsonArray);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    showToast("Error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    private void updateAdapter(JSONArray jsonArray) throws JSONException {
        filenames.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String filename = jsonObject.getString("filename");
                filenames.add(filename);
            } catch (JSONException e) {
                e.printStackTrace();
                JSONObject jsonObject = new JSONObject((Map) jsonArray);
                String message = jsonObject.getString("message");
                showToast(message);

            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(getContext(), "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
