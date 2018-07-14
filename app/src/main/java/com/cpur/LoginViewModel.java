package com.cpur;

import android.arch.lifecycle.ViewModel;

import com.cpur.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginViewModel extends ViewModel {


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public void start(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }
    public void writeNewUser(String userId, String name, String email) {
        String notificationToken = FirebaseInstanceId.getInstance().getToken();
        User user = new User(name, email, notificationToken);
        mDatabase.child("users").child(userId).setValue(user);
        user.setUid(userId);


    }

    public boolean checkAuth() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String username = usernameFromEmail(Objects.requireNonNull(currentUser.getEmail()));
            // Write new user
            writeNewUser(currentUser.getUid(), username, currentUser.getEmail());
            return true;
        }
        return false;
    }

    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void signUp(String email, String password, OnCompleteListener<AuthResult> listener , OnFailureListener failureListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener)
                .addOnFailureListener(failureListener);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
