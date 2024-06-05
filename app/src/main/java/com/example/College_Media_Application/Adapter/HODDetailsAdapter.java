package com.example.College_Media_Application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.College_Media_Application.R;
import com.example.College_Media_Application.Model.HODDetailsModel;

import java.util.List;

public class HODDetailsAdapter extends RecyclerView.Adapter<HODDetailsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<HODDetailsModel> hodDetailsModels;
    private Context context;
    private OnItemClickListener mListener;

    public HODDetailsAdapter(List<HODDetailsModel> hodDetailsModels, Context context,OnItemClickListener listener) {
        this.hodDetailsModels = hodDetailsModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public HODDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hoddetails, parent, false);
        return new HODDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HODDetailsAdapter.ViewHolder holder, int position) {
        HODDetailsModel model = hodDetailsModels.get(position);
        if (model!=null){
            try {
                holder.hodName.setText(model.getHodName());
                holder.course.setText(model.getCourse());
                holder.year.setText(model.getYear());

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDeleteClick(position);
                }
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onEditClick(position);
                }
            }
        });
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    @Override
    public int getItemCount() {
        return hodDetailsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hodName, course, year;
        CardView btnDelete,btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hodName = itemView.findViewById(R.id.hodname);
            course = itemView.findViewById(R.id.course);
            year = itemView.findViewById(R.id.year);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnEdit=itemView.findViewById(R.id.btn_edit);
        }
    }
}
