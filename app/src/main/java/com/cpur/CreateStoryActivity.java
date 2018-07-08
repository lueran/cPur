package com.cpur;


import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.cpur.models.Paragraph;
import com.cpur.models.Story;
import com.cpur.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateStoryActivity extends BaseActivity {
    private static final String TAG = "CreateStoryActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText titleEditText;
    private EditText maxPartEditText;
    private EditText maxRoundsEditText;
    private EditText contentEditText;
    private ImageView coverImageView;
    private FloatingActionButton subbmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        titleEditText = findViewById(R.id.story_title);
        contentEditText = findViewById(R.id.content);
        maxPartEditText = findViewById(R.id.maxPart);
        maxRoundsEditText = findViewById(R.id.maxRound);
        coverImageView = findViewById(R.id.coverImage);
        subbmitButton = findViewById(R.id.fab);
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        subbmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStory();
            }
        });
    }

    private void submitStory() {
        final String title = titleEditText.getText().toString();
        final String content = contentEditText.getText().toString();
        final int numRoundsValue = Integer.valueOf(maxRoundsEditText.getText().toString());
        final int numParticipantsValue = Integer.valueOf(maxPartEditText.getText().toString());

        // Title is required
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(REQUIRED);
            return;
        }

        if (numRoundsValue < 3){
            maxRoundsEditText.setError("Minimum 3 Participants");
        }

        if (numRoundsValue < 5){
            maxRoundsEditText.setError("Minimum 5 Rounds");
        }

        // Body is required
        if (TextUtils.isEmpty(content) || content.length() < 10) {
            contentEditText.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(CreateStoryActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewStory(userId,title, content, "", numParticipantsValue, numRoundsValue);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        titleEditText.setEnabled(enabled);
        contentEditText.setEnabled(enabled);
        if (enabled) {
            subbmitButton.setVisibility(View.VISIBLE);
        } else {
            subbmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewStory(String userId,String title,String body, String coverImageSrc, int numParticipantsValue, int numRoundsValue) {
        // Create new post at /user-stories/$userid/$storyid and at
        // /posts/$postid simultaneously

        String key = mDatabase.child("stories").push().getKey();

        List<String> participants = new ArrayList<>();
        participants.add(userId);

        List<Paragraph> paragraphList = new ArrayList<>();
        paragraphList.add(new Paragraph(userId, body));

        Story story = new Story(userId, title,paragraphList,coverImageSrc, numParticipantsValue, numRoundsValue,participants);


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + key, story);
        childUpdates.put("/user-stories/" + userId + "/" + key, story);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}

