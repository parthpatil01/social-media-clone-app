package com.example.socialmedia.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialmedia.FilterActivity;
import com.example.socialmedia.Homepage;
import com.example.socialmedia.R;
import com.example.socialmedia.SquareImage;
import com.example.socialmedia.prevalent.prevalentuser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterFragment extends Fragment implements View.OnClickListener {

    private ImageView croppedImage;
    private SquareImage filter1, filter2, filter3, filter4, filter5, filter6, noFilter, captionImage;
    private TextView nextButton;
    private Uri uri;
    private Bitmap image;
    private boolean[] Applied = {false, false, false, false, false, false};

    private Bitmap[] set = new Bitmap[6];
    private Bitmap[] thumbnails = new Bitmap[6];
    private Bitmap lastClicked;

    final int THUMBSIZE = 384;

    private Bitmap ThumbImage;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;

    private StorageReference firebaseStorage;
    private UploadTask uploadTask, uploadTaskThumb;
    private Uri downloadUri, downloadUriThumbnail;
    private Task<Uri> mImage;
    private Task<Uri> mThumb;
    private EditText caption;

    public FilterFragment() {
        // Required empty public constructor
    }


    static {
        System.loadLibrary("NativeImageProcessor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        firebaseStorage = FirebaseStorage.getInstance().getReference().child("POSTS").child(prevalentuser.ourData.getUsername());


        uri = ((FilterActivity) getActivity()).myUri;


        try {

            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            croppedImage.setImageBitmap(image);

            ThumbImage = ThumbnailUtils.extractThumbnail(image,
                    THUMBSIZE, THUMBSIZE);

            settingThumbnails();


        } catch (IOException e) {
            e.printStackTrace();
        }


        setclickListeners();

    }

    private void init(View view) {
        croppedImage = view.findViewById(R.id.squareImage);
        noFilter = view.findViewById(R.id.nofilter);
        filter1 = view.findViewById(R.id.filter1);
        filter2 = view.findViewById(R.id.filter2);
        filter3 = view.findViewById(R.id.filter3);
        filter4 = view.findViewById(R.id.filter4);
        filter5 = view.findViewById(R.id.filter5);
        filter6 = view.findViewById(R.id.filter6);
        nextButton = view.findViewById(R.id.filter_next);
    }

    private void settingThumbnails() {
        applyFilter1(true);
        applyFilter2(true);
        applyFilter3(true);
        applyFilter4(true);
        applyFilter5(true);
        applyFilter6(true);


        Glide.with(requireContext()).load(thumbnails[0]).into(filter1);
        Glide.with(requireContext()).load(thumbnails[1]).into(filter2);
        Glide.with(requireContext()).load(thumbnails[2]).into(filter3);
        Glide.with(requireContext()).load(thumbnails[3]).into(filter4);
        Glide.with(requireContext()).load(thumbnails[4]).into(filter5);
        Glide.with(requireContext()).load(thumbnails[5]).into(filter6);
        Glide.with(requireContext()).load(ThumbImage).into(noFilter);

        lastClicked = ThumbImage;
    }


    private void setclickListeners() {
        noFilter.setOnClickListener(this);
        filter1.setOnClickListener(this);
        filter2.setOnClickListener(this);
        filter3.setOnClickListener(this);
        filter4.setOnClickListener(this);
        filter5.setOnClickListener(this);
        filter6.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.filter_next:
                post();
                break;

            case R.id.nofilter:
                applyNoFilter();
                break;

            case R.id.filter1:
                if (!Applied[0]) {
                    applyFilter1(false);

                }

                lastClicked = thumbnails[0];
                croppedImage.setImageBitmap(set[0]);
                break;

            case R.id.filter2:
                if (!Applied[1]) {
                    applyFilter2(false);

                }

                lastClicked = thumbnails[1];
                croppedImage.setImageBitmap(set[1]);
                break;

            case R.id.filter3:
                if (!Applied[2]) {
                    applyFilter3(false);

                }

                lastClicked = thumbnails[2];
                croppedImage.setImageBitmap(set[2]);
                break;

            case R.id.filter4:
                if (!Applied[3]) {
                    applyFilter4(false);

                }

                lastClicked = thumbnails[3];
                croppedImage.setImageBitmap(set[3]);
                break;

            case R.id.filter5:
                if (!Applied[4]) {
                    applyFilter5(false);

                }

                lastClicked = thumbnails[4];
                croppedImage.setImageBitmap(set[4]);
                break;

            case R.id.filter6:
                if (!Applied[5]) {
                    applyFilter6(false);
                }

                lastClicked = thumbnails[5];
                croppedImage.setImageBitmap(set[5]);
                break;

        }
    }

    private void post() {


        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.Theme_AppCompat_BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.finalpost,
                null);
        captionImage = bottomSheetView.findViewById(R.id.caption_image);
        LinearLayout container = (LinearLayout) bottomSheetView.findViewById(R.id.container);
        Button post = bottomSheetView.findViewById(R.id.post_button_in_dialog);
        caption = bottomSheetView.findViewById(R.id.captionEt);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();


        captionImage.setImageBitmap(lastClicked);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.VISIBLE);
                bottomSheetDialog.setCancelable(false);
                uploadToCloud();
                post.setEnabled(false);
                post.setAlpha(0.5f);
            }
        });

    }


    private void uploadToCloud() {

        Calendar calendar = Calendar.getInstance();


        croppedImage.setDrawingCacheEnabled(true);
        croppedImage.buildDrawingCache();
        Bitmap bitmap = croppedImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();
        final StorageReference referenceImage = firebaseStorage.child(String.valueOf(calendar.getTimeInMillis()));
        uploadTask = referenceImage.putBytes(data);

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        lastClicked.compress(Bitmap.CompressFormat.JPEG, 50, baos1);
        byte[] thumb = baos1.toByteArray();

        final StorageReference referenceThumb = firebaseStorage.child(String.valueOf(calendar.getTimeInMillis() + 1));
        uploadTaskThumb = referenceThumb.putBytes(thumb);


        mImage = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return referenceImage.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUri = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("storage error", "onFailure: " + e.getMessage());
            }
        });


        mThumb = uploadTaskThumb.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                downloadUriThumbnail = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("storage error", "onFailure: " + e.getMessage());
            }
        });


        // All Tasks Success

        Tasks.whenAllSuccess(mImage, mThumb).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                System.out.println("image url " + downloadUri);
                System.out.println("thumb url " + downloadUriThumbnail);

                Log.d("all tasks success", "onSuccess: ");
                uploadToDatabase();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bottomSheetDialog.cancel();
                Log.d("all tasks failure", "onFailure: " + e.getMessage());
            }
        });


    }

    private void uploadToDatabase() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int strDate= Integer.parseInt(formatter.format(date));

        Map<String, Object> map = new HashMap<>();
        map.put("imageUrl", downloadUri.toString());
        map.put("thumbnailUrl", downloadUriThumbnail.toString());
        map.put("date",strDate);
        map.put("likes",0);
        map.put("comments",0);


        String mCaption = caption.getText().toString();

        if (!mCaption.equals("")) {
            System.out.println("caption is not null");
            map.put("caption",mCaption);
        }

        FirebaseFirestore.getInstance().collection("POSTSANDSTORIES").document(prevalentuser.ourData.getUsername())
                .collection("POSTS").add(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                Log.d("uploadToDatabase", "onFailure: " + e.getMessage());
                bottomSheetDialog.cancel();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                bottomSheetDialog.cancel();
                Log.d("uploadToDatabase", "onSuccess: ");
                getActivity().onBackPressed();
            }
        });
    }

    private void applyNoFilter() {

        lastClicked = ThumbImage;
        croppedImage.setImageBitmap(image);
        noFilter.setImageBitmap(ThumbImage);


    }


    private void applyFilter1(boolean tn) {

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(30));
        myFilter.addSubFilter(new ContrastSubFilter(1.1f));

        if (!tn) {
            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[0] = myFilter.processFilter(copy);
            Applied[0] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[0] = myFilter.processFilter(copy);
        }
    }

    private void applyFilter2(boolean tn) {
        Filter myFilter = SampleFilters.getAweStruckVibeFilter();

        if (!tn) {
            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[1] = myFilter.processFilter(copy);
            Applied[1] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[1] = myFilter.processFilter(copy);
        }


    }

    private void applyFilter3(boolean tn) {
        Filter myFilter = SampleFilters.getNightWhisperFilter();

        if (!tn) {
            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[2] = myFilter.processFilter(copy);
            Applied[2] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[2] = myFilter.processFilter(copy);
        }


    }

    private void applyFilter4(boolean tn) {
        Filter myFilter = SampleFilters.getLimeStutterFilter();

        if (!tn) {

            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[3] = myFilter.processFilter(copy);
            Applied[3] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[3] = myFilter.processFilter(copy);
        }


    }

    private void applyFilter5(boolean tn) {
        Filter myFilter = SampleFilters.getStarLitFilter();

        if (!tn) {

            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[4] = myFilter.processFilter(copy);
            Applied[4] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[4] = myFilter.processFilter(copy);
        }


    }

    private void applyFilter6(boolean tn) {
        Filter myFilter = SampleFilters.getBlueMessFilter();
        if (!tn) {

            Bitmap copy = image.copy(Bitmap.Config.ARGB_8888, true);
            set[5] = myFilter.processFilter(copy);
            Applied[5] = true;
        } else {
            Bitmap copy = ThumbImage.copy(Bitmap.Config.ARGB_8888, true);
            thumbnails[5] = myFilter.processFilter(copy);
        }


    }

}

