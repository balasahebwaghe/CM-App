package com.example.College_Media_Application.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.College_Media_Application.Model.PostModel;
import com.example.College_Media_Application.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    LayoutInflater inflater;
    List<PostModel> postModels;
    Context context;

    public PostAdapter(List<PostModel>postModels, Context context)
    {
        this.postModels = postModels;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    public void SetFilteredList(List<PostModel> filteredList){
        this.postModels = filteredList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.post,parent,false);
        return new PostAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        PostModel temp = postModels.get(position);
        holder.title.setText(String.valueOf(temp.getTitle())); // Corrected order
        holder.post.setText(temp.getPost());
        holder.date.setText(temp.getDate());

    }
    @Override
    public int getItemCount() {
        return postModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,post,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            post =itemView.findViewById(R.id.post);
            date=itemView.findViewById(R.id.date);
        }
    }
}
