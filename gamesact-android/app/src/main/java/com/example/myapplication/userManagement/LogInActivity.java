package com.example.myapplication.userManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity {
    //Variables and Objects declaration
    TextInputLayout logInEmail, logInPassword;

    //Loading class
    private LoadingDialog loadingDialog;

    //For firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    //Storing user data in shared preference after sign in is successful
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //LoadingDialog
        loadingDialog = new LoadingDialog(this);

        // Hooks
        logInEmail = findViewById(R.id.login_email);
        logInPassword = findViewById(R.id.login_password);


        //Initialize Firebase.
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public void gotToResetEmail(View view) {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    public void gotToSignUpActivity(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    public void login(View view) {
        //start loading dialog
        loadingDialog.startLoading();

        View contextView = findViewById(R.id.login_button);


        if (Validator.validateEmail(logInEmail) && Validator.validateDataNotNull(logInPassword)) {
            String userEmail = logInEmail.getEditText().getText().toString().trim();
            String userPassword = logInPassword.getEditText().getText().toString();

            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // Retrieve user data.
                        DocumentReference docRef = database.collection("user").document(mAuth.getCurrentUser().getUid());
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d("FireStore","Successfully retrieved user data.");
                                userModel = documentSnapshot.toObject(UserModel.class);

                                // save user model to shared preferences.
                                // Muhammad Aamir Ali | Aug 27 '13
                                // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
                                SharedPreferences mPrefs = getSharedPreferences("UserData",MODE_PRIVATE);

                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(userModel);
                                prefsEditor.putString("UserDetailsObject", json);
                                prefsEditor.commit();

                            }
                        });

                        //Toast.makeText(getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                        Snackbar.make(contextView, "Logged in Successfully!", Snackbar.LENGTH_LONG)
                                .show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                        //Stopping loading dialog
                        loadingDialog.dismissLoading();
                    } else {
                        Snackbar.make(contextView, "Error! " + task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                        //Toast.makeText(getApplicationContext(), "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //Stopping loading dialog
                    loadingDialog.dismissLoading();

                }
            });

        }

    }
}