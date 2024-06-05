package com.example.College_Media_Application.Fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.example.College_Media_Application.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AdminProfileFrag extends Fragment {

    TextView name, username,password;
    Button edit;
    String uname,pword;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin_profile, container, false);

        name=view.findViewById(R.id.ET_PName);
        username=view.findViewById(R.id.ET_Username);
        password=view.findViewById(R.id.ET_password);
        edit=view.findViewById(R.id.btn_edit);
         progressDialog=new ProgressDialog(getContext());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });


        Bundle args = getArguments();
        if (args != null) {
            uname = args.getString("uname1");
            pword = args.getString("pword1");


            System.out.println("UserName is: "+uname);
            System.out.println("Password is: "+pword);
        }
        new load().execute();

        return view;
    }

    public void getProfileData(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://yash.testproject.life/SASystem_getPrincipleProfileData.php?username=" + uname + "&password=" + pword )
                    .build();

            Response response4 = client.newCall(request).execute();
            String responseString4 = response4.body().string();

            if (responseString4.equals("Fail")) {
                Toast.makeText(getContext(), "Failed to retrieve profile data", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray contacts = new JSONArray(responseString4);
            for (int i = 0; i < contacts.length(); i++)
            {
                JSONObject c = contacts.getJSONObject(i);
                String  Name = c.getString("name");
                String  userName = c.getString("username");
                String  Password = c.getString("password");
                name.setText(Name);
                username.setText(userName);
                password.setText(Password);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showEditDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.dailog_admin_edit, null);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        editTextName.setText(name.getText());
        editTextUsername.setText(username.getText());
        editTextPassword.setText(password.getText());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                String Name=editTextName.getText().toString();
                String Username=editTextUsername.getText().toString();
                String Password=editTextPassword.getText().toString();
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://yash.testproject.life/SASystem_editPrincipleProfile.php?" +
                                    "name=" + Name +
                                    "&username=" + Username +
                                    "&password=" + Password+
                                    "&oldusername=" + uname+
                                    "&oldpassword=" + pword
                                    )
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();

                    if (responseString.equals("Update Success.")) {
                        Toast.makeText(getActivity(), "Update Success.", Toast.LENGTH_SHORT).show();
                        name.setText(editTextName.getText());
                        username.setText(editTextUsername.getText());
                        password.setText(editTextPassword.getText());
                        progressDialog.dismiss();
                        dialog.dismiss();
                    } else if (responseString.equalsIgnoreCase("Missing Parameters.")) {
                        Toast.makeText(getActivity(), "Missing Parameters.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Error: Something went wrong.", Toast.LENGTH_SHORT).show();
                       progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Canceled..", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    public class load extends AsyncTask<Void,Void,Void> {

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
                    getProfileData();
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