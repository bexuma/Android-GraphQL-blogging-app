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
import com.example.bexuma.apollographqlrailsapponheroku.SignInMutation;
import com.example.bexuma.apollographqlrailsapponheroku.models.User;

import javax.annotation.Nonnull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText email, password;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.getMainActivity());


        email = v.findViewById(R.id.editTextEmail);
        password = v.findViewById(R.id.editTextPassword);
        Button signIn = v.findViewById(R.id.buttonSignIn);
        TextView goToSignUp = v.findViewById(R.id.buttonToSignUp);


        signIn.setOnClickListener(v1 -> {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    () -> {
                        login();
                        progressDialog.dismiss();
                    }, 3000
            );
        });



        goToSignUp.setOnClickListener(v12 -> MainActivity.getMainActivity().openSignUpFragment());

        return v;
    }

    public void login() {
        MyApolloClient.getMyApolloClient().mutate(SignInMutation.builder()
                .email(email.getText().toString())
                .password(password.getText().toString())
                .build())
                .enqueue(new ApolloCall.Callback<SignInMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<SignInMutation.Data> response) {
                        SignInMutation.Data data = response.data();

                        if (data.signInUser() != null) {

                            Log.d("MainActivity", "onResponse: " + data.signInUser().user());

                            String token = data.signInUser().token();
                            String name =  data.signInUser().user().name();
                            String email = data.signInUser().user().email();

                            User user = new User(name, email, token);

                            MainActivity.getMainActivity().runOnUiThread(() ->
                                    Toast.makeText(MainActivity.getMainActivity(), user.getName() + "User authenticated!", Toast.LENGTH_LONG).show());
                        }

                        else {
                            MainActivity.getMainActivity().runOnUiThread(() ->
                                    Toast.makeText(MainActivity.getMainActivity(), "Wrong email or password", Toast.LENGTH_LONG).show());
                        }

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.d("MainActivity", "onResponse: " + e.getLocalizedMessage());
                        MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.getMainActivity(), "Something went wrong", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
    }

}
