package com.example.socialmedia.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.socialmedia.Homepage;
import com.example.socialmedia.R;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextView signUpButton;
    private EditText usernameTV, passwordTV;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressCircle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setUpOnClickListener();

        mAuth = FirebaseAuth.getInstance();

    }



    private void init(View view) {

        signUpButton = view.findViewById(R.id.signUp);
        usernameTV = view.findViewById(R.id.login_username);
        passwordTV = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_button);
        progressCircle = view.findViewById(R.id.login_progress);
    }

    private void setUpOnClickListener() {
        signUpButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signUp:
                ((RegisterActivity) getActivity()).setFragment(new CreateNewAccount());
                break;

            case R.id.login_button:
                checkDataFields();
                break;
        }

    }

    private void checkDataFields() {
        String username = usernameTV.getText().toString();
        String password = passwordTV.getText().toString();


        if (username.equals("") || password.equals("")) {
            Toast.makeText(getContext(), "Credentials can't be empty", Toast.LENGTH_SHORT).show();

        }else if(password.length()<10){
            Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
        }else {
            loginAuth(username,password);
        }
    }

    private void loginAuth(String username, String password) {

        progressCircle.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);


            mAuth.signInWithEmailAndPassword(username, password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressCircle.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1f);
                }
            }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        if(mAuth.getCurrentUser().isEmailVerified()) {
                            progressCircle.setVisibility(View.GONE);
                            Intent intent = new Intent(getContext(), Homepage.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getContext(), "Verify Email", Toast.LENGTH_SHORT).show();
                            progressCircle.setVisibility(View.GONE);
                            loginButton.setEnabled(true);
                            loginButton.setAlpha(1f);
                        }


                    } else {
                        progressCircle.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        loginButton.setAlpha(1f);
                    }

                }
            });


    }


}