package com.example.ogulcan.eatit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ogulcan on 14.04.2018.
 */

public class PostClass extends ArrayAdapter<String> {
    private final ArrayList<String> usermail;
    private final ArrayList<String> userImage;
    private final ArrayList<String> userComment;
    private final ArrayList<String> time;
    private final Activity context;

    public PostClass(ArrayList<String> usermail, ArrayList<String> userImage, ArrayList<String> userComment, ArrayList<String> time, Activity context) {
        super(context, R.layout.custom_view,usermail);
        this.usermail = usermail;
        this.userImage = userImage;
        this.userComment = userComment;
        this.time = time;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view,null,true);

        TextView usermailText = customView.findViewById(R.id.userName);
        TextView commentText = customView.findViewById(R.id.commentText);
        ImageView imageView = customView.findViewById(R.id.imageView3);
        TextView timeText = customView.findViewById(R.id.timeText);

        usermailText.setText(usermail.get(position));
        commentText.setText(userComment.get(position));
        Picasso.with(context).load(userImage.get(position)).into(imageView);
        timeText.setText(time.get(position));


        return customView;
    }
}

