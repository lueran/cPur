package com.cpur.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpur.R;
import com.cpur.models.Story;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;

    public StoryViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.story_title);
        authorView = itemView.findViewById(R.id.story_author);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.post_num_stars);
        bodyView = itemView.findViewById(R.id.story_body);
    }

    public void bindToPost(Story story, View.OnClickListener starClickListener) {
        titleView.setText(story.getTitle());
        authorView.setText(story.getAuthor());
        numStarsView.setText(String.valueOf(story.getClaps()));
        bodyView.setText(story.getContent().get(0).getSuffixBody());
        starView.setOnClickListener(starClickListener);
    }
}
