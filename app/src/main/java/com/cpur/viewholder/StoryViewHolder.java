package com.cpur.viewholder;

import android.content.res.Resources;
import android.graphics.Color;
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
    private TextView titleView;
    private ImageView statusImage;
    private ImageView clapsImage;
    private TextView numClapsView;
    private GlideRequests with;
    private ImageView coverImageView;
    private Resources resources;
    private TextView numOfPartOutOf;


    public StoryViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.story_title);
        numClapsView = itemView.findViewById(R.id.post_num_claps);
        clapsImage = itemView.findViewById(R.id.clapsImage);
        coverImageView = itemView.findViewById(R.id.textViewBackground);
        statusImage = itemView.findViewById(R.id.statusImage);
        numOfPartOutOf = itemView.findViewById(R.id.numOfPathOutOf);
        with = GlideApp.with(itemView);
        resources = itemView.getResources();
    }

    public void bindToStory(Story story) {
        String coverImageId = story.getCoverImage();
        if (coverImageId == null) {
            coverImageId = DEFAULT_IMG_URI;
        }
        Uri uri = Uri.parse(coverImageId);
        if (uri != null) {
            with.load(coverImageId).placeholder(R.drawable.logo).error(R.drawable.logo).fallback(R.drawable.logo).into(coverImageView);
        }
        titleView.setText(story.getTitle());


        int maxParticipants = story.getMaxParticipants();
        String maxParticipantsString = String.valueOf(maxParticipants);
        int minParticipants = story.getMinParticipants();
        int size = story.getParticipants().size();
        int currentRound = story.getTurn() / size;
        int numRounds = story.getNumRounds();
        String numRoundsString = String.valueOf(numRounds);
        String sizeString = String.valueOf(size);
        switch (story.getCurrentStatus()) {

            case PENDING:
            case IN_PROGRESS:
                int color;
                if (size < minParticipants) {
                    color = Color.parseColor("#edb138"); //Yellow
                } else if (size >= maxParticipants) {
                    color = Color.parseColor("#ff0011"); //Red

                } else {
                    color = Color.parseColor("#469c11"); //Green
                }

                statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_join));
                statusImage.setColorFilter(color);
                numOfPartOutOf.setText(String.format("%s/%s", size, maxParticipants));
                numClapsView.setText(String.format("%s/%s", currentRound, numRoundsString));
                clapsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_sync_black_24dp));
                break;
            case COMPLETED:
                statusImage.setColorFilter(0);
                statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_glasses));
                clapsImage.setImageDrawable(resources.getDrawable(R.drawable.ic_claps));
                numClapsView.setText(String.valueOf(story.getClaps()));
                break;
            default:
                statusImage.setColorFilter(0);
                statusImage.setImageDrawable(resources.getDrawable(R.drawable.ic_join));

        }
    }
}
