package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    private static class ViewHolder {
        TextView tvUserName;
        TextView tvBody;
        TextView tvRelativeTime;
        ImageView ivProfileImage;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        User user = tweet.getUser();
        
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        
        
        viewHolder.tvUserName.setText("@" + user.getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(viewHolder.ivProfileImage);
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String userScreenName = viewHolder.tvUserName.getText().toString();
                Log.d("DEBUG", userScreenName);
            }
        });
        
        return convertView;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
