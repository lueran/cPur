package com.cpur.viewholder;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cpur.R;
import com.cpur.models.Story;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StoryViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    RelativeLayout story_rl;
    StorageReference storageReference;
    GlideRequests with;
    ImageView coverImageView;


    public StoryViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.story_title);
        authorView = itemView.findViewById(R.id.story_author);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.post_num_stars);
        bodyView = itemView.findViewById(R.id.story_body);
        coverImageView = itemView.findViewById(R.id.textViewBackground);
        with = GlideApp.with(itemView);
    }

    public void bindToPost(Story story, View.OnClickListener starClickListener) {
        storageReference = FirebaseStorage.getInstance().getReference();
        String coverImageId = story.getCoverImage();
        if (coverImageId != null){
            storageReference.child("images/" + coverImageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    with.load(uri).into(coverImageView);
                }
            });
        }
        titleView.setText(story.getTitle());
        authorView.setText(story.getAuthor());
        numStarsView.setText(String.valueOf(story.getClaps()));
        bodyView.setText(story.getContent().get(0).getSuffixBody());
        starView.setOnClickListener(starClickListener);
    }

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)

}
