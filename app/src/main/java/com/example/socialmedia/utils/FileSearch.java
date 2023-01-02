package com.example.socialmedia.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class FileSearch {

    public static ArrayList<String> getDirectoryPaths(String directory) {

        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();


        for (int i = 0; i < listfiles.length; i++) {
            if (listfiles[i].isDirectory()) {
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }

        return pathArray;
    }

    public static ArrayList<String> getFilePaths(String directory) {

        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles;

        if (file.listFiles() == null) {

            System.out.println(file.getName());
            // have the object build the directory structure, if needed.
            file.mkdirs();
            // create a File object for the output file
            File outputFile = new File(String.valueOf(file));
            // now attach the OutputStream to the file object, instead of a String representation
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//            return null;
        }


        listfiles = file.listFiles();


        for (int i = 0; i < listfiles.length; i++) {

            if (listfiles[i].isFile()) {
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }


        return pathArray;

    }
}


/**
 * public class FileSearch {
 * <p>
 * <p>
 * // Search a directory and return a list of all **directories** contained inside
 * <p>
 * public static ArrayList<String> getDirectoryPaths(String directory){
 * ArrayList<String> pathArray = new ArrayList<>();
 * File file = new File(directory);
 * File[] listfiles = file.listFiles();
 * for(int i = 0; i < listfiles.length; i++){
 * if(listfiles[i].isDirectory()){
 * pathArray.add(listfiles[i].getAbsolutePath());
 * }
 * }
 * return pathArray;
 * }
 * <p>
 * <p>
 * //Search a directory and return a list of all **files** contained inside
 * <p>
 * public static ArrayList<String> getFilePaths(String directory){
 * ArrayList<String> pathArray = new ArrayList<>();
 * File file = new File(directory);
 * File[] listfiles = file.listFiles();
 * for(int i = 0; i < listfiles.length; i++){
 * if(listfiles[i].isFile()){
 * pathArray.add(listfiles[i].getAbsolutePath());
 * }
 * }
 * return pathArray;
 * }
 * }
 */