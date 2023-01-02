package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.example.socialmedia.fragments.HomeFragment;
import com.example.socialmedia.models.UsersData;
import com.example.socialmedia.prevalent.prevalentuser;
import com.example.socialmedia.registration.RegisterActivity;
import com.example.socialmedia.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

            if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()){
                SystemClock.sleep(500);
                Intent intent = new Intent(MainActivity.this, Homepage.class);
                startActivity(intent);
                finish();
            }else {
                SystemClock.sleep(500);
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

    }
}
