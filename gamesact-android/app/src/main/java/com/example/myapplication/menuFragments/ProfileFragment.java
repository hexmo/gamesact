package com.example.myapplication.menuFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.UserModel;
import com.example.myapplication.userManagement.EditPasswordActivity;
import com.example.myapplication.userManagement.EditProfileActivity;
import com.example.myapplication.userManagement.LogInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {

    TextView name, email, phone;
    Button editProfileButton, logOutButton, changePasswordButton;

    UserModel userModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Hooks
        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);

        setNameEmail();

        editProfileButton = view.findViewById(R.id.profile_edit_profile);
        logOutButton = view.findViewById(R.id.profile_log_out);
        changePasswordButton = view.findViewById(R.id.profile_change_password);

        // Assign listeners
        logOutButton.setOnClickListener(logOutButtonListener);
        editProfileButton.setOnClickListener(editProfileListener);
        changePasswordButton.setOnClickListener(changePasswordListener);



        return view;
    }

    private void setNameEmail() {
        // Muhammad Aamir Ali | Aug 27 '13
        // https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
        SharedPreferences mPrefs = getActivity().getSharedPreferences("UserData",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString("UserDetailsObject",null);
        userModel = gson.fromJson(json, UserModel.class);

        if(userModel != null){
            name.setText(userModel.getUserName());
            phone.setText(userModel.getPhoneNumber());
        }else{
            name.setText("User Bahadur");
        }

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    final View.OnClickListener logOutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Android AlertDialog Example
            // https://www.javatpoint.com/android-alert-dialog-example
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure you want to Sign Out?")
                    .setCancelable(false)
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getContext(), LogInActivity.class));
                            getActivity().finish();
                        }
                    }).setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            //Creating dialog box
            AlertDialog alert = builder.create();

            //Setting the title manually
            alert.show();
        }
    };

    final View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), EditPasswordActivity.class));
        }
    };


    final View.OnClickListener editProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), EditProfileActivity.class));
        }
    };
}