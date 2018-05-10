package com.example.bexuma.apollographqlrailsapponheroku.authentication;


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
import com.example.bexuma.apollographqlrailsapponheroku.SignInMutation;
import com.example.bexuma.apollographqlrailsapponheroku.SignUpMutation;
import com.example.bexuma.apollographqlrailsapponheroku.models.User;

import javax.annotation.Nonnull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText name, email, password;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        name = v.findViewById(R.id.editTextName);
        email = v.findViewById(R.id.editTextEmail);
        password = v.findViewById(R.id.editTextPassword);

        Button signUp = v.findViewById(R.id.buttonSignUp);
        TextView goToSignIn = v.findViewById(R.id.buttonToSignIn);

        signUp.setOnClickListener(v1 -> MyApolloClient.getMyApolloClient().mutate(SignUpMutation.builder()
                .name(name.getText().toString())
                .email(email.getText().toString())
                .password(password.getText().toString())
                .build())
                .enqueue(new ApolloCall.Callback<SignUpMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SignUpMutation.Data> response) {
                        SignUpMutation.Data data = response.data();

                        if (data.createUser() != null) {

                            Log.d("SignUpFragmentSU", "onResponse: " + data.createUser());

                            MainActivity.getMainActivity().runOnUiThread(() ->
                                    Toast.makeText(MainActivity.getMainActivity(), "User was successully created!", Toast.LENGTH_LONG).show());

                            signInUser();
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
                }));



        goToSignIn.setOnClickListener(v12 -> MainActivity.getMainActivity().openSignInFragment());


        return v;
    }

    public void signInUser() {
        MyApolloClient.getMyApolloClient().mutate(SignInMutation.builder()
                .email(email.getText().toString())
                .password(password.getText().toString())
                .build())
                .enqueue(new ApolloCall.Callback<SignInMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SignInMutation.Data> response) {
                        SignInMutation.Data data = response.data();

                        if (data.signInUser() != null) {

                            Log.d("SignUpFragmentSI", "onResponse: " + data.signInUser().user());

                            String token = data.signInUser().token();
                            String name = data.signInUser().user().name();
                            String email = data.signInUser().user().email();

                            User user = new User(name, email, token);

                            MainActivity.getMainActivity().runOnUiThread(() ->
                                    Toast.makeText(MainActivity.getMainActivity(), user.getName() + "User authenticated!", Toast.LENGTH_LONG).show());

                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.d("SignUpFragmentSI", "onResponse: " + e.getLocalizedMessage());
                        MainActivity.getMainActivity().runOnUiThread(() -> Toast.makeText(MainActivity.getMainActivity(), "Something went wrong", Toast.LENGTH_LONG).show());
                    }
                });
    }

}
