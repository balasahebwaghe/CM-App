package com.example.College_Media_Application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.College_Media_Application.Model.StudentDetailModel;
import com.example.College_Media_Application.R;

import java.util.List;

public class StudentDetailAdapter extends RecyclerView.Adapter<StudentDetailAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<StudentDetailModel> studentDetailModels;
    Context context;
    public StudentDetailAdapter(List<StudentDetailModel> studentDetailModels, Context context) {
        this.studentDetailModels = studentDetailModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void SetFilteredList(List<StudentDetailModel> filteredList){
        this.studentDetailModels = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StudentDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.studentdeatils,parent,false);
        return new StudentDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentDetailModel temp = studentDetailModels.get(position);

        holder.phoneNumber.setText(temp.getPnumber());
        holder.name.setText(temp.getName());
        holder.course.setText(temp.getCourse());
        holder.rollno.setText(temp.getRollNo());
        holder.year.setText(temp.getYear());
    }

    @Override
    public int getItemCount() {
        return studentDetailModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,course,year,rollno,phoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.std_name);
            course =itemView.findViewById(R.id.course);
            year=itemView.findViewById(R.id.year);
            rollno=itemView.findViewById(R.id.rollno);
            phoneNumber=itemView.findViewById(R.id.Pnumber);


        }
    }
}
