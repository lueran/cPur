package com.cpur;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateStoryActivity extends BaseActivity {
    private static final String TAG = "CreateStoryActivity";
    private static final String REQUIRED = "Required";
    private static int GET_FROM_GALLERY = 3;
    // [START declare_database_ref]
    private DatabaseReference databaseReference;
    // [END declare_database_ref]

    private EditText titleEditText;
    private EditText maxPartEditText;
    private EditText maxRoundsEditText;
    private EditText contentEditText;
    private ImageView coverImageView;
    private FloatingActionButton submitButton;
    private StorageReference storageReference;
    private Uri coverImagUri;
    private String imageId;

    private ProgressDialog uploadProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);

        titleEditText = findViewById(R.id.story_title);
        contentEditText = findViewById(R.id.content);
        maxPartEditText = findViewById(R.id.maxPart);
        maxRoundsEditText = findViewById(R.id.maxRound);
        coverImageView = findViewById(R.id.coverImage);
        submitButton = findViewById(R.id.fab);

        // [START initialize_database_ref]
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        uploadProgressDialog = new ProgressDialog(this);
        uploadProgressDialog.setTitle("Uploading...");


        coverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStory();
            }
        });
    }

    private void chooseImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    private void submitStory() {
        final String title = titleEditText.getText().toString();
        final String content = contentEditText.getText().toString();
        final int numRoundsValue = Integer.valueOf(maxRoundsEditText.getText().toString());
        final int numParticipantsValue = Integer.valueOf(maxPartEditText.getText().toString());
        uploadImage();


        // Title is required
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(REQUIRED);
            return;
        }

        if (numParticipantsValue < 3) {
            maxPartEditText.setError("Minimum 3 Participants");
        } else if (numParticipantsValue > 10) {
            maxPartEditText.setError("Max 10 Participants");
        }

        if (numRoundsValue < 5) {
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
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
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
                            writeNewStory(userId, title, content, numParticipantsValue, numRoundsValue);
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

    private void uploadImage() {

        uploadProgressDialog.show();
        if (coverImagUri != null) {
            imageId = UUID.randomUUID().toString();
            StorageReference filepath = storageReference.child("images/" + imageId);
            filepath.putFile(coverImagUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    uploadProgressDialog.dismiss();
                    Toast.makeText(CreateStoryActivity.this, "Uploading Finished!", Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            uploadProgressDialog.dismiss();
                            Toast.makeText(CreateStoryActivity.this, "Uploading Failed!", Toast.LENGTH_LONG).show();
                        }
                    })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    uploadProgressDialog.setMessage("Uploaded " + progress + "%" );
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            coverImagUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImagUri);
                coverImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEditingEnabled(boolean enabled) {
        titleEditText.setEnabled(enabled);
        contentEditText.setEnabled(enabled);
        if (enabled) {
            submitButton.setVisibility(View.VISIBLE);
        } else {
            submitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewStory(String userId, String title, String body, int numParticipantsValue, int numRoundsValue) {
        // Create new post at /user-stories/$userid/$storyid and at
        // /posts/$postid simultaneously

        String key = databaseReference.child("stories").push().getKey();

        List<String> participants = new ArrayList<>();
        participants.add(userId);

        List<Paragraph> paragraphList = new ArrayList<>();
        paragraphList.add(new Paragraph(userId, body));

        Story story = new Story(userId, title, paragraphList, imageId, numParticipantsValue, numRoundsValue, participants);


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + key, story);
        childUpdates.put("/user-stories/" + userId + "/" + key, story);

        databaseReference.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}

