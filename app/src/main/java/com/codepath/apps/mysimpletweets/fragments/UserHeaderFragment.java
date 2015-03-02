package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class UserHeaderFragment extends Fragment{
    private TwitterClient client;
    private User user;
    TextView tvUserName;
    TextView tvTagline;
    ImageView ivProfilePicture;
    TextView tvFollowers;
    TextView tvFollowing;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_header, parent, false);

        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvTagline = (TextView) v.findViewById(R.id.tvTagline);
        ivProfilePicture = (ImageView) v.findViewById(R.id.ivProfilePicture);
        tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) v.findViewById(R.id.tvFollowing);
        
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        populateHeader();
    }

    public static UserHeaderFragment newInstance(String screenName) {
        UserHeaderFragment userFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateHeader() {
        String screenName = getArguments().getString("screenName");
        
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
//                getActivity().getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    private void populateProfileHeader(User user) {
        tvUserName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowers() + " Followers");
        tvFollowing.setText(user.getFollowing() + " Following");
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfilePicture);
    }
}
