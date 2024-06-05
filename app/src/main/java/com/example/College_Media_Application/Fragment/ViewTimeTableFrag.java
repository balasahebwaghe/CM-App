package com.example.College_Media_Application.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.College_Media_Application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewTimeTableFrag extends Fragment {

    private static final String[] TIMES = {"10:00 AM - 11:00 AM", "11:00 AM - 12:00 PM", "01:00 PM - 02:00 PM", "02:00 PM - 03:00 PM", "03:00 PM - 04:00 PM", "04:00 PM - 05:00 PM"};
    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
    private ArrayList<String> subjectList;
    private LinearLayout timeLayout, dayLayout, timeTableLayout;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_time_table, container, false);

        progressDialog = new ProgressDialog(getContext());
        subjectList = new ArrayList<>();

        timeLayout = view.findViewById(R.id.LL_time);
        dayLayout = view.findViewById(R.id.LL_day);
        timeTableLayout = view.findViewById(R.id.LL_time_table);

        addTextViews(timeLayout, TIMES);
        /*addTextViews(dayLayout, DAYS);*/

        new LoadDataTask().execute();

        return view;
    }

    private void addTextViews(LinearLayout parentLayout, String[] texts) {
        for (String text : texts) {
            TextView textView = new TextView(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            textView.setLayoutParams(params);
            textView.setText(text);
            textView.setPadding(10, 10, 10, 10);
            textView.setBackgroundResource(R.drawable.border);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            parentLayout.addView(textView);
        }
    }

    private void generateTimeTable() {
        int subjectIndex = 0;
        for (String day : DAYS) {
            LinearLayout rowLayout = new LinearLayout(requireContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(params);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView dayTextView = new TextView(requireContext());
            LinearLayout.LayoutParams dayParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            dayTextView.setLayoutParams(dayParams);
            dayTextView.setText(day);
            dayTextView.setPadding(10, 10, 10, 10);
            dayTextView.setBackgroundResource(R.drawable.border);
            dayTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowLayout.addView(dayTextView);

            for (String time : TIMES) {
                TextView editText = new TextView(requireContext());
                LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                editText.setLayoutParams(editTextParams);
                if (subjectIndex < subjectList.size()) {
                    editText.setText(subjectList.get(subjectIndex));
                }
                subjectIndex++;
                editText.setPadding(10, 10, 10, 10);
                editText.setBackgroundResource(R.drawable.border);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                rowLayout.addView(editText);
            }
            timeTableLayout.addView(rowLayout);
        }
    }

    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return getTimeTable();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (result != null) {
                subjectList = result;
                generateTimeTable();
            } else {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    private ArrayList<String> getTimeTable() {
        ArrayList<String> subjects = new ArrayList<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_getTimeTable.php")
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            if (response.isSuccessful()) {
                JSONArray jsonArray = new JSONArray(responseString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    subjects.add(jsonObject.getString("subject"));
                }
                return subjects;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
