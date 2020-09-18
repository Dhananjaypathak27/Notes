package com.xparticle.notes.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xparticle.notes.R;
import com.xparticle.notes.models.Note;
import com.xparticle.notes.util.Utility;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "NotesRecyclerAdapter";
    private ArrayList<Note> mNotes = new ArrayList<>();
    private onNoteListener mOnNoteListener;

    public NotesRecyclerAdapter(ArrayList<Note> mNotes,onNoteListener mOnNoteListener) {
        this.mNotes = mNotes;
        this.mOnNoteListener = mOnNoteListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle,mTime;
        onNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView,onNoteListener onNoteListener) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTime = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item,parent,false);
        return new MyViewHolder(v,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        
        try {
            String month = mNotes.get(position).getTimestamp().substring(0,2);
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.mTitle.setText(mNotes.get(position).getTitle());
            holder.mTime.setText(timestamp);
        }
        catch (Exception e){
            Log.d(TAG, "onBindViewHolder: nullPointerException "+ e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }
}
