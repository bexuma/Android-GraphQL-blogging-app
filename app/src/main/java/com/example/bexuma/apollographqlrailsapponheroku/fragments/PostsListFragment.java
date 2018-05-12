package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.os.Bundle;
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

import javax.annotation.Nonnull;


public class PostsListFragment extends Fragment {

    ApolloClient myApolloClient;
    PostsAdapter postsAdapter;
    ViewGroup content;
    ProgressBar progressBar;

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

        Log.d(MyApolloClient.TAG, "Creating apollo activity...");

        myApolloClient = MyApolloClient.getMyApolloClient();

        content = view.findViewById(R.id.posts_holder);
        progressBar = view.findViewById(R.id.loading_bar);

        RecyclerView recyclerView = view.findViewById(R.id.posts_list_recycler);
        recyclerView.setHasFixedSize(true);

        PostsAdapter adapter = new PostsAdapter();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        fetchPosts();

        return view;
    }

    private ApolloCall.Callback<AllPostsQuery.Data> allPostsQueryCallback = new ApolloCall.Callback<AllPostsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<AllPostsQuery.Data> dataResponse) {
            Log.d(MyApolloClient.TAG, "Received posts: " + dataResponse.data().allPosts().size());


            MainActivity.getMainActivity().runOnUiThread(() -> {
                postsAdapter.setPosts(dataResponse.data().allPosts());
                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
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


//    public ArrayList<Post> getPosts() {
//
//
//        ArrayList<Post> list = new ArrayList<>();
//
//        list.add(new Post("1st post", "sample sample"));
//        list.add(new Post("Hakuna Matata", "sample sample sample"));
//
//
//
//        MyApolloClient.getMyApolloClient().query(AllPostsQuery.builder().build()).enqueue(new ApolloCall.Callback<AllPostsQuery.Data>() {
//            @Override
//            public void onResponse(@Nonnull Response<AllPostsQuery.Data> response) {
//                AllPostsQuery.Data data = response.data();
//                Log.d("PostsListFragment", "onResponse: " + data.allPosts());
//                String title, content;
//
//                for (int i = 0; i < data.allPosts().size(); i++) {
//                    title = data.allPosts().get(i).title();
//                    content = data.allPosts().get(i).content();
//                    list.add(new Post(title, content));
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(@Nonnull ApolloException e) {
//                Log.d("PostsListFragment", "onResponse: " + e.getLocalizedMessage());
//            }
//        });
//
//        return list;
//    }

}
