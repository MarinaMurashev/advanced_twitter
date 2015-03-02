package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utility.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment{
    protected ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    protected ListView lvTweets;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Tweet last_tweet =  tweets.get(tweets.size() - 1);
                long last_tweet_id = last_tweet.getUid();
                populateTimeline(last_tweet_id);

            }
        });
        
        return v;
    }

    protected abstract void populateTimeline(long maxId);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }
    
    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }
}

