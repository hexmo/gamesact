package com.example.myapplication.utils;

import com.google.android.material.textfield.TextInputLayout;

public class Validator {

    public static boolean validateEmail(TextInputLayout textInputLayout){
        String value = textInputLayout.getEditText().getText().toString().trim();
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]+";

        if (value.isEmpty()) {
            textInputLayout.setError("Email field cannot be left empty.");
        } else if (!value.matches(pattern)) {
            textInputLayout.setError("Invalid email address.");
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    public static boolean validatePassword(TextInputLayout textInputLayout){
        String value = textInputLayout.getEditText().getText().toString().trim();
        String pattern = "(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}";

        if (value.isEmpty()) {
            textInputLayout.setError("Password cannot be empty.");
        } else if (value.length() < 8) {
            textInputLayout.setError("Password should be at least 8 characters long.");
        } else if (!value.matches(pattern)) {
            textInputLayout.setError("Password should contain at least one special character and one number.");
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }

        return false;
    }

    /**
     * @param password TextInputLayout of firstly entered password.
     * @param reEnteredPassword TextInputLayout of Re-entered password.
     * @return True if both passwords are same otherwise False
     */
    public static boolean validateReEnterPassword(TextInputLayout password,TextInputLayout reEnteredPassword){
        String originalPassword = password.getEditText().getText().toString();
        String value = reEnteredPassword.getEditText().getText().toString();

        if (value.isEmpty()) {
            reEnteredPassword.setError("This field cannot be left empty.");
        } else if (!value.equals(originalPassword)) {
            reEnteredPassword.setError("Both passwords do not match.");
        } else {
            reEnteredPassword.setError(null);
            reEnteredPassword.setErrorEnabled(false);
            return true;
        }
        return false;


    }

    public static boolean validatePhoneNumber(TextInputLayout textInputLayout){
        String value = textInputLayout.getEditText().getText().toString().trim();

        if (value.isEmpty()) {
            textInputLayout.setError("Phone number cannot be empty.");
        } else if (value.length() < 10) {
            textInputLayout.setError("Phone number should be 10 characters long.");
        } else if (!value.substring(0, 2).equals("98")) {
            textInputLayout.setError("Phone number should start with 98.");
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }

        return false;
    }

    public static boolean validateName(TextInputLayout textInputLayout){
        String value = textInputLayout.getEditText().getText().toString().trim();

        if (value.isEmpty()) {
            textInputLayout.setError("Full name cannot be left empty.");
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }

        return false;
    }

    public static boolean validateDataNotNull(TextInputLayout textInputLayout){
        String value = textInputLayout.getEditText().getText().toString().trim();

        if (value.isEmpty()) {
            textInputLayout.setError("This field can't be left empty.");
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
            return true;
        }

        return false;
    }


}
