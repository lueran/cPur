package com.cpur;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cpur.models.Paragraph;
import com.cpur.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryActivity extends AppCompatActivity {

    public static final String EXTRA_STORY_ID_KEY = "com.cpur.EXTRA_STORY_ID_KEY";
    private TextView previousContentTextView;
    private EditText nextContentEditText;
    private Button actionButton;
    private FloatingActionButton clapsButton;
    private StoryViewModel storyViewModel;
    private String uid;
    private DatabaseReference storyReference;
    private DatabaseReference notificationRefrence;
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

        previousContentTextView = findViewById(R.id.previous_content);
        nextContentEditText = findViewById(R.id.next_content);
        nextLayout = findViewById(R.id.next_layout);
        actionButton = findViewById(R.id.action_button);
        clapsButton = findViewById(R.id.claps_button);
        uid = FirebaseAuth.getInstance().getUid();
        storyViewModel = ViewModelProviders.of(this).get(StoryViewModel.class);
        notificationRefrence = FirebaseDatabase.getInstance().getReference();
        // Get story key from intent
        String storyId = getIntent().getStringExtra(EXTRA_STORY_ID_KEY);
        if (storyId == null) {
            throw new IllegalArgumentException("Must pass EXTRA_STORY_ID_KEY");
        }

        // Initialize Database
        storyReference = FirebaseDatabase.getInstance().getReference()
                .child("stories").child(storyId);


    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove story value event listener
        if (storyReference != null) {
            storyReference.removeEventListener(storyListener);
        }
    }

    ValueEventListener storyListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Story object and use the values to update the UI
            Story story = dataSnapshot.getValue(Story.class);
            setData(story);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Story failed, log a message
//            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // [START_EXCLUDE]
//            Toast.makeText(StoryDetailActivity.this, "Failed to load story.",
//                    Toast.LENGTH_SHORT).show();
            // [END_EXCLUDE]
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        storyReference.addValueEventListener(storyListener);
    }

    private void setData(final Story story) {
        int size = story.getContent().size();
        StringBuilder sentence;
        if (size > 0) {
            Paragraph p = story.getContent().get(size - 1);
            sentence = new StringBuilder(p.getSuffixBody());

        } else {
            sentence = new StringBuilder("Be the first");
        }

        setTitle(story.getTitle());
        previousContentTextView.setText(String.format("%s %s", getString(R.string.previous_content), sentence.toString()));
        boolean isParticipants = story.getParticipants().contains(uid);
        boolean isMyTurn = story.getParticipants().get(story.getTurn() % story.getParticipants().size()).equals(uid);

        switch (story.getCurrentStatus()) {

            case PENDING: {
                if (!isParticipants) {
                    nextContentEditText.setVisibility(View.GONE);
                    nextLayout.setVisibility(View.GONE);
                    actionButton.setText(R.string.join);
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setBackgroundColor(Color.BLUE);
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            story.getParticipants().add(uid);
                            storyReference.setValue(story);
                        }
                    });
                } else {
                    actionButton.setVisibility(View.GONE);
                    nextContentEditText.setVisibility(View.VISIBLE);
                    nextLayout.setVisibility(View.VISIBLE);
                    int quantity = story.getMinParticipants() - story.getParticipants().size();
                    nextContentEditText.setText(getResources().getQuantityString(R.plurals.minimum_par_to_start,
                            quantity, quantity));
                    nextContentEditText.setEnabled(false);
                }
                clapsButton.setVisibility(View.GONE);
            }
            break;
            case IN_PROGRESS: {
                if (isMyTurn) {
                    actionButton.setText(R.string.send);
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setEnabled(false);
                    actionButton.setBackgroundColor(Color.YELLOW);
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            story.getContent().add(new Paragraph(uid, nextContentEditText.getText().toString()));
                            story.setTurn(story.getTurn() + 1);
                            storyReference.setValue(story);
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
                } else {
                    actionButton.setText(getString(R.string.buzz_format, "User X"));
                    actionButton.setEnabled(true);
                    nextContentEditText.setEnabled(false);
                    nextContentEditText.setText("Waiting for next user to response");
                    actionButton.setBackgroundColor(Color.RED);
                    actionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buzzNextUser(uid, story);
                            Toast.makeText(getBaseContext(), "Buzzed..", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                clapsButton.setVisibility(View.GONE);
            }
            break;
            case COMPLETED: {
                SpannableStringBuilder fullStory = new SpannableStringBuilder();

                for (int paragraphAt = 0; paragraphAt < story.getContent().size(); paragraphAt++) {
                    Paragraph p = story.getContent().get(paragraphAt);
                    fullStory.append(" ").append(p.getBody(), new ForegroundColorSpan(ContextCompat.getColor(getBaseContext(),
                            colors[paragraphAt % story.getParticipants().size()])), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                actionButton.setVisibility(View.GONE);
                previousContentTextView.setText(fullStory);
                nextContentEditText.setVisibility(View.GONE);
                nextLayout.setVisibility(View.GONE);
                clapsButton.setVisibility(View.VISIBLE);
                clapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        story.setClaps(story.getClaps() + 1);
                        storyReference.setValue(story);
                    }
                });
            }
            break;
        }
    }

    private void buzzNextUser(String userId, Story story) {

        String key = notificationRefrence.child("buzzes").push().getKey();
        String nextTurnUID = story.getNextTurnUID();
        Map<String, String> data = new HashMap<>();
        data.put("fromUID", userId);
        data.put("toUID", nextTurnUID);
        data.put("storyTitle", story.getTitle());
        data.put("storyUID", story.getUid());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/buzzes/" + key, data);
        childUpdates.put("/user-buzzes/" + userId + "/" + key, data);

        notificationRefrence.updateChildren(childUpdates);

    }
}
