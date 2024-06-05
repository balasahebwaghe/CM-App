/*
package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.College_Media_Application.*;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
public class NoticeAdapter  extends RecyclerView.Adapter<NoticeAdapter.PostViewHolder> {
    private List<String> filenames;
    private Context context;

    public NoticeAdapter(List<String> filenames, Context context) {
        this.filenames = filenames;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpdf, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String filename = filenames.get(position);
        holder.bind(filename);
    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView filenameTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            filenameTextView = itemView.findViewById(R.id.filenameTextView);
        }

        public void bind(String filename) {
            filenameTextView.setText(filename);
            filenameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //downloadFile(filename);
                    new DownloadFileTask().execute(filename);
                }
            });
        }

        */
/*private void downloadFile(String filename) {
            try {
                String fileUrl = "https://yash.testproject.life/pdf/" + filename;
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                request.setTitle(filename);
                request.setDescription("Downloading file...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                if (downloadManager != null) {
                    downloadManager.enqueue(request);
                    Toast.makeText(context, "Downloading file: " + filename, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Download Manager not available", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, "Error downloading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }*//*

        */
/*private class DownloadFileTask extends AsyncTask<String, Void, Boolean> {
            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.show();
            }
            @Override
            protected Boolean doInBackground(String... params) {
                String filename = params[0];
                try {
                    String fileUrl = "https://yash.testproject.life/pdf/" + filename;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setTitle(filename);
                    request.setDescription("Downloading file...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                        return true; // Download started successfully
                    } else {
                        // Handle download manager not available
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false; // Download failed
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                if (success) {
                    // File download started successfully, wait for completion
                    // Don't dismiss progressDialog yet
                    Toast.makeText(context, "Downloading started...", Toast.LENGTH_SHORT).show();
                } else {
                    // Download failed, dismiss progressDialog
                    progressDialog.dismiss();
                    Toast.makeText(context, "File download failed", Toast.LENGTH_SHORT).show();
                }
            }
        }*//*

        */
/*private class DownloadFileTask extends AsyncTask<String, Integer, Void> {
            private ProgressDialog progressDialog;
            private long downloadReference;
            private BroadcastReceiver receiver;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                // Initialize the receiver
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (downloadReference == reference) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "File download completed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // Register receiver to listen for download status updates
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(receiver, filter);
            }
            @Override
            protected Void doInBackground(String... params) {
                String filename = params[0];
                try {
                    String fileUrl = "https://yash.testproject.life/pdf/" + filename;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setTitle(filename);
                    request.setDescription("Downloading file...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadReference = downloadManager.enqueue(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Unregister receiver
                if (receiver != null) {
                    context.unregisterReceiver(receiver);
                    receiver = null;
                }
            }
        }*//*

        */
/*private class DownloadFileTask extends AsyncTask<String, Integer, Void> {
            private ProgressDialog progressDialog;
            private long downloadReference;
            private BroadcastReceiver receiver;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();

                // Initialize the receiver
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (downloadReference == reference) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "File download completed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(receiver, filter);
            }

            @Override
            protected Void doInBackground(String... params) {
                String filename = params[0];
                try {
                    String fileUrl = "http://yash.testproject.life/pdf/" + filename;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setTitle(filename);
                    request.setDescription("Downloading file...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadReference = downloadManager.enqueue(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (receiver != null) {
                    context.unregisterReceiver(receiver);
                    receiver = null;
                }
            }
        }
*//*




        private class DownloadFileTask extends AsyncTask<String, Integer, Void> {
            private ProgressDialog progressDialog;
            private long downloadReference;
            private BroadcastReceiver receiver;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.d("DownloadFileTask", "onPreExecute called");
                // Initialize the receiver
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (downloadReference == reference) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "File download completed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // Register receiver to listen for download status updates
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(receiver, filter);


                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDownload();
                    }
                });

            }
            @SuppressLint("Range")
            @Override
            protected Void doInBackground(String... params) {
                String filename = params[0];
                try {
                    String fileUrl = "http://yash.testproject.life/pdf/" + filename;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setTitle(filename);
                    request.setDescription("Downloading file...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadReference = downloadManager.enqueue(request);
                        boolean downloading = true;
                        while (downloading) {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(downloadReference);
                            Cursor cursor = null;
                            try {
                                cursor = downloadManager.query(q);
                                if (cursor != null && cursor.moveToFirst()) {
                                    int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                    int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                        downloading = false;
                                        publishProgress(100);
                                    } else {
                                        int progress = (int) ((bytesDownloaded * 100l) / bytesTotal);
                                        publishProgress(progress);
                                        Thread.sleep(1000); // Adjust the interval as needed
                                    }
                                } else {
                                    // Handle the case when the cursor is null or empty
                                    downloading = false;
                                }
                            } finally {
                                if (cursor != null) {
                                    cursor.close(); // Close the cursor in a finally block
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                // Update progress in the dialog
                Log.d("DownloadFileTask", "Progress update: " + values[0]);
                progressDialog.setProgress(values[0]);
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // Unregister receiver
                if (receiver != null) {
                    context.unregisterReceiver(receiver);
                    receiver = null;
                }
            }
        }
    }

}

*/
package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.College_Media_Application.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.PostViewHolder> {
    private List<String> filenames;
    private Context context;

    public NoticeAdapter(List<String> filenames, Context context) {
        this.filenames = filenames;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpdf, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        String filename = filenames.get(position);
        holder.bind(filename);
    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView filenameTextView;
        private DownloadFileTask downloadFileTask;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            filenameTextView = itemView.findViewById(R.id.filenameTextView);
        }

        public void bind(String filename) {
            filenameTextView.setText(filename);
            filenameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to download this file?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    downloadFileTask = new DownloadFileTask();
                                    downloadFileTask.execute(filename);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    builder.create().show();
                }
            });
        }

        private class DownloadFileTask extends AsyncTask<String, Integer, Void> {
            private ProgressDialog progressDialog;
            private long downloadReference;
            private BroadcastReceiver receiver;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Downloading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDownload();
                    }
                });
                progressDialog.show();

                // Initialize the receiver
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                        if (downloadReference == reference) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "File download completed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // Register receiver to listen for download status updates
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(receiver, filter);
            }

            @SuppressLint("Range")
            @Override
            protected Void doInBackground(String... params) {
                String filename = params[0];
                try {
                    String fileUrl = "http://yash.testproject.life/pdf/" + filename;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                    request.setTitle(filename);
                    request.setDescription("Downloading file...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (downloadManager != null) {
                        downloadReference = downloadManager.enqueue(request);
                        boolean downloading = true;
                        while (downloading) {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(downloadReference);
                            Cursor cursor = null;
                            try {
                                cursor = downloadManager.query(q);
                                if (cursor != null && cursor.moveToFirst()) {
                                    int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                    int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                        downloading = false;
                                        publishProgress(100);
                                    } else {
                                        int progress = (int) ((bytesDownloaded * 100l) / bytesTotal);
                                        publishProgress(progress);
                                        Thread.sleep(1000); // Adjust the interval as needed
                                    }
                                } else {
                                    // Handle the case when the cursor is null or empty
                                    downloading = false;
                                }
                            } finally {
                                if (cursor != null) {
                                    cursor.close(); // Close the cursor in a finally block
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                // Update progress in the dialog
                progressDialog.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Unregister receiver
                if (receiver != null) {
                    context.unregisterReceiver(receiver);
                    receiver = null;
                }
            }

            private void cancelDownload() {
                if (downloadReference != 0) {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.remove(downloadReference);
                    progressDialog.dismiss();
                    Toast.makeText(context, "Download cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
