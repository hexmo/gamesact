package com.example.myapplication.userManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputLayout email;

    FirebaseAuth mAuth;

    Context context;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //context
        context = this;

        //hooks
        email = findViewById(R.id.reset_email);

        //loading dialog
        loadingDialog = new LoadingDialog(this);

        mAuth = FirebaseAuth.getInstance();

    }

    public void resetPassword(View view) {
        if (Validator.validateEmail(email)) {
            loadingDialog.startLoading();
            mAuth.sendPasswordResetEmail(email.getEditText().getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("RESET", "Successfully sent reset email.");

                    loadingDialog.dismissLoading();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Reset password").setMessage("Password reset email has been sent to your inbox. Please check your email.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(context, LogInActivity.class));
                                    finish();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();

                    //Setting the title manually
                    alert.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("RESET", "Failed to send reset email." + "Error: " + e.toString());

                    loadingDialog.dismissLoading();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Reset password").setMessage("No account with that email address was found.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();

                    //Setting the title manually
                    alert.show();
                }
            });
        }
    }
}