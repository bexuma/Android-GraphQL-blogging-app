package com.example.bexuma.apollographqlrailsapponheroku.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.bexuma.apollographqlrailsapponheroku.activities.MainActivity;
import com.example.bexuma.apollographqlrailsapponheroku.MyApolloClient;
import com.example.bexuma.apollographqlrailsapponheroku.R;
import com.example.bexuma.apollographqlrailsapponheroku.SignInMutation;
import com.example.bexuma.apollographqlrailsapponheroku.models.User;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.bexuma.apollographqlrailsapponheroku.activities.MainActivity.userLocalStore;


public class SignInFragment extends Fragment {


    private EditText emailEditText, passwordEditText;
    private String email, password;
    private static ProgressDialog progressDialog;
    private Button signIn;

    public SignInFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        progressDialog = new ProgressDialog(EntryActivity.getEntryActivity());
        emailEditText = v.findViewById(R.id.editTextEmail);
        passwordEditText = v.findViewById(R.id.editTextPassword);
        signIn = v.findViewById(R.id.buttonSignIn);
        TextView goToSignUp = v.findViewById(R.id.buttonToSignUp);


        signIn.setOnClickListener(v1 -> loginAttempt());

        goToSignUp.setOnClickListener(v12 -> EntryActivity.getEntryActivity().openSignUpFragment());

        return v;
    }

    public void loginAttempt() {
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if (!isEmailValid(email) || !isPasswordValid(password)) {
            onLoginFailed();
            return;
        }

        signIn.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                () -> {
                    signIn.setEnabled(true);
                    authenticate(email, password);

                }, 3000
        );

    }

    public void onLoginFailed() {
        Toast.makeText(EntryActivity.getEntryActivity(), "Login failed", Toast.LENGTH_LONG).show();
        signIn.setEnabled(true);

    }


    public static void authenticate(String email, String password) {
        MyApolloClient.getMyApolloClient(MainActivity.userLocalStore.getUserToken()).mutate(SignInMutation.builder()
                .email(email)
                .password(password)
                .build())
                .enqueue(signInMutationCallback);
    }

    private static ApolloCall.Callback<SignInMutation.Data> signInMutationCallback = new ApolloCall.Callback<SignInMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<SignInMutation.Data> response) {
            SignInMutation.Data data = response.data();

            assert data != null;
            if (data.signInUser() != null) {
                Log.d(EntryActivity.TAG, "onResponse: " + data.signInUser().user());

                String token = Objects.requireNonNull(data.signInUser()).token();
                String name = data.signInUser().user().name();
                String email = data.signInUser().user().email();

                User user = new User(name, email, token);

                EntryActivity.getEntryActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(EntryActivity.getEntryActivity(), user.getName() + "User authenticated!", Toast.LENGTH_LONG).show();

                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);

                    Intent startMainActivity = new Intent(EntryActivity.getEntryActivity(), MainActivity.class);

                    startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    EntryActivity.getEntryActivity().startActivity(startMainActivity);
                    EntryActivity.getEntryActivity().finish();

                });
            } else {
                EntryActivity.getEntryActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(EntryActivity.getEntryActivity(), "Wrong email or password", Toast.LENGTH_LONG).show();
                });
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(EntryActivity.TAG, "onResponse: " + e.getLocalizedMessage());
            EntryActivity.getEntryActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(EntryActivity.getEntryActivity(), "Something went wrong", Toast.LENGTH_LONG).show();


            });
        }
    };


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
