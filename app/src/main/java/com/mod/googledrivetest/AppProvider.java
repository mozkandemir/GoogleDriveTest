package com.mod.googledrivetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class AppProvider extends ContentProvider {
    private AppDataBase mOpenHelper;
    private static final int FILES_DIRECTORIES = 100;
    private static final int FILES_DIRECTORIES_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.mod.googledrivetest.provider";

    static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //  eg. content://com.mod.googledrivetest.provider/FilesDirectories
        matcher.addURI(CONTENT_AUTHORITY, FilesDirectoriesContract.TABLE_NAME, FILES_DIRECTORIES);
        // e.g. content:///com.mod.googledrivetest.provider/FilesDirectories/8
        matcher.addURI(CONTENT_AUTHORITY, FilesDirectoriesContract.TABLE_NAME + "/#", FILES_DIRECTORIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDataBase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int match = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(match) {
            case FILES_DIRECTORIES:
                queryBuilder.setTables(FilesDirectoriesContract.TABLE_NAME);
                break;

            case FILES_DIRECTORIES_ID:
                queryBuilder.setTables(FilesDirectoriesContract.TABLE_NAME);
                long fileDirectoryId = FilesDirectoriesContract.getFileId(uri);
                queryBuilder.appendWhere(FilesDirectoriesContract.Columns._ID + " = " + fileDirectoryId);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, strings, s, strings1, null, null, s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILES_DIRECTORIES:
                return FilesDirectoriesContract.CONTENT_TYPE;

            case FILES_DIRECTORIES_ID:
                return FilesDirectoriesContract.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch(match) {
            case FILES_DIRECTORIES:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(FilesDirectoriesContract.TABLE_NAME, null, contentValues);
                if(recordId >=0) {
                    returnUri = FilesDirectoriesContract.buildFileUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (recordId >= 0) {
            // something was inserted
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
           //"insert: nothing inserted"
        }

       return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case FILES_DIRECTORIES:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(FilesDirectoriesContract.TABLE_NAME, contentValues, s, strings);
                break;

            case FILES_DIRECTORIES_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = FilesDirectoriesContract.getFileId(uri);
                selectionCriteria = FilesDirectoriesContract.Columns._ID + " = " + taskId;

                if((s != null) && (s.length()>0)) {
                    selectionCriteria += " AND (" + s + ")";
                }
                count = db.update(FilesDirectoriesContract.TABLE_NAME, contentValues, selectionCriteria, strings);
                break;


            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if(count > 0) {
            // something was deleted
            Log.d(TAG, "update: Setting notifyChange with " + uri);
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            Log.d(TAG, "update: nothing deleted");
        }

        Log.d(TAG, "Exiting update, returning " + count);
        return count;
    }
}

