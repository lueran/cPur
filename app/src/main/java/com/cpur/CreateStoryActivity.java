package com.cpur;


import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.NumberPicker;
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

    private EditText mTitleField;
    private EditText mBodyField;
    NumberPicker numParticipants = findViewById(R.id.numParticipants);
    NumberPicker numRounds = findViewById(R.id.numRounds);
    private FloatingActionButton mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        numParticipants.setMinValue(2);
        numParticipants.setMinValue(20);
        numRounds.setMinValue(5);
        numRounds.setMinValue(100);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mTitleField = findViewById(R.id.field_title);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStory();
            }
        });
    }

    private void submitStory() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final int numRoundsValue = numRounds.getValue();
        final int numParticipantsValue = numParticipants.getValue();
        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
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
                            writeNewStory(userId, user.getUsername(), title, body, numParticipantsValue, numRoundsValue);
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
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewStory(String userId, String username, String title, String body, int numParticipantsValue, int numRoundsValue) {
        // Create new post at /user-stories/$userid/$storyid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("stories").push().getKey();
        List<Paragraph> paragraphList = new ArrayList<>();
        paragraphList.add(new Paragraph(userId,body));
        Story story = new Story(userId,username,title,paragraphList,numParticipantsValue,numRoundsValue);
        Map<String, Object> postValues = story.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + key, postValues);
        childUpdates.put("/user-stories/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}

