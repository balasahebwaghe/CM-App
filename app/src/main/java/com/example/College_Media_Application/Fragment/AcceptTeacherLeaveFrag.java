// AcceptTeacherLeaveFrag.java

package com.example.College_Media_Application.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.College_Media_Application.Adapter.AcceptTeacherLeaveAdapter;
import com.example.College_Media_Application.Model.AcceptTeacherLeaveModel;

public class AcceptTeacherLeaveFrag extends Fragment {
    private RecyclerView recyclerView;
    private AcceptTeacherLeaveAdapter adapter;
    private List<AcceptTeacherLeaveModel> postList;
    private Handler handler;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean isNoDataShown = false;
    private WeakReference<FragmentActivity> activityRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_teacher_leave, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        adapter = new AcceptTeacherLeaveAdapter(getContext(), postList, getActivity());
        recyclerView.setAdapter(adapter);
        handler = new Handler();
        startFetchingPosts();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, you can now proceed with sending SMS
            // Call the method to send SMS here
        }
        return view;
    }

    private void startFetchingPosts() {
        handler.postDelayed(() -> {
            fetchPosts();
            handler.postDelayed(this::startFetchingPosts, 1000);
        }, 1000);
    }

    private void fetchPosts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://yash.testproject.life/SASystem_teacherleave.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                FragmentActivity activity = activityRef.get();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string().trim();
                        if (responseData.startsWith("No")) {
                            if (!isNoDataShown) {
                                showToast("No data available");
                                isNoDataShown = true;
                            }
                            return;
                        } else {
                            isNoDataShown = false;
                        }
                        JSONArray jsonArray = new JSONArray(responseData);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("Id");
                            String status = jsonObject.getString("status");
                            boolean isNewItem = true;
                            for (AcceptTeacherLeaveModel post : postList) {
                                if (post.getId().equals(id)) {
                                    isNewItem = false;
                                    if ("waiting".equals(status) && !post.getStatus().equals("waiting")) {
                                        post.setStatus("waiting");
                                        activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
                                    } else if ("accepted".equals(status)) {
                                        Iterator<AcceptTeacherLeaveModel> iterator = postList.iterator();
                                        while (iterator.hasNext()) {
                                            AcceptTeacherLeaveModel postToRemove = iterator.next();
                                            if (postToRemove.getId().equals(id)) {
                                                iterator.remove();
                                                activity.runOnUiThread(() -> adapter.notifyDataSetChanged());
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            if (isNewItem && "waiting".equals(status)) {
                                AcceptTeacherLeaveModel post = new AcceptTeacherLeaveModel(
                                        id,
                                        jsonObject.getString("teachername"),
                                        jsonObject.getString("reason"),
                                        jsonObject.getString("date"),
                                        status,
                                        jsonObject.getString("username"),
                                        jsonObject.getString("phone")
                                );
                                postList.add(post);
                                activity.runOnUiThread(() -> adapter.notifyItemInserted(postList.size() - 1));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        showToast("Error: " + e.getMessage());
                    }
                } else {
                    showToast("Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                showToast("Error: " + e.getMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityRef = new WeakReference<>((FragmentActivity) getActivity());
    }


    private void showToast(String message) {
        FragmentActivity activity = activityRef.get();
        if (activity != null) {
            activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now send SMS
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
