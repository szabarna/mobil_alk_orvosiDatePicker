package com.example.orvosidatapicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(RegisterActivity.class.getPackage()).toString();


    EditText regUserNameEditText;
    EditText regPasswordEditText;
    EditText regEmailEditText;
    EditText regPasswordConfirmEditText;

    TextView regUserNameTextView;
    TextView regPasswordTextView;
    TextView regEmailTextView;
    TextView regPasswordConfirmTextView;

    private SharedPreferences userPreferences;
    private FirebaseAuth mAuth;

    // resource functions

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            Bundle bundle = getIntent().getExtras();
            int secret_key = bundle.getInt("SECRET_KEY");

            Log.i(LOG_TAG, String.valueOf(secret_key));
            if(secret_key != 96) finish();

            regUserNameEditText = findViewById(R.id.regEditTextUsername);
            regPasswordEditText = findViewById(R.id.regEditTextPassword);
            regEmailEditText = findViewById(R.id.regEditTextEmail);
            regPasswordConfirmEditText = findViewById(R.id.regEditTextPasswordConfirm);

            regUserNameTextView = findViewById(R.id.regUserNameTextView);
            regPasswordTextView = findViewById(R.id.regPasswordTextView);
            regEmailTextView = findViewById(R.id.regEmailTextView);
            regPasswordConfirmTextView = findViewById(R.id.regPasswordConfirmTextView);

            userPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);


            String email = userPreferences.getString("email", "");
            String password = userPreferences.getString("password", "");

            regEmailEditText.setText(email);
            regPasswordEditText.setText(password);
            regPasswordConfirmEditText.setText(password);

            mAuth = FirebaseAuth.getInstance();

            ActionBar ab = getSupportActionBar();
            if (ab != null)   ab.hide();
        }

        @Override
        protected void onStart() {
            super.onStart();


            Log.i(LOG_TAG, "onStart");
        }

        @Override
        protected void onStop() {
            super.onStop();


            Log.i(LOG_TAG, "onStop");
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();



            Log.i(LOG_TAG, "onDestroy");
        }

        @Override
        protected void onResume() {
            super.onResume();

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_to_up);
            regUserNameTextView.startAnimation(animation);
            regPasswordTextView.startAnimation(animation);
            regEmailTextView.startAnimation(animation);
            regPasswordConfirmTextView.startAnimation(animation);

            Log.i(LOG_TAG, "onResume");
        }

        @Override
        protected void onPause() {
            super.onPause();


            Log.i(LOG_TAG, "onPause");
        }

        @Override
        protected void onRestart() {
            super.onRestart();

            Log.i(LOG_TAG, "onRestart");
        }

    // onClick event listeners

    public void register(View view) {
        String userName = regUserNameEditText.getText().toString();
        String password = regPasswordEditText.getText().toString();
        String email = regEmailEditText.getText().toString();
        String passwordConfirm = regPasswordConfirmEditText.getText().toString();

        if(!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "The 2 passwords are not the same!");
            return;
        }
        else if(userName.isEmpty()) {
            Log.e(LOG_TAG, "Username is required!");
            return;
        }
        else if(email.isEmpty()) {
            Log.e(LOG_TAG, "E-mail is required!");
            return;
        }

        Log.i(LOG_TAG, "Registered with data: " +
                "username: " + userName +
                " password: " + password +
                " email: " + email +
                " passConfirm: " + passwordConfirm);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Log.i(LOG_TAG, "User created sucessfully!");

                SharedPreferences.Editor editor = userPreferences.edit();
                editor.putString("regEmail", regEmailEditText.getText().toString());
                editor.putString("regPassword", regPasswordEditText.getText().toString());
                editor.apply();

                finish();
            } else {
                Log.d(LOG_TAG, "User created unsucessfully!");
                Toast.makeText(RegisterActivity.this, "User wasn't created sucessfully: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }

        });


     //   finish();

    }

    public void cancel(View view) {
        finish();
    }
}