package com.example.socialmedia.fragments;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    private TextView changeProfileImage;
    private EditText username, name, bio;
    private ImageView update, profileImage;
    private Uri imageUri;
    private Bitmap image;
    final int THUMBSIZE = 384;
    private UploadTask uploadTask;
    private StorageReference firebaseStorage;

    public EditProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        getData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImage();
                update.setEnabled(false);
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                Picasso.get().load(result).fit().centerCrop().into(profileImage);
                imageUri = result;
            }
        }
    });

    private void compressImage() {

        if (imageUri != null) {

            Calendar calendar = Calendar.getInstance();

            firebaseStorage = FirebaseStorage.getInstance().getReference().child("POSTS").child(prevalentuser.ourData.getUsername());
            try {
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            image = ThumbnailUtils.extractThumbnail(image,
                    THUMBSIZE, THUMBSIZE);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] thumb = baos.toByteArray();

            final StorageReference referenceThumb = firebaseStorage.child(String.valueOf(calendar.getTimeInMillis()));
            uploadTask = referenceThumb.putBytes(thumb);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return referenceThumb.getDownloadUrl();
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    setData(uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("storage error", "onFailure: " + e.getMessage());
                }
            });
        } else setData(prevalentuser.ourData.getProfilePic());
    }

    private void setData(String url) {

        String biO = bio.getText().toString();
        String namE = name.getText().toString();


        Map<String, Object> map = new HashMap<>();
        map.put("bio", biO);
        map.put("name", namE);
        map.put("profileUrl", url);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String uid = currentUser.getUid();
        firestore.collection("USERINFO").document(uid).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                prevalentuser.ourData.setProfilePic(url);
                prevalentuser.ourData.setName(namE);
                prevalentuser.ourData.setBio(biO);

                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
        });

        firestore.collection("POSTSANDSTORIES").document(prevalentuser.ourData.getUsername()).update(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
        update.setEnabled(true);
    }

    private void getData() {

        username.setText(prevalentuser.ourData.getUsername());
        name.setText(prevalentuser.ourData.getName());
        bio.setText(prevalentuser.ourData.getBio());
        Glide.with(getContext()).load(prevalentuser.ourData.getProfilePic()).placeholder(R.drawable.profile).into(profileImage);

    }

    private void init(View view) {

        username = view.findViewById(R.id.username_edittext);
        name = view.findViewById(R.id.name_edittext);
        bio = view.findViewById(R.id.bio_edittext);
        changeProfileImage = view.findViewById(R.id.change_profile_picture);
        update = view.findViewById(R.id.edit_profile_toolbar_update);
        profileImage = view.findViewById(R.id.profile_picture_image);
    }


}