package com.example.College_Media_Application.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.College_Media_Application.Model.FacultyDetailsModel;
import com.example.College_Media_Application.R;

import java.util.List;
public class facultyDetailsAdapter extends RecyclerView.Adapter<facultyDetailsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<FacultyDetailsModel> facultyDetailsModels;
    private Context context;

    public facultyDetailsAdapter(List<FacultyDetailsModel> facultyDetailsModels, Context context) {
        this.facultyDetailsModels = facultyDetailsModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.teachersubject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacultyDetailsModel model = facultyDetailsModels.get(position);
        if (model!=null){
        // Bind data to TextViews
            try {
                holder.teacherName.setText(model.getTeacherName());
                holder.course.setText(model.getCourse());
                holder.year.setText(model.getYear());
                /*holder.subjectName.setText(model.getSubjectName());
                holder.subjectCode.setText(model.getSubjectCode());*/
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return facultyDetailsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName, course, year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teachername);
            course = itemView.findViewById(R.id.course);
            year = itemView.findViewById(R.id.year);
            /*subjectName = itemView.findViewById(R.id.subjectname);
            subjectCode = itemView.findViewById(R.id.subjectcode);*/
        }
    }
}

