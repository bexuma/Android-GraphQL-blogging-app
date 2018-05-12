package com.example.bexuma.apollographqlrailsapponheroku.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

public class PostsViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView contentTextView;

    public PostsViewHolder(View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.title);
        contentTextView = itemView.findViewById(R.id.content);
    }

    public void updateUI(Post post) {
        titleTextView.setText(post.getTitle());
        contentTextView.setText(post.getContent());


    }
}
