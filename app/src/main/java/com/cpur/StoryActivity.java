package com.cpur;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
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

import java.util.List;

public class StoryActivity extends AppCompatActivity {

    public static final String EXTRA_STORY_ID_KEY = "com.cpur.EXTRA_STORY_ID_KEY";
    private TextView previousContentTextView;
    private EditText nextContentEditText;
    private Button actionButton;
    private StoryViewModel storyViewModel;
    private String uid;
    private DatabaseReference storyReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        previousContentTextView = findViewById(R.id.previous_content);
        nextContentEditText = findViewById(R.id.next_content);
        actionButton = findViewById(R.id.action_button);
        uid = FirebaseAuth.getInstance().getUid();
        storyViewModel = ViewModelProviders.of(this).get(StoryViewModel.class);

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

                    actionButton.setText(R.string.join);
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
                }
            }
            break;
            case IN_PROGRESS: {
                if (isMyTurn) {
                    actionButton.setText(R.string.send);
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
                            //TODO: SEND BUZZ
                            Toast.makeText(getBaseContext(), "Buzzed..", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            break;
            case COMPLETED: {
                SpannableStringBuilder fullStory = new SpannableStringBuilder();

                for (Paragraph p : story.getContent()) {
                    fullStory.append(p.getBody());
                }
            }
            break;
        }
    }
}
