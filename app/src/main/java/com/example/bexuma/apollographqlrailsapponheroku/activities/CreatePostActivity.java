package com.example.bexuma.apollographqlrailsapponheroku.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.bexuma.apollographqlrailsapponheroku.CreatePostMutation;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.models.User;

import javax.annotation.Nonnull;

public class CreatePostActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private String title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.post_title);
        contentEditText = findViewById(R.id.post_content);
        Button createPost = findViewById(R.id.buttonCreatePost);

        createPost.setOnClickListener(v -> {
            if (MainActivity.userLocalStore.getUserLoggedIn()) {
                title = titleEditText.getText().toString();
                content = contentEditText.getText().toString();
                createPost(title, content);
            } else {
                Toast.makeText(MainActivity.getMainActivity(), "User is not signed in", Toast.LENGTH_LONG).show();
            }


        });


    }

    private ApolloCall.Callback<CreatePostMutation.Data> createPostMutationCallback = new ApolloCall.Callback<CreatePostMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreatePostMutation.Data> response) {
            CreatePostMutation.Data data = response.data();

            assert data != null;

            if (data.createPost() != null) {
                Log.d("CreatePostActivity", "onResponse: " + data.createPost());
                CreatePostActivity.this.runOnUiThread(() -> {
                    Intent myIntent = new Intent(CreatePostActivity.this, MainActivity.class);
                    CreatePostActivity.this.startActivity(myIntent);
                    MainActivity.getMainActivity().openPostsListFragment();
                    Toast.makeText(MainActivity.getMainActivity(), "Post was successfully created!", Toast.LENGTH_LONG).show();
                });
            }

        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d("CreatePostActivity", "error: " + e.getLocalizedMessage());
        }
    };

    private void createPost(String title, String content) {

        MyApolloClient.getAuthorizedApolloClient(MainActivity.userLocalStore.getUserToken()).mutate(CreatePostMutation.builder()
                .title(title)
                .content(content)
                .build()).enqueue(createPostMutationCallback);
    }
}
