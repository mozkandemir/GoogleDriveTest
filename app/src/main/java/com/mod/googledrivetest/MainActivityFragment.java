package com.mod.googledrivetest;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.InputStream;
import java.security.InvalidParameterException;

import static android.content.ContentValues.TAG;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.OnFileClickListener {

    public static final int LOADER_ID = 0;
    private CursorRecyclerViewAdapter mAdapter;
    private String s;
    View view;
    private TextView textView;
    private VideoView videoview;
    private ImageView imageView;
    private AppDataBase appDataBase;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (!(activity instanceof CursorRecyclerViewAdapter.OnFileClickListener)) {
                       throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement CursorRecyclerViewAdapter.OnTaskClickListener interface");
        }
        Log.d(TAG, "onActivityCreated: "+ mAdapter.getItemCount());

        getLoaderManager().initLoader(LOADER_ID, null,  this);
    }


    public MainActivityFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //textView=getActivity().findViewById(R.id.testText);
         OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                try {
                    if(mAdapter!=null){
                        recyclerView.setAdapter(mAdapter);
                        imageView.setVisibility(View.GONE);
                        videoview.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }};
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_main_fragment, container, false);
        recyclerView = view.findViewById(R.id.rView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final FilesDirectories filesDirectories;
        s="null";
        if (mAdapter == null) {
            mAdapter = new CursorRecyclerViewAdapter(null, this,s);
        }

       /* ContentResolver contentResolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(FilesDirectoriesContract.Columns.NAME, "ROOT");
        values.put(FilesDirectoriesContract.Columns.A_NAME, "NULL");
        contentResolver.insert(FilesDirectoriesContract.CONTENT_URI, values);
        values.put(FilesDirectoriesContract.Columns.NAME, "FOLDER");
        values.put(FilesDirectoriesContract.Columns.A_NAME, "ROOT");
        contentResolver.insert(FilesDirectoriesContract.CONTENT_URI, values);
        values.put(FilesDirectoriesContract.Columns.NAME, "FILE");
        values.put(FilesDirectoriesContract.Columns.A_NAME, "FOLDER");
        contentResolver.insert(FilesDirectoriesContract.CONTENT_URI, values);
*/
        recyclerView.setAdapter(mAdapter);
        return view;
    }



    @Override
    public void onEditClick(FilesDirectories fd, Cursor mCursor) {

        s=fd.getName();
        String extension=fd.getExtension();
        Log.d(TAG, " handleOnBackPressed onEditClick Name: "+ fd.getName());
        videoview = view.findViewById(R.id.videoView);
        imageView= view.findViewById(R.id.imageView);

        //according to its extension show in appropriate view if extension is null or folder it will not trigger videoView or imageView
        if(extension.equalsIgnoreCase("mp4"))
        {
            int videoResourceId = this.getResources().getIdentifier(fd.getName(), "raw", getActivity().getPackageName());
            videoview.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+videoResourceId);
            videoview.setVideoURI(uri);
            videoview.start();
        }
        else if(extension.equalsIgnoreCase("jpg")|| extension.equalsIgnoreCase("jpeg")){
            int pictureResourceId = this.getResources().getIdentifier(fd.getName(), "raw", getActivity().getPackageName());
            imageView.setVisibility(View.VISIBLE);
            InputStream imageStream = this.getResources().openRawResource(pictureResourceId);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(bitmap);

        }
            recyclerView.setAdapter(new CursorRecyclerViewAdapter(mCursor,this,fd.getName()));

    }

    @Override
    public void onFileLongClick(FilesDirectories fd, Cursor curs) {

  /* onFileLongClick is thought for renaming of Folder/Files
        appDataBase = AppDataBase.getInstance(getContext());
        SQLiteDatabase db = appDataBase.getWritableDatabase();
        String getName= "'" + fd.getName() + "'";
        String where=FilesDirectoriesContract.Columns.NAME + " = " +getName +" AND "+ FilesDirectoriesContract.Columns._ID + " = "+ (int)fd.getId() ;
        ContentValues values= new ContentValues();
        values.put(FilesDirectoriesContract.Columns.NAME,"SAMPLE");

        values.put(FilesDirectoriesContract.Columns.D_NAME, "mp4");
        db.update(FilesDirectoriesContract.TABLE_NAME,values,where,null);

        getName= "'" + fd.getName() + "'";
        where=FilesDirectoriesContract.Columns.A_NAME + " = " +getName ;
        values.put(FilesDirectoriesContract.Columns.A_NAME,"TRY");
        db.update(FilesDirectoriesContract.TABLE_NAME,values,where,null);
        Toast.makeText(getActivity(),"Message is "+ fd.getName(), Toast.LENGTH_LONG).show();

        mAdapter.swapCursor(curs); */
        Toast.makeText(getActivity(), fd.getName(), Toast.LENGTH_LONG).show();
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {FilesDirectoriesContract.Columns._ID, FilesDirectoriesContract.Columns.NAME,
                FilesDirectoriesContract.Columns.A_NAME, FilesDirectoriesContract.Columns.E_NAME};
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getActivity(),
                        FilesDirectoriesContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new InvalidParameterException( ".onCreateLoader called with invalid loader id" + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

        int count = mAdapter.getItemCount();
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}