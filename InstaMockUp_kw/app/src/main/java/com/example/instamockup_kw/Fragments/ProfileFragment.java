package com.example.instamockup_kw.Fragments;

import static com.parse.Parse.getApplicationContext;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instamockup_kw.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostFragment{

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Posts: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        adapter.clear();
                        Toast.makeText(getApplicationContext(), "Works!", Toast.LENGTH_LONG).show();
                        // To keep animation for 4 seconds
                        //noinspection deprecation
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addAll(posts);
                                // Stop animation (This will be after 3 seconds)
                                swipeContainer.setRefreshing(false);
                            }
                        }, 4000);
                    }
                });
            }
        });
    }
}
