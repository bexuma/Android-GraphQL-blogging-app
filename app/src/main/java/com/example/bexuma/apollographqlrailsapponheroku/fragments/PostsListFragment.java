package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.bexuma.apollographqlrailsapponheroku.AllPostsQuery;
import com.example.bexuma.apollographqlrailsapponheroku.UserLocalStore;
import com.example.bexuma.apollographqlrailsapponheroku.activities.CreatePostActivity;
import com.example.bexuma.apollographqlrailsapponheroku.activities.EntryActivity;
import com.example.bexuma.apollographqlrailsapponheroku.activities.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.adapters.PostsAdapter;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class PostsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<Post> postList;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private PostsAdapter adapter;
    UserLocalStore userLocalStore;

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

        FloatingActionButton fab = view.findViewById(R.id.fab);

        userLocalStore = new UserLocalStore(EntryActivity.getEntryActivity());

        fab.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(MainActivity.getMainActivity(), CreatePostActivity.class);
            MainActivity.getMainActivity().startActivity(myIntent);


        });

        Log.d(MyApolloClient.TAG, "PostsListFragment created");

        recyclerView = view.findViewById(R.id.posts_list_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            fetchPosts();
        });


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

                adapter = new PostsAdapter(postList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);


            });

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(MyApolloClient.TAG, "Error:" + e.toString());
            MainActivity.getMainActivity().runOnUiThread(() ->  mSwipeRefreshLayout.setRefreshing(false));

        }

    };

    @Override
    public void onRefresh() {
        postList.clear();
        fetchPosts();

    }

    private void fetchPosts() {
        Log.d(MyApolloClient.TAG, "Fetch posts ....");
        mSwipeRefreshLayout.setRefreshing(true);
        MyApolloClient.getMyApolloClient(MainActivity.userLocalStore.getUserToken()).query(
                AllPostsQuery.builder()
                        .build()
        ).enqueue(allPostsQueryCallback);
    }


}
