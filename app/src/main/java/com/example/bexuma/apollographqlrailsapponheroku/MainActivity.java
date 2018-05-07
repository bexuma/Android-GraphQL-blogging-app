package com.example.bexuma.apollographqlrailsapponheroku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    private TextView title1, title2, content1, content2;
    private String t1, t2, c1, c2;
    private EditText mTitle, mContent;
    private Button button, registerBtn;

    private EditText mName, mEmail, mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);
        content1 = findViewById(R.id.content1);
        content2 = findViewById(R.id.content2);
        mTitle = findViewById(R.id.editTextTitle);
        mContent = findViewById(R.id.editTextContent);
        button = findViewById(R.id.button);

        mName = findViewById(R.id.editTextName);
        mPassword = findViewById(R.id.editTextPwd);
        mEmail = findViewById(R.id.editTextEmail);
        registerBtn = findViewById(R.id.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApolloClient.getMyApolloClient().mutate(CreatePostMutation.builder()
                        .title(mTitle.getText().toString())
                        .content(mContent.getText().toString()).build())
                        .enqueue(new ApolloCall.Callback<CreatePostMutation.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<CreatePostMutation.Data> response) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Post created!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {

                            }
                        });
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApolloClient.getMyApolloClient().mutate(CreateUserMutation.builder()
                .name(mName.getText().toString())
                .email(mEmail.getText().toString())
                .password(mPassword.getText().toString()).build())
                        .enqueue(new ApolloCall.Callback<CreateUserMutation.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<CreateUserMutation.Data> response) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "User created!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
            }
        });



        getPosts();
    }

    private void getPosts() {
        MyApolloClient.getMyApolloClient().query(
                AllPostsQuery.builder().build()).enqueue(new ApolloCall.Callback<AllPostsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AllPostsQuery.Data> response) {
                Log.d("Somesome", "onResponse: " + response.data().allPosts().get(1).content());

                t1 = response.data().allPosts().get(0).title();
                t2 = response.data().allPosts().get(1).title();
                c1 = response.data().allPosts().get(0).content();
                c2 = response.data().allPosts().get(1).content();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        title1.setText(t1);
                        title2.setText(t2);
                        content1.setText(c1);
                        content2.setText(c2);
                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

}
