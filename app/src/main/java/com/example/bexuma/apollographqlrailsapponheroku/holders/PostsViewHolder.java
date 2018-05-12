package com.example.bexuma.apollographqlrailsapponheroku.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bexuma.apollographqlrailsapponheroku.AllPostsQuery;
import com.example.bexuma.apollographqlrailsapponheroku.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

public class PostsViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView contentTextView;

    private View postEntryContainer;

    public PostsViewHolder(View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.post_title);
        contentTextView = itemView.findViewById(R.id.post_content);
        postEntryContainer = itemView.findViewById(R.id.post_entry_container);
    }

    public void updateUI(Post post) {
        titleTextView.setText(post.getTitle());
        contentTextView.setText(post.getContent());


    }

    public void setPost(final AllPostsQuery.AllPost post) {
        titleTextView.setText(post.title());
        contentTextView.setText(post.content());

        postEntryContainer.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MainActivity.getMainActivity().openPostFragment(post.title(), post.content());
            }
        });

    }
}
