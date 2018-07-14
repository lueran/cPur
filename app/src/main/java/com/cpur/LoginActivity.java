package com.cpur;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cpur.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private Button mSignUpButton;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelFactory.getInstance().create(LoginViewModel.class);

        // Views
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mSignInButton = findViewById(R.id.login);
        mSignUpButton = findViewById(R.id.register);

        loginViewModel.start();

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if(loginViewModel.checkAuth()){
            onAuthSuccess();
        }

    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                hideProgressDialog();

                if (task.isSuccessful()) {
                    onAuthSuccess();
                } else {
                    hideProgressDialog();
                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                    Toast.makeText(LoginActivity.this, "Sign In Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        loginViewModel.signIn(email, password, listener);

    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                hideProgressDialog();

                if (task.isSuccessful()) {
                    onAuthSuccess();
                } else {
                    Toast.makeText(LoginActivity.this, "Sign Up Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        OnFailureListener failureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                hideProgressDialog();
                Toast.makeText(getBaseContext(), "Failed Creating User", Toast.LENGTH_LONG).show();
            }
        };

        loginViewModel.signUp(email, password, listener, failureListener);
    }

    private void onAuthSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        }

        return result;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login) {
            signIn();
        } else if (i == R.id.register) {
            signUp();
        }
    }
}
