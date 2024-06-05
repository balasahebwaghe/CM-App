package com.example.College_Media_Application.Activity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.College_Media_Application.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UploadPdfActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private TextView tvSelectedPDF;
    private Uri selectedPDFUri;
    Button btnChoosePDF;
    Button btnUpload,all;
    Spinner year,sem,branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btnChoosePDF = findViewById(R.id.btnChoosePDF);
        tvSelectedPDF = findViewById(R.id.tvSelectedPDF);
        btnUpload = findViewById(R.id.btnUpload);

        year=findViewById(R.id.yearSpinner);
        sem=findViewById(R.id.semesterSpinner);
        branch=findViewById(R.id.branchSpinner);


        ArrayAdapter<CharSequence> Tadapter = ArrayAdapter.createFromResource(getApplication(), R.array.year, android.R.layout.simple_spinner_item);
        Tadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(Tadapter);

        ArrayAdapter<CharSequence> Tadapter2 = ArrayAdapter.createFromResource(getApplication(), R.array.sem, android.R.layout.simple_spinner_item);
        Tadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem.setAdapter(Tadapter2);

        ArrayAdapter<CharSequence> Tadapter3 = ArrayAdapter.createFromResource(getApplication(), R.array.branch, android.R.layout.simple_spinner_item);
        Tadapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(Tadapter3);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setupButtonClickListeners();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    private void setupButtonClickListeners() {
        btnChoosePDF.setOnClickListener(v -> pickPDFFile());

        btnUpload.setOnClickListener(v -> {
            String Year = year.getSelectedItem().toString().trim();
            String Branch = branch.getSelectedItem().toString().trim();
            String Sem = sem.getSelectedItem().toString().trim();

            if (selectedPDFUri == null) {
                showToast("Please choose a PDF file");
            } else if (Year.equals("Select year")) {
                showToast("Please select year.");
            } else if (Branch.equals("Select branch")) {
                showToast("Please Select branch");
            } else if (Sem.equals("Select sem")) {
                showToast("Please select sem");
            } else {
                uploadFile(selectedPDFUri, Year, Branch, Sem);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupButtonClickListeners();
            } else {
                showToast("Permission denied. You cannot proceed.");
            }
        }
    }

    private void pickPDFFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedPDFUri = data.getData();
            tvSelectedPDF.setText("Selected PDF: " + selectedPDFUri.getLastPathSegment());
        }
    }

    private void uploadFile(Uri fileUri,String year,String branch,String sem) {
        String realPath = getRealPathFromURI(this, fileUri);
        if (realPath == null || realPath.isEmpty()) {
            showToast("Invalid file path");
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pdfFile", getFileDisplayName(this, fileUri), RequestBody.create(MediaType.parse("application/pdf"), getFileContent(UploadPdfActivity.this, fileUri).toString()))
                .addFormDataPart("year",year)
                .addFormDataPart("branch",branch)
                .addFormDataPart("sem",sem)
                .build();

        Request request = new Request.Builder()
                .url("http://yash.testproject.life/SASystem_uploadNotes.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Upload failed!"));
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> showToast(responseData));
                } else {
                    showToast("Upload failed! Please try again later.");
                }
            }
        });
    }

    private String getRealPathFromURI(Context context, Uri uri) {
        String filePath = null;
        if (uri != null && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                //final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                filePath = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }

        Log.d("FilePath", "File Path: " + filePath);
        return filePath;
    }

    private InputStream getFileContent(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileDisplayName(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cursor.close();
            return displayName;
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                return cursor.getString(index);
            }
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
