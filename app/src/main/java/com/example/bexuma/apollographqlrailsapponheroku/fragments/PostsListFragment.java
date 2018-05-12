package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;
import com.example.bexuma.apollographqlrailsapponheroku.AllPostsQuery;
import com.example.bexuma.apollographqlrailsapponheroku.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.adapters.PostsAdapter;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class PostsListFragment extends Fragment {

    private ArrayList<Post> postList = new ArrayList<>();

    ApolloClient myApolloClient;
    ViewGroup content;
    ProgressBar progressBar;
    PostsAdapter adapter;

    public PostsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        Log.d(MyApolloClient.TAG, "Creating apollo activity...");

        myApolloClient = MyApolloClient.getMyApolloClient();

        content = view.findViewById(R.id.posts_holder);
        progressBar = view.findViewById(R.id.loading_bar);

        RecyclerView recyclerView = view.findViewById(R.id.posts_list_recycler);
        recyclerView.setHasFixedSize(true);

        adapter = new PostsAdapter(postList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.getMainActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        fetchPosts();
        return view;
    }

    private ApolloCall.Callback<AllPostsQuery.Data> allPostsQueryCallback = new ApolloCall.Callback<AllPostsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<AllPostsQuery.Data> dataResponse) {
            AllPostsQuery.Data data = dataResponse.data();

            assert data != null;
            Log.d(MyApolloClient.TAG, "Received posts: " + data.allPosts().size());

            MainActivity.getMainActivity().runOnUiThread(() -> {

                for (int i = 0; i < data.allPosts().size(); i++) {
                    Post post = new Post(data.allPosts().get(i).title(), data.allPosts().get(i).content());
                    postList.add(post);
                }

                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            });

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(MyApolloClient.TAG, "Error:" + e.toString());
        }
    };

    private void fetchPosts() {
        Log.d(MyApolloClient.TAG, "Fetch posts ....");
        myApolloClient.query(
                AllPostsQuery.builder()
                        .build()
        ).httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
                .enqueue(allPostsQueryCallback);
    }

}
