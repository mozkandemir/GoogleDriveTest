package com.mod.googledrivetest;

import android.database.Cursor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


class CursorRecyclerViewAdapter extends  RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor mCursor;
    private OnFileClickListener mListener;
    private String mName;


     /** todo Check if more is needed
     * */
     interface OnFileClickListener {
        void onEditClick ( FilesDirectories fd, Cursor curs);
        void onFileLongClick(FilesDirectories fd, Cursor curs);
    }

    public OnFileClickListener getListener() {
        return mListener;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public CursorRecyclerViewAdapter(Cursor cursor, OnFileClickListener listener, String name) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
        mListener = listener;
        mName = name;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: starts");

        if((mCursor == null) || (mCursor.getCount() == 0)) {
            Log.d(TAG, "onBindViewHolder: providing instructions");
            holder.name.setText("No Directory or File");

        } else  {
            if(!mCursor.moveToPosition(position)) {
                throw new IllegalStateException("Couldn't move cursor to position " + position);
            }

            final FilesDirectories fd = new FilesDirectories(mCursor.getLong(mCursor.getColumnIndex(FilesDirectoriesContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex(FilesDirectoriesContract.Columns.NAME)),
                    mCursor.getString(mCursor.getColumnIndex(FilesDirectoriesContract.Columns.A_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(FilesDirectoriesContract.Columns.E_NAME)));
            //Log.d(TAG, "onBindViewHolder: Write Names"+ fd.getName());


            if(mCursor.getString(mCursor.getColumnIndex(FilesDirectoriesContract.Columns.A_NAME)).equalsIgnoreCase(mName)){

                //according to its extension set the button icons
                holder.name.setText(mCursor.getString(mCursor.getColumnIndex(FilesDirectoriesContract.Columns.NAME)));
                if(fd.getExtension().equalsIgnoreCase("jpg")|| fd.getExtension().equalsIgnoreCase("jpeg")){
                    holder.but.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_perm_media_black_48dp, 0, 0, 0);
                }
                else if(fd.getExtension().equalsIgnoreCase("mp4")){
                    holder.but.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_theaters_black_48dp, 0, 0, 0);
                }
                else{
                    holder.but.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_source_black_48dp, 0, 0, 0);
                }
            }
          else{
                holder.name.setVisibility(View.GONE);
                holder.but.setVisibility(View.GONE);

            }

            //TODO IMPORTANT for BUTTON ETC
            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.d(TAG, "onClick: starts");
                    //TODO CHECK for button
                    switch(view.getId()) {

                        case R.id.button2:

                            if(mListener != null) {
                                mListener.onEditClick(fd,mCursor);
                            }
                            break;

                        default:
                            Log.d(TAG, "onClick: found unexpected id");
                    }

                }
            };

            View.OnLongClickListener buttonLongListener = new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    Log.d(TAG, "onLongClick starts");
                    if(mListener != null) {
                        mListener.onFileLongClick(fd,mCursor);
                        /*holder.name.setClickable(true);
                        holder.name.setFocusable(true);
                        holder.name.setEnabled(true);
                        holder.name.setFocusableInTouchMode(true);*/
                        Log.d(TAG, "onLongClick: which one is clicked"+fd.getName());


                        return true;
                    }
                    return false;
                }
            };

            holder.but.setOnClickListener(buttonListener);
           //
            holder.itemView.setOnLongClickListener(buttonLongListener);
        }

    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: starts");
        if((mCursor == null) || (mCursor.getCount() == 0)) {
            return 1; // we populate a single ViewHolder with instructions
        } else {
            return mCursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old Cursor is <em>not</em> closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns the previously set Cursor, or null if there wasn't one.
     * If the given new Cursor is the same instance as the previously set
     * Cursor, null is also returned.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        int numItems = getItemCount();

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, numItems);
        }
        return oldCursor;

    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
//        private static final String TAG = "TaskViewHolder";
        Button but;
        TextView name;
        View itemView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            this.but=itemView.findViewById(R.id.button2);
            this.name = itemView.findViewById(R.id.testText);
            this.itemView = itemView;
        }
    }
}
