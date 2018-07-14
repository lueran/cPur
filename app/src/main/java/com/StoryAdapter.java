package com;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cpur.R;
import com.cpur.data.StoryAllParagraph;
import com.cpur.viewholder.StoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder> {

        private List<StoryAllParagraph> stories = new ArrayList<>();
        private OnStoryClickListener listener;
        private int currentPosition;

    @NonNull
        @Override
        public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new StoryViewHolder(inflater.inflate(R.layout.item_story, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull StoryViewHolder holder, final int position) {
            final StoryAllParagraph story = stories.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        currentPosition = holder.getAdapterPosition();
                        listener.onStoryClick(story);
                    }
                }
            });
            // Bind Story to ViewHolder
            holder.bindToStory(story);
        }

        @Override
        public int getItemCount() {
            return stories.size();
        }

        public List<StoryAllParagraph> getStories() {
            return stories;
        }

        public void setStories(List<StoryAllParagraph> stories) {
            this.stories = stories;
            notifyDataSetChanged();
        }

        public OnStoryClickListener getListener() {
            return listener;
        }

        public void setListener(OnStoryClickListener listener) {
            this.listener = listener;
        }

        public interface OnStoryClickListener {
            void onStoryClick(StoryAllParagraph story);
        }

        public int getCurrentPosition(){
            return currentPosition;
        }
}

