package com.example.bexuma.apollographqlrailsapponheroku.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bexuma.apollographqlrailsapponheroku.activities.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.holders.PostsViewHolder;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
    private ArrayList<Post> postList;

    public PostsAdapter(ArrayList<Post> postsList) {
        this.postList = postsList;
    }


    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.updateUI(post);

        holder.itemView.setOnClickListener(v ->
                MainActivity.getMainActivity().openPostFragment(post));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.post_item, parent, false);

        return new PostsViewHolder(itemView);

    }
}
