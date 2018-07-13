package com.cpur.viewholder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpur.R;
import com.cpur.data.Story;
import com.cpur.data.StoryAllParagraph;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    private static final String DEFAULT_IMG_URI = "https://firebasestorage.googleapis.com/v0/b/cpur-1ad2a.appspot.com/o/images%2Fpizza_monster.png?alt=media&token=ef5d90e7-639c-46ae-a7e3-89df0fa6b52b";
    private TextView titleView;
    private ImageView statusImage;
    private TextView numClapsView;
    private GlideRequests with;
    private ImageView coverImageView;
    private Resources resources;
    private Drawable fallback;
    private TextView numOfPartOutOf;




    public StoryViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.story_title);
        numClapsView = itemView.findViewById(R.id.post_num_claps);
        coverImageView = itemView.findViewById(R.id.textViewBackground);
        statusImage = itemView.findViewById(R.id.statusImage);
        numOfPartOutOf = itemView.findViewById(R.id.numOfPathOutOf);
        with = GlideApp.with(itemView);
        resources = itemView.getResources();
        fallback = itemView.getResources().getDrawable(R.drawable.logo);

    }

    public void bindToPost(StoryAllParagraph storyAllParagraph, View.OnClickListener starClickListener) {
        Story story = storyAllParagraph.getStory();
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
        int maxParticipants = story.getMaxParticipants();
        String maxParticipantsString = String.valueOf(maxParticipants);
        int size = story.getParticipants().size();
        String sizeString = String.valueOf(size);
        switch (story.getCurrentStatus()){

            case PENDING:
            case IN_PROGRESS:
                statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_join));
                numOfPartOutOf.setText(String.format("%s/%s", size, maxParticipants));
                break;
            case COMPLETED:
                statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_glasses));
                break;
            default:
               statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_join));
        }
    }

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

}
