package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.College_Media_Application.Model.AcceptTeacherLeaveModel;
import com.example.College_Media_Application.R;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AcceptTeacherLeaveAdapter extends RecyclerView.Adapter<AcceptTeacherLeaveAdapter.PostViewHolder> {
    private Context context;
    private List<AcceptTeacherLeaveModel> postList;
    private FragmentActivity activity;

    public AcceptTeacherLeaveAdapter(Context context, List<AcceptTeacherLeaveModel> postList, FragmentActivity activity) {
        this.context = context;
        this.postList = postList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AcceptTeacherLeaveModel post = postList.get(position);
        holder.bind(post);

        holder.acceptButton.setOnClickListener(v -> {
            // Update status in the database
            String phoneNumber = post.getPhone();
            String message = "Name: " + post.getTeacherName() + "\nMobile no: " + phoneNumber + "\n Your leave request has been accepted.";
            sendSMS(phoneNumber, message, position);
            updateStatusInDatabase(post.getId(), "accepted");

        });

        holder.rejectButton.setOnClickListener(v -> {
            String phoneNumber = post.getPhone();
            String message = "Name: " + post.getTeacherName() + "\nMobile no: " + phoneNumber + "\n Your leave request has been rejected.";
            sendSMS(phoneNumber, message, position);
            updateStatusInDatabase(post.getId(), "rejected");

        });
    }
    private void updateStatusInDatabase(String postId, String status) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("postId", postId);
        formBodyBuilder.add("status", status);

        Request request = new Request.Builder()
                .url("http://yash.testproject.life/SASystem_updateteacherleave.php") // Replace with your API endpoint
                .post(formBodyBuilder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    // Status updated successfully
                    // You can perform any UI updates here if needed
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Inside sendSMS method
    private void sendSMS(String phoneNumber, String message, int position) {
        if (activity != null) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                // Remove the accepted item from the list and update the UI
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postList.remove(position);
                        notifyItemRemoved(position);
                    }
                }, 200);
                Toast.makeText(activity, "Message sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, "Failed to send message", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView teacherNameTextView;
        private TextView reasonTextView;
        private TextView dateTextView;
        private TextView statusTextView;
        private TextView usernameTextView;

        private Button acceptButton;
        private Button rejectButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherNameTextView = itemView.findViewById(R.id.teacherNameTextView);
            reasonTextView = itemView.findViewById(R.id.reasonTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        public void bind(AcceptTeacherLeaveModel post) {
            teacherNameTextView.setText(post.getTeacherName());
            reasonTextView.setText(post.getReason());
            dateTextView.setText(post.getDate());
            statusTextView.setText(post.getStatus());
            usernameTextView.setText(post.getUsername());
        }
    }
}
