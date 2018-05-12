package com.example.bexuma.apollographqlrailsapponheroku;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bexuma.apollographqlrailsapponheroku.authentication.SignInFragment;
import com.example.bexuma.apollographqlrailsapponheroku.authentication.SignUpFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.PostFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.PostsListFragment;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setMainActivity(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentHolder = fragmentManager.findFragmentById(R.id.fragmentHolder);

        if (fragmentHolder == null) {
            fragmentHolder = new PostsListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentHolder, fragmentHolder)
                    .commit();
        }

    }

    public void openSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, signUpFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, signInFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openPostFragment(Post post) {
        PostFragment postFragment = PostFragment.newInstance(post.getTitle(), post.getContent());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, postFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openPostsListFragment() {
        PostsListFragment postsListFragment = new PostsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentHolder, postsListFragment)
                .addToBackStack(null)
                .commit();
    }
}

