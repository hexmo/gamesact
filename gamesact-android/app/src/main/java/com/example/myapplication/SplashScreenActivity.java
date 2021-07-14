package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.userManagement.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Since this application requires internet access, we check the internet access.
        // If internet is available then we go on next steps or else we close the internet.
        Boolean isInternetAvailable = checkInternet(getApplicationContext());

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Intents
        Intent openHomePageActivity = new Intent(getApplicationContext(), MainActivity.class);
        Intent openLogInActivity = new Intent(getApplicationContext(), LogInActivity.class);


        if (isInternetAvailable) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentUser != null) {

                        // Subscribe to UID channel to receive notification.
                        FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Successfully subscribed to cloud message.";
                                        if (!task.isSuccessful()) {
                                            msg = "Successfully subscribed to cloud message.";
                                        }
                                        Log.d("FCM", msg);
                                        // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });

                        startActivity(openHomePageActivity);
                    } else {
                        startActivity(openLogInActivity);
                    }
                    finish();
                }
            }, 1500);
        }


    }

    private boolean checkInternet(Context context) {
        // https://developer.android.com/training/monitoring-device-state/connectivity-status-type

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            View contextView = findViewById(R.id.splash_screen_title);

            // Toast.makeText(context, "No internet access! App will exit.", Toast.LENGTH_SHORT).show();
            // Android Snack bar Example Tutorial
            // https://www.journaldev.com/10324/android-snackbar-example-tutorial
            Snackbar.make(contextView, "No internet access! App will exit.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .show();



            return false;

        }

        return true;
    }


}