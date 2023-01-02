package com.example.socialmedia.registration;

import android.os.Bundle;
import android.util.Log;
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

import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateNewAccount extends Fragment implements View.OnClickListener {

    private static final String TAG = "CREATEACCOUNT";
    private ProgressBar progressCircle;
    private TextView signIn, usernameError, phoneOrEmailError, passwordError, confirmPassError;
    private String userNameRegex = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    private String emailRegex = "^[a-zA-Z0-9+_.-]{4,}@[a-zA-Z0-9-]+\\.[a-zA-Z0-9.]{2,5}$";

    private String passwordRegex = "[\\w@#$%&*()_]{7,20}";

    private String phoneRegex = "^[0-9]{10}$";

    private Button signUpButton;
    private EditText usernameBox, phoneOrEmailBox, passwordBox, confirmPassBox;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public CreateNewAccount() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        signIn.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void init(View view) {
        progressCircle = view.findViewById(R.id.register_progress);
        signIn = view.findViewById(R.id.signIn);
        signUpButton = view.findViewById(R.id.signup_Button);
        usernameBox = view.findViewById(R.id.username);
        usernameError = view.findViewById(R.id.username_error);
        phoneOrEmailBox = view.findViewById(R.id.phone_or_email);
        phoneOrEmailError = view.findViewById(R.id.phone_or_email_error);
        passwordBox = view.findViewById(R.id.password);
        passwordError = view.findViewById(R.id.password_error);
        confirmPassBox = view.findViewById(R.id.confirm_password);
        confirmPassError = view.findViewById(R.id.confirm_password_error);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signIn:
                ((RegisterActivity) getActivity()).setFragment(new LoginFragment());
                break;

            case R.id.signup_Button:
                validate();
                break;
        }

    }

    private void validate() {
        String username = usernameBox.getText().toString();
        String phoneoremail = phoneOrEmailBox.getText().toString();
        String password = passwordBox.getText().toString();
        String confirmPassword = confirmPassBox.getText().toString();

        HashMap<String, Object> userInfo = new HashMap<>();

        if (!(username.equals("") && phoneoremail.equals("") && password.equals(""))) {


            if (!username.equals("")) {
                if (username.matches(userNameRegex)) {
                    usernameError.setVisibility(View.GONE);
                    userInfo.put("username", username);

                } else {

                    String dotAtStart = "^[._]+\\w+";
                    String dotAtEnd = "\\w+[._]+$";

                    String nextToEachOther = "(\\w*)?[_.]{2,}(\\w*)?";


                    if (username.length() < 8 || username.length() > 20) {
                        usernameError.setText("characters must be between 8 to 20");

                    } else if (username.matches(dotAtStart) || username.matches(dotAtEnd)) {
                        usernameError.setText("' . ' or ' _ ' not allowed at start or end");

                    } else if (username.matches(nextToEachOther)) {
                        usernameError.setText("' . ' or ' _ ' can't be next to each other");

                    } else {
                        usernameError.setText("should only contain a - z ,   0 - 9 ,   ' . '  and  ' _ '");

                    }

                    usernameError.setVisibility(View.VISIBLE);

                }
            }

            if (!phoneoremail.equals("")) {
                if (phoneoremail.matches(emailRegex)) {

                    phoneOrEmailError.setVisibility(View.GONE);

                    userInfo.put("email", phoneoremail);

                } else if (phoneoremail.matches(phoneRegex)) {

                    phoneOrEmailError.setVisibility(View.GONE);

                    userInfo.put("phone", phoneoremail);

                } else {

                    phoneOrEmailError.setText("Invalid email or phone number");
                    phoneOrEmailError.setVisibility(View.VISIBLE);

                }
            }

            if (!password.equals("")) {
                if (password.matches(passwordRegex)) {


                    Pattern pattern = Pattern.compile("[0-9]+");
                    Matcher m = pattern.matcher(password);

                    if (m.find()) {
                        passwordError.setVisibility(View.GONE);
                        userInfo.put("password", password);

                    } else {
                        passwordError.setText("password must contain number ");
                        passwordError.setVisibility(View.VISIBLE);
                    }

                } else {

                    if (password.length() < 7 || password.length() > 20) {

                        passwordError.setText("password must be between 7 to 20 characters");

                    } else {

                        Pattern pattern = Pattern.compile("[\\s]+");
                        Matcher m = pattern.matcher(password);

                        if (m.find()) {
                            passwordError.setText("space between characters is not allowed");
                        } else {
                            passwordError.setText("allowed characters are ' @  ' #  ' $ ' % '  &  '  * '  (  '  )  ' _ ' ");
                        }

                    }
                    passwordError.setVisibility(View.VISIBLE);

                }
            }

            if (!confirmPassword.equals("")) {

                if (confirmPassword.equals(password)) {
                    confirmPassError.setVisibility(View.GONE);
                    userInfo.put("confirmPassword", confirmPassword);
                } else {
                    confirmPassError.setText("password did not match");
                    confirmPassError.setVisibility(View.VISIBLE);
                }

            }

        }

        if (userInfo.get("username") != null && (userInfo.get("email") != null || userInfo.get("phone") != null)
                && userInfo.get("password") != null && userInfo.get("confirmPassword") != null) {

            creatAccount(userInfo);

        }

    }

    private void creatAccount(HashMap<String, Object> userInfo) {

        signUpButton.setEnabled(false);
        signUpButton.setAlpha(0.5f);

        userInfo.remove("confirmPassword");
        String username = userInfo.get("username").toString();

        Map<String, Object> status = new HashMap<>();
        status.put("status", "open");

        progressCircle.setVisibility(View.VISIBLE);


        firebaseFirestore.collection("POSTSANDSTORIES").document(username).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {

                            firebaseAuth.createUserWithEmailAndPassword(userInfo.get("email").toString(), userInfo.get("password").toString()).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    signUpButton.setEnabled(true);
                                    signUpButton.setAlpha(1f);
                                    progressCircle.setVisibility(View.GONE);


                                }
                            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    String uid = authResult.getUser().getUid();



                                    firebaseFirestore.collection("USERINFO").document(uid).set(userInfo).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressCircle.setVisibility(View.GONE);
                                            signUpButton.setEnabled(true);
                                            signUpButton.setAlpha(1f);
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "onFailure: " + e.getMessage());
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {


                                            firebaseFirestore.collection("POSTSANDSTORIES").document(username)
                                                    .set(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    firebaseAuth.getCurrentUser().sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getContext().getApplicationContext(), "Verify Email", Toast.LENGTH_SHORT).show();
                                                                        progressCircle.setVisibility(View.GONE);
                                                                        ((RegisterActivity) getActivity()).setFragment(new LoginFragment());
                                                                    }
                                                                }
                                                            });

//                                                    progressCircle.setVisibility(View.GONE);
//                                                    ((RegisterActivity) getActivity()).setFragment(new LoginFragment());

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressCircle.setVisibility(View.GONE);
                                                    signUpButton.setEnabled(true);
                                                    signUpButton.setAlpha(1f);
                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                                }
                                            });


                                        }
                                    });

                                }
                            });

                        } else {
                            usernameError.setText("username already exists");
                        }
                    }
                }
        );


    }

}

