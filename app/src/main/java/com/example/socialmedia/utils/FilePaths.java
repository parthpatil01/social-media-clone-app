package com.example.socialmedia.utils;

import android.os.Environment;

import java.io.File;

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String PICTURES = ROOT_DIR + "/Pictures";
    public final String DOWNLOADS = ROOT_DIR + "/Download";
    public String CAMERA = ROOT_DIR + "/DCIM/Camera";
    public String SOCIALMEDIA= ROOT_DIR + "/Pictures/SocialMedia";
}
