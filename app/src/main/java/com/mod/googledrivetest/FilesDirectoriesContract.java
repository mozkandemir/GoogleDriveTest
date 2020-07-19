package com.mod.googledrivetest;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FilesDirectoriesContract {
    static final String TABLE_NAME = "FilesDirectories";


    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String NAME = "Name";
        public static final String A_NAME = "AncestorName";
        public static final String E_NAME = "Extension";
        private Columns() {
            // private constructor to prevent instantiation
        }
    }
    /**
     * The URI to access the table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AppProvider.CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AppProvider.CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AppProvider.CONTENT_AUTHORITY + "." + TABLE_NAME;

    public static Uri buildFileUri(long fileId) {
        return ContentUris.withAppendedId(CONTENT_URI, fileId);
    }

    public static long getFileId(Uri uri) {
        return ContentUris.parseId(uri);
    }

}

