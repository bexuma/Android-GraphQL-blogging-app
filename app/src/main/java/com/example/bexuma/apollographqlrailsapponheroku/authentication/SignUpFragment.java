package com.example.bexuma.apollographqlrailsapponheroku.authentication;


import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.example.bexuma.apollographqlrailsapponheroku.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.SignUpMutation;

import java.util.concurrent.atomic.AtomicReference;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        nameEditText = v.findViewById(R.id.editTextName);
        emailEditText = v.findViewById(R.id.editTextEmail);
        passwordEditText = v.findViewById(R.id.editTextPassword);

        signUp = v.findViewById(R.id.buttonSignUp);
        TextView goToSignIn = v.findViewById(R.id.buttonToSignIn);

        progressDialog = new ProgressDialog(MainActivity.getMainActivity());


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAttempt();

            }
        });


        goToSignIn.setOnClickListener(v12 -> MainActivity.getMainActivity().openSignInFragment());


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
        Toast.makeText(MainActivity.getMainActivity(), "Signup failed", Toast.LENGTH_LONG).show();
        signUp.setEnabled(true);

    }

    public void register(String name, String email, String password) {
        MyApolloClient.getMyApolloClient().mutate(SignUpMutation.builder()
                .name(name)
                .email(email)
                .password(password)
                .build())
                .enqueue(new ApolloCall.Callback<SignUpMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SignUpMutation.Data> response) {
                        SignUpMutation.Data data = response.data();

                        if (data.createUser() != null) {

                            Log.d("SignUpFragmentSU", "onResponse: " + data.createUser());

                            MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.getMainActivity(), "User was successully created!", Toast.LENGTH_SHORT).show();

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

                                }
                            });

                        }

                        else {
                            MainActivity.getMainActivity().runOnUiThread(() ->
                                    Toast.makeText(MainActivity.getMainActivity(), "User was not created!", Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.d("SignUpFragmentSU", "onResponse: " + e.getLocalizedMessage());
                        MainActivity.getMainActivity().runOnUiThread(() ->
                                Toast.makeText(MainActivity.getMainActivity(), "Something went wrong", Toast.LENGTH_LONG).show());

                    }
                });
    }

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
