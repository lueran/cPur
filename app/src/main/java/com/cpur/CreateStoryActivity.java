package com.cpur;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.util.UUID;

public class CreateStoryActivity extends BaseActivity {
    private static final String REQUIRED = "Required";
    private static final String DEFAULT_IMG_URI = "https://firebasestorage.googleapis.com/v0/b/cpur-1ad2a.appspot.com/o/images%2Fpizza_monster.png?alt=media&token=ef5d90e7-639c-46ae-a7e3-89df0fa6b52b";
    private static int GET_FROM_GALLERY = 3;

    private EditText titleEditText;
    private EditText maxPartEditText;
    private EditText maxRoundsEditText;
    private EditText contentEditText;
    private ImageView coverImageView;
    private FloatingActionButton submitButton;
    private Uri coverImageUri;
    private String imageId;
    private CreateStoryViewModel createStoryViewModel;
    private StorageReference storageReference;


    private ProgressDialog uploadProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        ViewModelFactory factory = ViewModelFactory.getInstance();
        createStoryViewModel = ViewModelProviders.of(this, factory).get(CreateStoryViewModel.class);
        titleEditText = findViewById(R.id.story_title);
        contentEditText = findViewById(R.id.content);
        maxPartEditText = findViewById(R.id.maxPart);
        maxRoundsEditText = findViewById(R.id.maxRound);
        coverImageView = findViewById(R.id.coverImage);
        submitButton = findViewById(R.id.fab);

        // [START initialize_database_ref]
        storageReference = FirebaseStorage.getInstance().getReference();
        createStoryViewModel.start();
        // [END initialize_database_ref]

        uploadProgressDialog = new ProgressDialog(this);
        uploadProgressDialog.setTitle("Uploading...");


        coverImageView.setOnClickListener(v -> chooseImage());
        submitButton.setOnClickListener(v -> submitStory());
    }

    private void chooseImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    private void submitStory() {
        final String title = titleEditText.getText().toString();
        final String content = contentEditText.getText().toString();
        String maxRoundsString = maxRoundsEditText.getText().toString();
        String maxPartString = maxPartEditText.getText().toString();
        int numRoundsValue;
        int numParticipantsValue;


        // Title is required
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError(REQUIRED);
            return;
        }
        // Validation
        if (TextUtils.isEmpty(maxRoundsString)) {
            maxRoundsEditText.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(maxPartString)) {
            maxPartEditText.setError(REQUIRED);
            return;
        }

        numRoundsValue = Integer.valueOf(maxRoundsString);
        numParticipantsValue = Integer.valueOf(maxPartString);

        if (numParticipantsValue < 3) {
            maxPartEditText.setError("Minimum 3 Participants");
            return;

        } else if (numParticipantsValue > 10) {
            maxPartEditText.setError("Max 10 Participants");
            return;
        }

        if (numRoundsValue < 5) {
            maxRoundsEditText.setError("Minimum 5 Rounds");
            return;
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

        createStoryViewModel.writeNewStory(title, imageId, content, numParticipantsValue, numRoundsValue);
        setEditingEnabled(true);
        finish();
    }

    private void uploadImage() {
        uploadProgressDialog.show();
        if (coverImageUri != null) {
            String imageUUID = UUID.randomUUID().toString();
            final StorageReference filepath = storageReference.child("images/" + imageUUID);
            filepath.putFile(coverImageUri).addOnProgressListener(
                    taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        uploadProgressDialog.setMessage("Uploaded " + progress + "%");
                    }).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageId = uri.toString();
                        uploadProgressDialog.dismiss();
                        Toast.makeText(CreateStoryActivity.this, "Uploading Finished!", Toast.LENGTH_LONG).show();
                        //Do what you want with the url
                    })).addOnFailureListener(e -> {
                        imageId = DEFAULT_IMG_URI;
                        uploadProgressDialog.dismiss();
                        Toast.makeText(CreateStoryActivity.this, "Uploading Failed!", Toast.LENGTH_LONG).show();
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            coverImageUri = data.getData();
            Glide.with(this).load(coverImageUri).into(coverImageView);
            setEditingEnabled(false);
            uploadImage();
            setEditingEnabled(true);
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


}

