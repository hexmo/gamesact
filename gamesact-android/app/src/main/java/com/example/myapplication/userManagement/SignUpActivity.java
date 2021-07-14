package com.example.myapplication.userManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {

    //Variables and Objects declaration
    TextInputLayout signUpFullName, signUpPhoneNumber, signUpEmail, signUpPassword, signUpReEnterPassword;

    //Loading class
    private LoadingDialog loadingDialog;

    //For firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //LoadingDialog
        loadingDialog = new LoadingDialog(this);

        //Hooks
        signUpFullName = findViewById(R.id.signup_name);
        signUpPhoneNumber = findViewById(R.id.signup_phone);
        signUpEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpReEnterPassword = findViewById(R.id.signup_re_password);

        //Initialize Firebase.
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

    }

    public void signUp(View view) {
        if (Validator.validateName(signUpFullName)
                && Validator.validatePhoneNumber(signUpPhoneNumber)
                && Validator.validateEmail(signUpEmail)
                && Validator.validatePassword(signUpPassword)
                && Validator.validateReEnterPassword(signUpPassword, signUpReEnterPassword)) {

            //start signup process
            startSignUp();
        }
    }

    private void startSignUp() {
        String userEmail = signUpEmail.getEditText().getText().toString().trim();
        String userPassword = signUpPassword.getEditText().getText().toString();

        //start loading dialog
        loadingDialog.startLoading();

        //Creating user account in firebase database as adding more user data
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    //Get userName and phoneNumber
                    String _id = mAuth.getCurrentUser().getUid();
                    String _userFullName = signUpFullName.getEditText().getText().toString().trim();
                    String _userPhoneNumber = signUpPhoneNumber.getEditText().getText().toString().trim();

                    //Creating userModel object to insert into the database
                    UserModel userModel = new UserModel(_id, _userFullName, _userPhoneNumber);


                    database.collection("user").document(mAuth.getCurrentUser().getUid()).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Stopping loading icon
                            loadingDialog.dismissLoading();

                            mAuth.signOut();

                            Log.d("User data:", "DocumentSnapshot successfully written!");
                            Toast.makeText(SignUpActivity.this, "Successfully created account.\nPlease Sign In To Continue.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("User data:", "Error writing document", e);
                            Toast.makeText(SignUpActivity.this, "Something went wrong." + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                //Stopping loading icon
                loadingDialog.dismissLoading();
            }
        });


    }

    public void gotoLogInActivity(View view) {
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
        finish();
    }
}