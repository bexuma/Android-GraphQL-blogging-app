package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.bexuma.apollographqlrailsapponheroku.AllPostsQuery;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.adapters.PostsAdapter;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class PostsListFragment extends Fragment {




    public PostsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.posts_recycler);
        recyclerView.setHasFixedSize(true);

        PostsAdapter adapter = new PostsAdapter(getPosts());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public ArrayList<Post> getPosts() {


        ArrayList<Post> list = new ArrayList<>();

        list.add(new Post("1st post", "sample sample"));
        list.add(new Post("Hakuna Matata", "sample sample sample"));



        MyApolloClient.getMyApolloClient().query(AllPostsQuery.builder().build()).enqueue(new ApolloCall.Callback<AllPostsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AllPostsQuery.Data> response) {
                AllPostsQuery.Data data = response.data();
                Log.d("PostsListFragment", "onResponse: " + data.allPosts());
                String title, content;

                for (int i = 0; i < data.allPosts().size(); i++) {
                    title = data.allPosts().get(i).title();
                    content = data.allPosts().get(i).content();
                    list.add(new Post(title, content));
                }


            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.d("PostsListFragment", "onResponse: " + e.getLocalizedMessage());
            }
        });

        return list;
    }

}
