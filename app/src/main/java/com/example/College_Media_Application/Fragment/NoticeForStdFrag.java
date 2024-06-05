package com.example.College_Media_Application.Fragment;

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
import android.widget.Toast;

import com.example.College_Media_Application.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NoticeForStdFrag extends Fragment {
    EditText title,notice;
    Button send;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_notice_for_std, container, false);

        progressDialog=new ProgressDialog(getContext());
        title=view.findViewById(R.id.ET_title);
        notice=view.findViewById(R.id.ET_notice);
        send=view.findViewById(R.id.btn_send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadDataTask().execute();
            }
        });
        return view;
    }
    public void sendNotice(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String formattedDate = sdf.format(calendar.getTime());

        String Title=title.getText().toString().trim();
        String Notice=notice.getText().toString().trim();
        if (!(Title.isEmpty() && Notice.isEmpty())) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://yash.testproject.life/SASystem_sendNotice.php?title=" + Title + "&notice=" + Notice + "&date="+formattedDate)
                        .build();
                Response response4 = client.newCall(request).execute();
                String responseString4 = response4.body().string();

                if (responseString4.equalsIgnoreCase("Missing Parameters.")) {
                    Toast.makeText(getContext(), "Missing Parameters.", Toast.LENGTH_SHORT).show();
                }
                if (responseString4.equalsIgnoreCase("success.")) {
                    title.setText("");
                    notice.setText("");
                    Toast.makeText(getContext(), "Send success.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error: Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            Toast.makeText(getContext(), "Please Enter Title and Notice ..", Toast.LENGTH_SHORT).show();
        }
    }
    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    sendNotice();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressDialog.dismiss();
        }
    }
}