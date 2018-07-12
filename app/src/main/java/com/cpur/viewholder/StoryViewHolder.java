package com.cpur.viewholder;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpur.R;
import com.cpur.models.Story;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    private static final String DEFAULT_IMG_URI = "https://firebasestorage.googleapis.com/v0/b/cpur-1ad2a.appspot.com/o/images%2Fpizza_monster.png?alt=media&token=ef5d90e7-639c-46ae-a7e3-89df0fa6b52b";
    private final Drawable joinDraw;
    private TextView titleView;
    private ImageView statusImage;
    private TextView numClapsView;
    private GlideRequests with;
    private ImageView coverImageView;
    private Drawable readDraw;
    private Drawable fallback;



    public StoryViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.story_title);
        numClapsView = itemView.findViewById(R.id.post_num_claps);
        coverImageView = itemView.findViewById(R.id.textViewBackground);
        statusImage = itemView.findViewById(R.id.statusImage);
        with = GlideApp.with(itemView);
        readDraw = itemView.getResources().getDrawable(R.drawable.ic_glasses);
        joinDraw = itemView.getResources().getDrawable(R.drawable.join);
        fallback = itemView.getResources().getDrawable(R.drawable.pizza_monster);

    }

    public void bindToPost(Story story, View.OnClickListener starClickListener) {
        String coverImageId = story.getCoverImage();
        if (coverImageId == null){
            coverImageId = DEFAULT_IMG_URI;
        }
        Uri uri = Uri.parse(coverImageId);
        if (uri != null){
            with.load(coverImageId).fallback(fallback).into(coverImageView);
        }
        titleView.setText(story.getTitle());
        numClapsView.setText(String.valueOf(story.getClaps()));
        switch (story.getCurrentStatus()){

            case PENDING:
            case IN_PROGRESS:
                statusImage.setImageDrawable(joinDraw);
                break;
            case COMPLETED:
                statusImage.setImageDrawable(readDraw);
                break;
            default:
               statusImage.setImageDrawable(joinDraw);
        }
    }

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

}
