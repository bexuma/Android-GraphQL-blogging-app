package com.example.bexuma.apollographqlrailsapponheroku.fragments;


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

    private static final String SAVED_POSTS = "Saved Posts";
    private static final String TAG = PostsListFragment.class.getSimpleName();
    private ArrayList<Post> postArrayList;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private PostsAdapter mRecyclerAdapter;
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
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        userLocalStore = new UserLocalStore(EntryActivity.getEntryActivity());

        fab.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(MainActivity.getMainActivity(), CreatePostActivity.class);
            MainActivity.getMainActivity().startActivity(myIntent);

        });

        Log.d(TAG, "PostsListFragment created");

        mRecyclerView = view.findViewById(R.id.posts_list_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(new PostsAdapter(new ArrayList<Post>()));

        if (savedInstanceState != null) {
            postArrayList = savedInstanceState.getParcelableArrayList(SAVED_POSTS);
            if (mRecyclerAdapter == null) {
                initializeAdapter();
                Log.d(TAG, "Adapter was initialized, because was null, but had been bundled.");
            } else {
                mRecyclerAdapter.notifyDataSetChanged();
                Log.d(TAG, "Adapter data set was changed, and had been bundled..");
            }
        }



        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        if (postArrayList == null || postArrayList.size() == 0) {
            fetchPosts();
        } else {
            Log.d(TAG, "Initialize adapter HA ....");
            initializeAdapter();
        }

        return view;
    }

    private void initializeAdapter() {
        mRecyclerAdapter = new PostsAdapter(postArrayList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }


    private ApolloCall.Callback<AllPostsQuery.Data> allPostsQueryCallback = new ApolloCall.Callback<AllPostsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<AllPostsQuery.Data> dataResponse) {
            AllPostsQuery.Data data = dataResponse.data();

            assert data != null;
            Log.d(TAG, "Received posts: " + data.allPosts().size());

            MainActivity.getMainActivity().runOnUiThread(() -> {
                postArrayList.clear();

                ArrayList<Post> new_posts = new ArrayList<>();

                for (int i = 0; i < data.allPosts().size(); i++) {
                    Post post = new Post(data.allPosts().get(i).title(), data.allPosts().get(i).content());
                    new_posts.add(post);
                }

                postArrayList.addAll(new_posts);

                if (mRecyclerAdapter == null) {
                    initializeAdapter();
                    Log.d(TAG, "Adapter was initialized, because was null.");
                }
                else {
                    mRecyclerAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Adapter data set was changed.");
                }
                mSwipeRefreshLayout.setRefreshing(false);

            });

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(TAG, "Error:" + e.toString());
            MainActivity.getMainActivity().runOnUiThread(() ->  mSwipeRefreshLayout.setRefreshing(false));

        }

    };

    @Override
    public void onRefresh() {
        Log.d(TAG, "Fetch posts ....");
        fetchPosts();
    }

    private void fetchPosts() {
        if (postArrayList == null) {
            postArrayList = new ArrayList<>();
        }
        mSwipeRefreshLayout.setRefreshing(true);

        MyApolloClient.getMyApolloClient(MainActivity.userLocalStore.getUserToken()).query(
                AllPostsQuery.builder()
                        .build()
        ).enqueue(allPostsQueryCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (postArrayList != null) {
            outState.putParcelableArrayList(SAVED_POSTS, postArrayList);
        }
        super.onSaveInstanceState(outState);
    }


}
