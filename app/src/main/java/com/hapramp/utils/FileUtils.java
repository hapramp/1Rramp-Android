package com.hapramp.utils;

import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Ankit on 11/14/2017.
 */

public class FileUtils {

    private final int videoFileLimit = 16;
    private final int audioFileLimit  = 16;

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static boolean isVideo(String mimeType){
        return mimeType.substring(0,1).equals("v");
    }

    public static boolean isAudio(String mimeType){
        return mimeType.substring(0,1).equals("a");
    }

    public static boolean isImage(String mimeType){
        return mimeType.substring(0,1).equals("i");
    }

    public boolean isFileApplicable(String path){
        File file = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length()/1048576));
        return file_size < 16;
    }

}
