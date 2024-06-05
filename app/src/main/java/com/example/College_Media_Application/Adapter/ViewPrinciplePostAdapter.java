package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.College_Media_Application.Model.ViewPrinciplePostModel;
import com.example.College_Media_Application.R;

import java.util.List;

public class ViewPrinciplePostAdapter  extends RecyclerView.Adapter<ViewPrinciplePostAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<ViewPrinciplePostModel> viewPrinciplePostModels;
    Context context;

    public ViewPrinciplePostAdapter(List<ViewPrinciplePostModel>viewPrinciplePostModels, Context context)
    {
        this.viewPrinciplePostModels = viewPrinciplePostModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    public void SetFilteredList(List<ViewPrinciplePostModel> filteredList){
        this.viewPrinciplePostModels = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewPrinciplePostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.principlepost,parent,false);
        return new ViewPrinciplePostAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewPrinciplePostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        ViewPrinciplePostModel temp = viewPrinciplePostModels.get(position);
        holder.title.setText(String.valueOf(temp.getTitle()));
        holder.post.setText(temp.getPost());
        holder.date.setText(temp.getDate());

    }
    @Override
    public int getItemCount() {
        return viewPrinciplePostModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,post,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            post =itemView.findViewById(R.id.post);
            date = itemView.findViewById(R.id.date);

        }
    }
}
