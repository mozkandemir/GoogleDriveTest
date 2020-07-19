package com.mod.googledrivetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class AppDataBase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";
    public static final String DATABASE_NAME = "Files.db";
    public static final int DATABASE_VERSION = 1;

    // Implement AppDatabase as a Singleton
    private static AppDataBase instance = null;

    private AppDataBase(Context context){ //constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    /**
     *
     * Get an instance of the app's singleton database helper object
     *
     * @param context the content providers context.
     * @return a SQLite database helper object
     */
    static AppDataBase getInstance(Context context) {
        if(instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDataBase(context);
        }

        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: starts");
        String sql;
        sql="CREATE TABLE " + FilesDirectoriesContract.TABLE_NAME + " ("
                + FilesDirectoriesContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + FilesDirectoriesContract.Columns.NAME + " TEXT NOT NULL, "
                + FilesDirectoriesContract.Columns.A_NAME + " TEXT ,"
                + FilesDirectoriesContract.Columns.E_NAME + " TEXT );";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('ROOT','NULL','folder')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('Folders','ROOT','folder')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('Videos','Folders','folder')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('Pictures','Folders','folder')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('koala','Pictures', 'jpg')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('bird','Pictures', 'jpeg')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('butterfly','Pictures', 'jpg')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('earth','Videos', 'mp4')";
        sqLiteDatabase.execSQL(sql);
        sql= "INSERT INTO " +  FilesDirectoriesContract.TABLE_NAME + "( "+ FilesDirectoriesContract.Columns.NAME + ", "
                + FilesDirectoriesContract.Columns.A_NAME+","+FilesDirectoriesContract.Columns.E_NAME +") VALUES ('grb','Videos', 'mp4')";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
