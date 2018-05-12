package com.example.bexuma.apollographqlrailsapponheroku.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.UserLocalStore;
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

    public static UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setMainActivity(this);
        userLocalStore = new UserLocalStore(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentHolder = fragmentManager.findFragmentById(R.id.fragmentHolder);

        if (fragmentHolder == null) {
            fragmentHolder = new PostsListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentHolder, fragmentHolder)
                    .commit();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entry, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logout() {
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);

        Intent startEntryActivity = new Intent(this, EntryActivity.class);

        startEntryActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(startEntryActivity);
        finish();
    }
}

