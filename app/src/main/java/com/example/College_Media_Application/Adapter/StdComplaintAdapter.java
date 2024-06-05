package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.College_Media_Application.Model.*;
import com.example.College_Media_Application.R;

import java.util.List;

public class StdComplaintAdapter extends RecyclerView.Adapter<StdComplaintAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<StdCompaintModel> stdCompaintModels;
    Context context;

    public StdComplaintAdapter(List<StdCompaintModel>stdCompaintModels, Context context)
    {
        this.stdCompaintModels = stdCompaintModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    public void SetFilteredList(List<StdCompaintModel> filteredList){
        this.stdCompaintModels = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StdComplaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.stdcomplaint,parent,false);
        return new StdComplaintAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull StdComplaintAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        StdCompaintModel temp = stdCompaintModels.get(position);
        holder.faculty_name.setText(temp.getFacultyName());
        holder.complaint.setText(temp.getComplaint());
        holder.date.setText(temp.getDate());
    }
    @Override
    public int getItemCount() {
        return stdCompaintModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView faculty_name,complaint,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faculty_name =itemView.findViewById(R.id.name);
            complaint =itemView.findViewById(R.id.complaint);
            date=itemView.findViewById(R.id.date);
        }
    }
}
