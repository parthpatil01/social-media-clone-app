package com.example.socialmedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchCompat;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private Button changePrivacy;
    private ImageView clickableArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchCompat = findViewById(R.id.switchButton);
        clickableArea = findViewById(R.id.clickableArea);

        switchCompat.setClickable(false);

        setPrivacyStatus();

        clickableArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog=new BottomSheetDialog(SettingsActivity.this, R.style.Theme_AppCompat_BottomSheetDialogTheme);
                bottomSheetView = LayoutInflater.from(SettingsActivity.this).inflate(R.layout.privacy_dialog,
                        null);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                init();
            }
        });

    }

    private void state(boolean isChecked) {
        prevalentuser.changePrivacy(isChecked);

    }

    private void setPrivacyStatus() {
        prevalentuser.privacyStatus();
        buttonset();
    }

    private void buttonset() {
        if (prevalentuser.privacyStatus.equals("open")) {
            switchCompat.setChecked(false);
        } else if (prevalentuser.privacyStatus.equals("hidden")) {
            switchCompat.setChecked(true);
        }

    }

    private void init(){
        changePrivacy=bottomSheetView.findViewById(R.id.change_privacy);
        changePrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prevalentuser.privacyStatus.equals("open")) {
                    state(true);
                    switchCompat.setChecked(true);
                }
                else if(prevalentuser.privacyStatus.equals("hidden")) {
                    state(false);
                    switchCompat.setChecked(false);
                }
                bottomSheetDialog.cancel();
            }
        });

    }


}


//        switchCompat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog=new BottomSheetDialog(SettingsActivity.this, R.style.Theme_AppCompat_BottomSheetDialogTheme);
//                bottomSheetView = LayoutInflater.from(SettingsActivity.this).inflate(R.layout.privacy_dialog,
//                        null);
//                bottomSheetDialog.setContentView(bottomSheetView);
//                bottomSheetDialog.show();
//                init();
//            }
//        });