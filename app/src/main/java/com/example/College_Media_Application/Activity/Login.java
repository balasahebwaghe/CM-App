package com.example.College_Media_Application.Activity;
import static android.graphics.Color.blue;

import com.example.College_Media_Application.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity
{
    public  static  String PREFS_NAME="MyPrefsFile";
    EditText uname,pword;
    TextView text,link;
    RadioButton admin,student,teacher,hod;
    RadioGroup group;
    CardView btnlogin;
    String e1;
    String e2;
    int SelectedId;

    private static final String ADMIN_LOGGED_IN_KEY = "adminLoggedIn";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        uname = findViewById(R.id.username);
        uname.requestFocus();
        text = findViewById(R.id.text);
        pword = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);
        group = findViewById(R.id.group);
        admin = findViewById(R.id.admin);
        student = findViewById(R.id.student);
        hod = findViewById(R.id.hod);
        teacher = findViewById(R.id.teacher);
       // register = findViewById(R.id.register);
        link=findViewById(R.id.link);
         SelectedId = group.getCheckedRadioButtonId();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == admin.getId()) {
                    link.setTextColor(Color.BLUE);
                } else {
                    link.setTextColor(Color.GRAY);
                }
            }
        });



       link.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int selectedId = group.getCheckedRadioButtonId();
                if (selectedId == admin.getId())
                {
                    link.setTextColor(Color.BLUE);

                    Intent intent=new Intent(Login.this, PrincipleRegistrationActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this, "It's only for Admin.", Toast.LENGTH_SHORT).show();
                    link.setTextColor(Color.GRAY);
                }
            }

        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 SelectedId = group.getCheckedRadioButtonId();
// ========================= Admin Selection =====================================
                if (SelectedId == admin.getId()) {
                    String UserName = uname.getText().toString().trim();
                    String Password = pword.getText().toString().trim();

                    if (!UserName.isEmpty() && !Password.isEmpty()) {

                        setAdminLoggedIn(true);
                        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://yash.testproject.life/SASystem_principleLogin.php?username=" + UserName + "&password=" + Password)
                                        .build();

                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseString = response.body().string();

                                    if (responseString.equalsIgnoreCase("success.")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Login.this,AdminDashbord.class);
                                                intent.putExtra("uname",UserName);
                                                intent.putExtra("pword",Password);
                                                startActivity(intent);
                                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Invalid MobileNo/Password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Server temporarily out of service", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();

                    } else {
                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                // ========================= Admin Selection End =====================================
                // ========================= Student Selection =====================================
                else if (SelectedId == student.getId()) {
                    String userName = uname.getText().toString().trim();
                    String password = pword.getText().toString().trim();

                    if (userName.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter UserName", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    } else {
                        // Show the progress dialog dynamically
                        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://yash.testproject.life/SASystem_studentLogin.php?userName=" + userName + "&password=" + password)
                                        .build();

                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseString = response.body().string();

                                    if (responseString.equalsIgnoreCase("success.")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Intent intent = new Intent(Login.this, StudentDashboard.class);
                                                intent.putExtra("uname",userName);
                                                intent.putExtra("pword",password);
                                                startActivity(intent);
                                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Invalid MobileNo/Password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Server temporarily out of service", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
// ========================= Student Selection End ====================================
//=========================== HOD Selection Start =====================================
                else if (SelectedId == hod.getId()) {
                    String userName = uname.getText().toString().trim();
                    String password = pword.getText().toString().trim();

                    if (userName.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter UserName", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    } else {

                        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();



                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://yash.testproject.life/SASystem_hodLogin.php?userName=" + userName + "&password=" + password)
                                        .build();

                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseString = response.body().string();

                                    if (responseString.equalsIgnoreCase("success.")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Intent intent = new Intent(Login.this, HODDashboard.class);
                                                intent.putExtra("uname",userName);
                                                intent.putExtra("pword",password);
                                                startActivity(intent);
                                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Invalid MobileNo/Password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Server temporarily out of service", Toast.LENGTH_SHORT).show();
                                            /*try {
                                                throw new Exception("Server temporarily out of service");
                                            } catch (Exception ex) {
                                                throw new RuntimeException("Server temporarily out of service");
                                            }*/
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
// ========================= HOD Selection End ==========================================
// ========================= Teacher Selection Start =====================================
                else if (SelectedId == teacher.getId()) {
                    String UserName = uname.getText().toString().trim();
                    String Password = pword.getText().toString().trim();

                    if (UserName.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter UserName", Toast.LENGTH_SHORT).show();
                    } else if (Password.isEmpty()) {
                        Toast.makeText(Login.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    } else {
                        // Show the progress dialog dynamically
                        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                        progressDialog.setMessage("Logging in...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://yash.testproject.life/SASystem_teacherLogin.php?userName=" + UserName + "&password=" + Password)
                                        .build();

                                try {
                                    Response response = client.newCall(request).execute();
                                    String responseString = response.body().string();

                                    if (responseString.equalsIgnoreCase("success")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();

                                                getSharedPreferences("TeacherLogin",0).edit()
                                                                .putString("UName",UserName).apply();

                                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Login.this, TeacherDashboard.class);
                                                intent.putExtra("uname",UserName);
                                                intent.putExtra("pword",Password);
                                                startActivity(intent);

                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Invalid MobileNo/Password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } catch (IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login.this, "Server temporarily out of service", Toast.LENGTH_SHORT).show();
                                            /*try {
                                                throw new Exception("Server temporarily out of service");
                                            } catch (Exception ex) {
                                                throw new RuntimeException("Server temporarily out of service");
                                            }*/
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
// ========================= Teacher Selection End =====================================
            }
            private void setAdminLoggedIn(boolean loggedIn) {
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(ADMIN_LOGGED_IN_KEY, loggedIn);
                editor.apply();
            }
            private boolean isAdminLoggedIn() {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                return prefs.getBoolean(ADMIN_LOGGED_IN_KEY, false);
            }
            private void logoutAdmin() {
                setAdminLoggedIn(false);
            }
        });

    }
}