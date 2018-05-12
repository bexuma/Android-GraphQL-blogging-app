package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.bexuma.apollographqlrailsapponheroku.activities.EntryActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.SignUpMutation;

import javax.annotation.Nonnull;

public class SignUpFragment extends Fragment {

    private EditText nameEditText, emailEditText, passwordEditText;
    private String name, email, password;
    private Button signUp;
    private ProgressDialog progressDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        nameEditText = v.findViewById(R.id.editTextName);
        emailEditText = v.findViewById(R.id.editTextEmail);
        passwordEditText = v.findViewById(R.id.editTextPassword);

        signUp = v.findViewById(R.id.buttonSignUp);
        TextView goToSignIn = v.findViewById(R.id.buttonToSignIn);

        progressDialog = new ProgressDialog(EntryActivity.getEntryActivity());

        signUp.setOnClickListener(v1 -> signUpAttempt());


        goToSignIn.setOnClickListener(v12 -> EntryActivity.getEntryActivity().openSignInFragment());


        return v;
    }

    public void signUpAttempt() {
        name = nameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (!isEmailValid(email) || !isPasswordValid(password) || !isNameValid(name)) {
            onSignUpFailed();
            return;
        }

        signUp.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating new user...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                () -> {
                    signUp.setEnabled(true);
                    register(name, email, password);

                    progressDialog.dismiss();
                }, 3000
        );

    }

    public void onSignUpFailed() {
        Toast.makeText(EntryActivity.getEntryActivity(), "Signup failed", Toast.LENGTH_LONG).show();
        signUp.setEnabled(true);

    }

    public void register(String name, String email, String password) {
        MyApolloClient.getMyApolloClient().mutate(SignUpMutation.builder()
                .name(name)
                .email(email)
                .password(password)
                .build())
                .enqueue(signUpMutationCallback);
    }

    private ApolloCall.Callback<SignUpMutation.Data> signUpMutationCallback = new ApolloCall.Callback<SignUpMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<SignUpMutation.Data> response) {
            SignUpMutation.Data data = response.data();

            assert data != null;

            if (data.createUser() != null) {

                Log.d(EntryActivity.TAG, "onResponse: " + data.createUser());

                EntryActivity.getEntryActivity().runOnUiThread(() -> {
                    Toast.makeText(EntryActivity.getEntryActivity(), "User was successully created!", Toast.LENGTH_SHORT).show();

                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                    new android.os.Handler().postDelayed(
                            () -> {
                                signUp.setEnabled(true);
                                SignInFragment.authenticate(email, password);
                                progressDialog.dismiss();
                            }, 3000
                    );

                });

            } else {
                EntryActivity.getEntryActivity().runOnUiThread(() ->
                        Toast.makeText(EntryActivity.getEntryActivity(), "User was not created!", Toast.LENGTH_LONG).show());
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(EntryActivity.TAG, "onResponse: " + e.getLocalizedMessage());
            EntryActivity.getEntryActivity().runOnUiThread(() ->
                    Toast.makeText(EntryActivity.getEntryActivity(), "Something went wrong", Toast.LENGTH_LONG).show());

        }
    };

    private boolean isNameValid(String name) {
        if (name.length() >= 4) {
            nameEditText.setError(null);
            return true;
        } else {
            nameEditText.setError("from 4");
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        if (email.contains("@")) {
            emailEditText.setError(null);
            return true;
        } else {
            emailEditText.setError("enter a valid email address");
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() >= 6) {
            passwordEditText.setError(null);
            return true;
        } else {
            passwordEditText.setError("from 6");
            return false;
        }
    }


}
