package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.clients.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends ActionBarActivity {
    private EditText etBody;
    private ImageView imProfilePicture;
    private TextView tvScreenName;
    private TextView tvUserName;
    private TextView tvCharsRemaining;
    private Button bSubmit;
    private TwitterClient client;
    public static final String TWEET_EXTRA = "tweet";
    private static final int MAX_CHARACTERS = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        setActionBarIcon();
        
        etBody = (EditText) findViewById(R.id.etBody);
        imProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvScreenName = (TextView) findViewById(R.id.tvTagline);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvCharsRemaining = (TextView) findViewById(R.id.tvCharsRemaining);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        
        client = TwitterApplication.getRestClient();
        fillInUserInfo();
        enforceCharacterLimit();
    }

    private void setActionBarIcon(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
    
    private void enforceCharacterLimit(){
        tvCharsRemaining.setText(Integer.toString(MAX_CHARACTERS));
        
        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int remainingCharacters = MAX_CHARACTERS - s.length();

                if(remainingCharacters >= 0) {
                    bSubmit.setEnabled(true);
                    tvCharsRemaining.setText(Integer.toString(remainingCharacters));
                    tvCharsRemaining.setTextColor(Color.parseColor("#000000"));
                } else {
                    tvCharsRemaining.setText(Integer.toString(remainingCharacters));
                    tvCharsRemaining.setTextColor(Color.parseColor("#A00000"));
                    bSubmit.setEnabled(false);
                }
            }
        });  
    }

    private void fillInUserInfo() {
        client.getCurrentUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJson(response);
                Picasso.with(getBaseContext()).load(user.getProfileImageUrl()).into(imProfilePicture);
                tvScreenName.setText("@" + user.getScreenName());
                tvUserName.setText(user.getName());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "getting user info failed");
            }
        });
    }

    public void onSubmitTweet(View view) {
        client.postTweet(etBody.getText().toString(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJSON(response);

                Intent i = new Intent();
                i.putExtra(TWEET_EXTRA, tweet);
                setResult(RESULT_OK, i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    
    
}
