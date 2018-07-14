package com.cpur;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cpur.data.Paragraph;
import com.cpur.data.Story;
import com.cpur.data.StoryAllParagraph;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class StoryActivity extends AppCompatActivity {

    public static final String EXTRA_STORY_ID_KEY = "com.cpur.EXTRA_STORY_ID_KEY";
    private TextView previousContentTextView;
    private TextView infoMessageTextView;
    private EditText nextContentEditText;
    private FloatingActionButton actionButton;
    private StoryViewModel storyViewModel;
    private DatabaseReference notificationReference;
    private TextView storyTitleTextView;
    private String storyId;
    private final int[] colors = {
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.color8,
            R.color.color9,
            R.color.color10,
            R.color.color11,
    };
    private View nextLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        infoMessageTextView = findViewById(R.id.info_message);
        previousContentTextView = findViewById(R.id.previous_content);
        nextContentEditText = findViewById(R.id.next_content);
        nextLayout = findViewById(R.id.next_layout);
        actionButton = findViewById(R.id.action_button);
        storyTitleTextView = findViewById(R.id.theStoryTitle);

        ViewModelFactory factory = ViewModelFactory.getInstance();
        storyViewModel = ViewModelProviders.of(this, factory).get(StoryViewModel.class);
        notificationReference = FirebaseDatabase.getInstance().getReference();
        // Get story key from intent
        storyId = getIntent().getStringExtra(EXTRA_STORY_ID_KEY);
        if (storyId == null) {
            throw new IllegalArgumentException("Must pass EXTRA_STORY_ID_KEY");
        }

        storyViewModel.start(storyId);

        storyViewModel.getStory().observe(this, new Observer<StoryAllParagraph>() {
            @Override
            public void onChanged(@Nullable StoryAllParagraph story) {
                if (story != null) {
                    setData(story);
                }
            }
        });
    }

    private void setData(final StoryAllParagraph storyAllParagraph) {
        int size = storyAllParagraph.getParagraphs().size();
        StringBuilder sentence;
        if (size > 0) {
            Paragraph p = storyAllParagraph.getParagraphs().get(size - 1);
            sentence = new StringBuilder(p.getSuffixBody());

        } else {
            sentence = new StringBuilder("Be the first");
        }
        Story story = storyAllParagraph.getStory();
        String storyTitle = story.getTitle();
        storyTitleTextView.setText(storyTitle);
        storyTitleTextView.setPaintFlags(storyTitleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String prevContent = String.format("%s %s", getString(R.string.previous_content), sentence.toString());
        boolean isParticipants = storyViewModel.isParticipants();
        boolean isMyTurn = storyViewModel.isMyTurn();
        boolean isMyContent = storyViewModel.isMyContent();
        boolean isFull = storyViewModel.isFull();

        switch (story.getCurrentStatus()) {

            case PENDING: {
                previousContentTextView.setText(prevContent);
                if (!isParticipants) {
                    showJoin();
                } else {
                    showWaitingForMore(story);
                }
            }
            break;
            case IN_PROGRESS: {
                if (isParticipants) {
                    if (isMyTurn) {
                        showMyTurn(prevContent);
                    } else {
                        if (isMyContent) {
                            previousContentTextView.setVisibility(View.VISIBLE);
                            previousContentTextView.setText(prevContent);
                        } else {
                            previousContentTextView.setVisibility(View.GONE);
                        }
                        showBuzzUser(story);
                    }
                } else if (!isFull){
                    showJoin();
                } else {
                    showIsFull();
                }
            }
            break;
            case COMPLETED: {
                showCompleteStory(storyAllParagraph);
            }
            break;
        }
    }

    private void showCompleteStory(StoryAllParagraph story) {
        SpannableStringBuilder fullStory = new SpannableStringBuilder();

        for (int paragraphAt = 0; paragraphAt < story.getParagraphs().size(); paragraphAt++) {
            Paragraph p = story.getParagraphs().get(paragraphAt);
            fullStory.append(" ").append(p.getBody(), new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(),
                    colors[paragraphAt % story.getStory().getParticipants().size()])), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        previousContentTextView.setVisibility(View.VISIBLE);
        previousContentTextView.setText(fullStory);
        nextContentEditText.setVisibility(View.GONE);
        infoMessageTextView.setVisibility(View.GONE);
        nextLayout.setVisibility(View.GONE);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.clap)));
        actionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.logo));
        actionButton.setRippleColor(getResources().getColor(R.color.clap_light));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyViewModel.clap(storyId);
                Snackbar.make(v, "You Clapped", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void showBuzzUser(final Story story) {
        nextLayout.setVisibility(View.GONE);
        infoMessageTextView.setVisibility(View.VISIBLE);
        infoMessageTextView.setText(R.string.story_buzz);
        actionButton.setEnabled(true);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buzz)));
        actionButton.setImageResource(R.drawable.ic_bee);
        actionButton.setRippleColor(getResources().getColor(R.color.buzz_light));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buzzNextUser(story);
                Snackbar.make(v, "You Buzzed...", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showMyTurn(String prevContent) {
        previousContentTextView.setVisibility(View.VISIBLE);
        previousContentTextView.setText(prevContent);
        infoMessageTextView.setVisibility(View.GONE);
        nextLayout.setVisibility(View.VISIBLE);
        nextContentEditText.setHint(R.string.continue_the_story_here);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setEnabled(false);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.send)));
        actionButton.setRippleColor(getResources().getColor(R.color.send_light));
        actionButton.setImageResource(R.drawable.ic_send_black_24dp);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyViewModel.sendContent(nextContentEditText.getText().toString(), storyId);
                Snackbar.make(v, "Published " + storyTitleTextView.getText().toString(), Snackbar.LENGTH_LONG).show();

            }
        });
        nextContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actionButton.setEnabled(s.length() >= 10);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showWaitingForMore(Story story) {
        actionButton.setVisibility(View.GONE);
        infoMessageTextView.setVisibility(View.VISIBLE);
        nextLayout.setVisibility(View.GONE);
        int quantity = story.getMinParticipants() - story.getParticipants().size();
        infoMessageTextView.setText(getResources().getQuantityString(R.plurals.minimum_par_to_start,
                quantity, quantity));
    }

    private void showJoin() {
        nextLayout.setVisibility(View.GONE);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.join)));
        actionButton.setImageResource(R.drawable.ic_join);
        actionButton.setRippleColor(getResources().getColor(R.color.join_light));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyViewModel.joinStory(storyId);
                Snackbar.make(v, "You Joined " + storyTitleTextView.getText().toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showIsFull() {
        nextLayout.setVisibility(View.GONE);
        actionButton.setVisibility(View.GONE);
        infoMessageTextView.setVisibility(View.VISIBLE);
        infoMessageTextView.setText(R.string.story_is_full);
    }

    private void buzzNextUser(Story story) {
        String uid = storyViewModel.getUID();
        String key = notificationReference.child("buzzes").push().getKey();
        String nextTurnUID = story.getNextTurnUID();
        Map<String, String> data = new HashMap<>();
        data.put("fromUID", uid);
        data.put("toUID", nextTurnUID);
        data.put("storyTitle", story.getTitle());
        data.put("storyUID", story.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/buzzes/" + key, data);
        childUpdates.put("/user-buzzes/" + uid + "/" + key, data);

        notificationReference.updateChildren(childUpdates);

    }
}
