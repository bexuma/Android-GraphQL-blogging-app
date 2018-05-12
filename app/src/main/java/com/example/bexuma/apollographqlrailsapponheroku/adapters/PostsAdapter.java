package com.example.bexuma.apollographqlrailsapponheroku.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bexuma.apollographqlrailsapponheroku.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.holders.PostsViewHolder;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
    private ArrayList<Post> posts;

    public PostsAdapter(ArrayList<Post> posts) {
        this.posts = posts;

    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        final Post post = posts.get(position);
        holder.updateUI(post);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().openPostFragment(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View postItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostsViewHolder(postItem);
    }
}
