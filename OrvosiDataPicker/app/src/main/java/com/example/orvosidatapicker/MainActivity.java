package com.example.orvosidatapicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(MainActivity.class.getPackage()).toString();
    private static final int SECRET_KEY = 96;

    EditText emailEditText;
    EditText passwordEditText;

    TextView email;
    TextView password;

    private SharedPreferences userPreferences;
    private FirebaseAuth mAuth;

    // resource functions

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            emailEditText = findViewById(R.id.editTextEmail);
            passwordEditText = findViewById(R.id.editTextPassword);

            userPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
            mAuth = FirebaseAuth.getInstance();


            email = findViewById(R.id.emailTextView);
            password = findViewById(R.id.passwordTextView);


            Log.i(LOG_TAG, "onCreate");

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

            SharedPreferences.Editor editor = userPreferences.edit();
            editor.remove("regEmail");
            editor.remove("regPassword");
            editor.apply();

            Log.i(LOG_TAG, "onStop");
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            SharedPreferences.Editor editor = userPreferences.edit();
            editor.remove("regEmail");
            editor.remove("regPassword");
            editor.apply();

            Log.i(LOG_TAG, "onDestroy");
        }

        @Override
        protected void onResume() {
            super.onResume();

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_to_up);
            email.startAnimation(animation);
            password.startAnimation(animation);


            Log.i(LOG_TAG, "onResume");
        }

        @Override
        protected void onPause() {
            super.onPause();

            SharedPreferences.Editor editor = userPreferences.edit();


            editor.putString("email", emailEditText.getText().toString());
            editor.putString("password", passwordEditText.getText().toString());
            editor.apply();

            Log.i(LOG_TAG, "onPause");
        }

        @Override
        protected void onRestart() {
            super.onRestart();

            String username = userPreferences.getString("regEmail", "");
            String password = userPreferences.getString("regPassword", "");

            emailEditText.setText(username);
            passwordEditText.setText(password);

            Log.i(LOG_TAG, "onRestart");
        }

    // onClick event listeners

        public void login(View view) {

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User logged in sucessfully!");
                    startDatePicking();
                } else {
                    Log.d(LOG_TAG, "User wasn't logged in sucessfully!");
                    Toast.makeText(MainActivity.this, "User wasn't logged in sucessfully: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }

        private void startDatePicking() {
            Intent intent = new Intent(this, DatePickerActivity.class);
            startActivity(intent);
        }

        public void register(View view) {
                Intent regIntent = new Intent(this, RegisterActivity.class);
                regIntent.putExtra("SECRET_KEY", SECRET_KEY);
                startActivity(regIntent);
        }
}