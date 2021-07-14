package com.example.myapplication.userManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utils.LoadingDialog;
import com.example.myapplication.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class EditPasswordActivity extends AppCompatActivity {

    //Variables and Objects declaration
    TextInputLayout currentPassword, newPassword, reNewPassword;

    //Loading class
    private LoadingDialog loadingDialog;

    //For firebase
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        //LoadingDialog
        loadingDialog = new LoadingDialog(this);

        //Hooks
        currentPassword = findViewById(R.id.change_pw_current);
        newPassword = findViewById(R.id.change_pw_new);
        reNewPassword = findViewById(R.id.change_pw_re_new);
    }

    public void changePassword(View view) {

        if (Validator.validateDataNotNull(currentPassword)
                && Validator.validatePassword(newPassword)
                && Validator.validateReEnterPassword(newPassword, reNewPassword)) {

            //start loading dialog
            loadingDialog.startLoading();

            // Julfikar | Jan 13 '17
            // https://stackoverflow.com/questions/39866086/change-password-with-firebase-for-android
            user = FirebaseAuth.getInstance().getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,
                    currentPassword.getEditText().getText().toString());

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    // Empty form
                                    currentPassword.getEditText().setText("");
                                    newPassword.getEditText().setText("");
                                    reNewPassword.getEditText().setText("");


                                    Snackbar.make(findViewById(R.id.change_pw_button), "Successfully changed password.", Snackbar.LENGTH_LONG)
                                            .show();

                                } else {
                                    Snackbar.make(findViewById(R.id.change_pw_button), "Password change failed.", Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    } else {
                        currentPassword.setError("Wrong current password.");
                        Snackbar.make(findViewById(R.id.change_pw_button), "Unable to change password.", Snackbar.LENGTH_LONG)
                                .show();
                    }

                    //stop loading dialog
                    loadingDialog.dismissLoading();
                }
            });


        }
    }

    public void exitActivity(View view) {
        finish();
    }
}