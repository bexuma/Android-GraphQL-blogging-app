package com.example.bexuma.apollographqlrailsapponheroku.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bexuma.apollographqlrailsapponheroku.AllPostsQuery;
import com.example.bexuma.apollographqlrailsapponheroku.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.holders.PostsViewHolder;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsViewHolder> {
    private List<AllPostsQuery.AllPost> posts = Collections.emptyList();

    public PostsAdapter() {
    }

    public void setPosts(List<AllPostsQuery.AllPost> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
        Log.d(MyApolloClient.TAG, "Updated posts in adapter: " + posts.size());
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        final AllPostsQuery.AllPost post = this.posts.get(position);
        holder.setPost(post);



//        final Post post = posts.get(position);
//        holder.updateUI(post);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.getMainActivity().openPostFragment(post);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.post_item, parent, false);

        return new PostsViewHolder(itemView);

//
//
//        View postItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new PostsViewHolder(postItem);
    }
}
