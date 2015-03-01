package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends ActionBarActivity {
    private static final int COMPOSE_REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setActionBarIcon();

//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//               Tweet last_tweet =  tweets.get(tweets.size() - 1);
//               long last_tweet_id = last_tweet.getUid();
//               populateTimeline(last_tweet_id);
//            }
//        });
    }

    private void setActionBarIcon(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getSerializableExtra(ComposeTweetActivity.TWEET_EXTRA);

//            aTweets.insert(tweet, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_compose) {
            Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
            startActivityForResult(i, COMPOSE_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }
}
