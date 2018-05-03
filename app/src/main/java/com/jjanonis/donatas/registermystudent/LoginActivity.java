package com.jjanonis.donatas.registermystudent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjanonis.donatas.registermystudent.models.User;

import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            // already signed in
            updateUI();
        } else {
            // not signed in
            startFirebaseUiAuthentication();
        }
    }

    private void startFirebaseUiAuthentication() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
//                startActivity(MainActivity.createIntent(this, response));
                updateUI();
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

//                showSnackbar(R.string.unknown_error);
//                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }


    private void updateUI() {
        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
//        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
        Intent loginIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
        LoginActivity.this.startActivity(loginIntent);
    }
}

