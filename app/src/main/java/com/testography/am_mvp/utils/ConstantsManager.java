package com.testography.am_mvp.utils;

public class ConstantsManager {
    public static final String CUSTOM_FONTS_ROOT = "fonts/";
    public static final String CUSTOM_FONT_NAME = "PTBebasNeueRegular.ttf";

    public static final String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";
    public static final String PHOTO_FILE_PREFIX = "IMG_";

    public static final int REQUEST_CAMERA_PICTURE = 99;
    public static final int REQUEST_GALLERY_PICTURE = 88;

    public static final int REQUEST_PERMISSION_CAMERA = 3000;
    public static final int REQUEST_PERMISSION_READ_WRITE_STORAGE = 3001;

    public static final int REQUEST_PROFILE_PHOTO_PICKER = 1001;
    public static final int REQUEST_PROFILE_PHOTO_CAMERA = 1002;
    public static final String FILE_PROVIDER_AUTHORITY = "com.testography.am_mvp" +
            ".fileprovider";
}
