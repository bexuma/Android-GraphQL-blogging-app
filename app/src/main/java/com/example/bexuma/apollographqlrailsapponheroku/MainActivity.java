package com.example.bexuma.apollographqlrailsapponheroku;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.apollographql.apollo.ApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.authentication.SignInFragment;
import com.example.bexuma.apollographqlrailsapponheroku.authentication.SignUpFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.PostFragment;
import com.example.bexuma.apollographqlrailsapponheroku.fragments.PostsListFragment;
import com.example.bexuma.apollographqlrailsapponheroku.models.Post;
import com.example.bexuma.apollographqlrailsapponheroku.models.User;

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

        if (fragmentHolder == null && User.isSignedIn()) {
            fragmentHolder = new PostsListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentHolder, fragmentHolder)
                    .commit();
        } else {
            fragmentHolder = new SignInFragment();
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
                .commitAllowingStateLoss();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

