package com.cpur;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


import com.cpur.models.Paragraph;
import com.cpur.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar sbNumParticipants = findViewById(R.id.numParticipants);
        SeekBar sbNumRounds = findViewById(R.id.numRounds);
        final TextView tvNumParticipantsValue = findViewById(R.id.numParticipantsValue);
        final TextView tvNumRoundsValue = findViewById(R.id.numRoundsValue);
        final EditText name = findViewById(R.id.name);
        final EditText s1 = findViewById(R.id.sentence1);
        final EditText s2 = findViewById(R.id.sentence2);
        final Button createButton = findViewById(R.id.create);


        sbNumRounds.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumRoundsValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvNumRoundsValue.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvNumRoundsValue.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        sbNumParticipants.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumParticipantsValue.setText(String.valueOf((progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvNumParticipantsValue.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvNumParticipantsValue.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStory(s1, s2, name);
            }
        });
    }

    private void createStory(EditText s1, EditText s2, EditText name) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        Paragraph paragraph = new Paragraph();
        paragraph.setS1(s1.getText().toString());
        paragraph.setS2(s2.getText().toString());
        paragraph.setAuthorId(uid);
        paragraphs.add(paragraph);
        Story story = new Story();
        story.setOwnerId(uid);
        story.setContent(paragraphs);
        story.setTitle(name.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("story");
        myRef.child(uid).setValue(story);
        startActivity(new Intent(MainActivity.this, MyStoriesActivity.class));
    }
}
