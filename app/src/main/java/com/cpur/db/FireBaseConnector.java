package com.cpur.db;

import com.cpur.models.Story;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseConnector {

    public enum DB {
        STORIES("STORIES"),
        USERS("USERS");

        private final String text;

        /**
         * @param text
         */
        DB(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }

    private static FirebaseDatabase mDatabase =  FirebaseDatabase.getInstance();

    private static FireBaseConnector INSTANCE;

    private FireBaseConnector() {

    }

    public static FireBaseConnector getInstance(){
        if (INSTANCE == null){
            INSTANCE = new FireBaseConnector();
        }
        return INSTANCE;
    }

    public Task<Void> updateField(DB db, String uid, String field, Object value){
        return mDatabase.getReference(db.text).child(uid).child(field).setValue(value);
    }

    public Task<Void> create(DB db, Object object){
        return mDatabase.getReference(db.text).setValue(object);
    }

    public Task<Void> createStory(Story story){
        // Write a message to the database
        return mDatabase.getReference(DB.STORIES.text).setValue(story);
    }



}
