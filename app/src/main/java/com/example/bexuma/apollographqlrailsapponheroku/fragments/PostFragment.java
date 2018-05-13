package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    private static final String ARG_POST = "post";


    private Post post;


    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post   Post.
     * @return A new instance of fragment PostFragment.
     */
    public static PostFragment newInstance(Post post) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST, post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = getArguments().getParcelable(ARG_POST);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        TextView titleTextView = v.findViewById(R.id.title);
        TextView contentTextView = v.findViewById(R.id.content);

        titleTextView.setText(post.getTitle());
        contentTextView.setText(post.getContent());
        return v;
    }

}
