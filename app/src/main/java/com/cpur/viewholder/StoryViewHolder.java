package com.cpur.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpur.R;
import com.cpur.data.Story;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    private static final String DEFAULT_IMG_URI = "https://firebasestorage.googleapis.com/v0/b/cpur-1ad2a.appspot.com/o/images%2Fpizza_monster.png?alt=media&token=ef5d90e7-639c-46ae-a7e3-89df0fa6b52b";
    private TextView titleView;
    private TextView storyStatus;
    private TextView authorView;
    private ImageView starView;
    private TextView numStarsView;
    private TextView bodyView;
    private GlideRequests with;
    private ImageView coverImageView;


    public StoryViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.story_title);
        authorView = itemView.findViewById(R.id.story_author);
        starView = itemView.findViewById(R.id.clapsImage);
        numStarsView = itemView.findViewById(R.id.post_num_claps);
        bodyView = itemView.findViewById(R.id.story_body);
        coverImageView = itemView.findViewById(R.id.textViewBackground);
        storyStatus = itemView.findViewById(R.id.story_status);
        with = GlideApp.with(itemView);
    }

    public void bindToPost(Story story, View.OnClickListener starClickListener) {
        String coverImageId = story.getCoverImage();
        if (coverImageId == null){
            coverImageId = DEFAULT_IMG_URI;
        }
        Uri uri = Uri.parse(coverImageId);
        if (uri != null){
            with.load(coverImageId).into(coverImageView);
        }
        storyStatus.setText(story.getCurrentStatus().toString());
        titleView.setText(story.getTitle());
        authorView.setText(story.getAuthor());
        numStarsView.setText(String.valueOf(story.getClaps()));
        bodyView.setText(story.getContent().get(0).getSuffixBody());
    }

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

}
