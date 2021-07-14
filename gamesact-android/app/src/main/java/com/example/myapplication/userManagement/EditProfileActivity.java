package com.example.myapplication.userManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class EditProfileActivity extends AppCompatActivity {
    // Variables and Objects declaration
    TextInputLayout name, phone, email;

    UserModel userModel;
    FirebaseUser firebaseUser;
    LoadingDialog loadingDialog;
    DocumentReference databaseReference;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //databaseReference = FirebaseFirestore.getInstance().collection("user").document(firebaseUser.getUid());
        db = FirebaseFirestore.getInstance();

        loadingDialog = new LoadingDialog(this);

        // Hooks
        name = findViewById(R.id.edit_profile_name);
        phone = findViewById(R.id.edit_profile_phone);
        email = findViewById(R.id.edit_profile_email);

        assignDefaultValues();
    }

    private void assignDefaultValues() {
        // Get userModel from shared Preferences
        SharedPreferences mPrefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("UserDetailsObject", null);
        userModel = gson.fromJson(json, UserModel.class);

        if (userModel != null) {
            name.getEditText().setText(userModel.getUserName());
            phone.getEditText().setText(userModel.getPhoneNumber());
        }

        email.getEditText().setText(firebaseUser.getEmail());
    }

    public void exitActivity(View view) {
        finish();
    }

    public void saveChanges(View view) {


        String nameText = name.getEditText().getText().toString().trim();
        String phoneText = phone.getEditText().getText().toString().trim();
        String emailText = email.getEditText().getText().toString().trim();

        if (!nameText.equals(userModel.getUserName()) || !phoneText.equals(userModel.getPhoneNumber())) {
            if (Validator.validateName(name) && Validator.validatePhoneNumber(phone)) {
                userModel.setPhoneNumber(phoneText);
                userModel.setUserName(nameText);
                upDateNamePhone(nameText, phoneText);
            }
        }

        if (!emailText.equals(firebaseUser.getEmail())) {
            if (Validator.validateEmail(email)) {
                firebaseUser.updateEmail(emailText).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //oldEmail = firebaseUser.getEmail();
                        Log.d("UPDATE_EMAIL", "Successfully updated email.");
                        Toast.makeText(EditProfileActivity.this, "Successfully updated email.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Sign out and login to change email.", Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_EMAIL", "Email update failed: " + e.toString());
                    }
                });
            }

        }


    }

    private void upDateNamePhone(String nameText, String phoneText) {


        loadingDialog.startLoading();

        db.collection("user").document(firebaseUser.getUid()).update(
                "userName", userModel.getUserName(),
                "phoneNumber", userModel.getPhoneNumber()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                loadingDialog.dismissLoading();


                Toast.makeText(EditProfileActivity.this, "Profile update successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPDATE", "Profile update successful.");


                // Save updated user details to shared preference.
                SharedPreferences mPrefs = getSharedPreferences("UserData", MODE_PRIVATE);

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(userModel);
                prefsEditor.putString("UserDetailsObject", json);
                prefsEditor.apply();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                loadingDialog.dismissLoading();

                Toast.makeText(EditProfileActivity.this, "Update failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("UPDATE", "Update name failed: " + e.toString());

            }
        });


    }


}