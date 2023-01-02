package com.example.socialmedia.addpostfragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmedia.BuildConfig;
import com.example.socialmedia.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class CameraFragment extends Fragment {

    private ImageView photo;
    private Button openCamera;
    private String mCurrentPhotoPath;
    private Uri uri;
    private String currentPhotoPath;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        openCamera=view.findViewById(R.id.open_camera);
        photo=view.findViewById(R.id.photo);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSomeActivityForResult();
            }
        });
    }



    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        photo.setBackgroundColor(0);
                        photo.setImageURI(uri);


                    }
                }
            });

    @SuppressLint("QueryPermissionsNeeded")
    public void openSomeActivityForResult() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(requireContext(),
                        BuildConfig.APPLICATION_ID+".fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                cameraActivityResultLauncher.launch(intent);
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(),"/Pictures2");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println(currentPhotoPath);
        return image;
    }




}

/*12/8/21
public void openSomeActivityForResult() {

        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        cameraActivityResultLauncher.launch(intent);

    }
 */

/*2

Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// ------- add below lines to store the photos to custom directory -------
File imagesFolder = new File(Environment.getExternalStorageDirectory(), "My Images");
imagesFolder.mkdirs();
mUri = Uri.fromFile(new File(imagesFolder, "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
startActivityForResult(intent, REQUEST_CAMERA);

 */

/*3
//camera stuff
Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

//folder stuff
File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
imagesFolder.mkdirs();

File image = new File(imagesFolder, "QR_" + timeStamp + ".png");
Uri uriSavedImage = Uri.fromFile(image);

imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

 */