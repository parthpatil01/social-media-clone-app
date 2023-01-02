package com.example.socialmedia.addpostfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.FilterActivity;
import com.example.socialmedia.R;
import com.example.socialmedia.adapters.AddPhotoGridAdapter;
import com.example.socialmedia.utils.FilePaths;
import com.example.socialmedia.utils.FileSearch;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ArrayList<String> directories;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private AddPhotoGridAdapter addPhotoGridAdapter;
    private ImageView photo;
    private ArrayList<String> currentFiles;
    private ArrayList<String> directoriesName;
    private TextView next;
    private String currentImageUri;


    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        init(view);
        getFiles();
        spinnerAdapter();

        return view;
    }


    public void init(View view) {

        directories = new ArrayList<>();
        spinner = view.findViewById(R.id.spinner);
        recyclerView = view.findViewById(R.id.gallery_recycler_view);
        photo = view.findViewById(R.id.gallery_photo);
        next = view.findViewById(R.id.gallery_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilterActivity.class);
                intent.putExtra("uri", currentImageUri);
                startActivity(intent);

            }
        });
    }


    private void getFiles() {


        FilePaths filePaths = new FilePaths();


        directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        directories.add(filePaths.DOWNLOADS);
        directories.add(filePaths.CAMERA);

        directoriesName = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {

            String file = directories.get(i);
            int index = file.lastIndexOf("/") + 1;
            String substring = file.substring(index);
            directoriesName.add(substring);


        }


    }

    public void spinnerAdapter() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_txt, directoriesName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFiles = FileSearch.getFilePaths(directories.get(position));

                if (currentFiles != null && currentFiles.size() != 0) {
                    next.setEnabled(true);
                    setupRecyclerView();
                }
                else spinner.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupRecyclerView() {
        currentImageUri = currentFiles.get(0);
        Glide.with(getContext()).load(currentImageUri).centerCrop().into(photo);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        addPhotoGridAdapter = new AddPhotoGridAdapter(getContext(), currentFiles, new AddPhotoGridAdapter.selectImage() {
            @Override
            public void imgClick(int position) {
                currentImageUri = currentFiles.get(position);
                Glide.with(getContext()).load(currentImageUri).centerCrop().into(photo);
            }
        });
        recyclerView.setAdapter(addPhotoGridAdapter);

    }

}


/**
 * private void init(){
 * FilePaths filePaths = new FilePaths();
 * <p>
 * //check for other folders indide "/storage/emulated/0/pictures"
 * if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
 * directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
 * }
 * <p>
 * directories.add(filePaths.CAMERA);
 * <p>
 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
 * android.R.layout.simple_spinner_item, directories);
 * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 * directorySpinner.setAdapter(adapter);
 * <p>
 * directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 *
 * @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
 * Log.d(TAG, "onItemClick: selected: " + directories.get(position));
 * <p>
 * //setup our image grid for the directory chosen
 * }
 * @Override public void onNothingSelected(AdapterView<?> parent) {
 * <p>
 * }
 * });
 * }
 */