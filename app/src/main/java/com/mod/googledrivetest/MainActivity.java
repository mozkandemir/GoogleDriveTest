package com.mod.googledrivetest;


import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnFileClickListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onEditClick(FilesDirectories fd, Cursor curs) {

    }

    @Override
    public void onFileLongClick(FilesDirectories fd,Cursor curs) {

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "onConfigurationChanged: LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(TAG, "onConfigurationChanged: PORTRAIT");
        }
    }
}