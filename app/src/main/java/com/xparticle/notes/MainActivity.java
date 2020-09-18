package com.xparticle.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xparticle.notes.adapters.NotesRecyclerAdapter;
import com.xparticle.notes.models.Note;
import com.xparticle.notes.persistence.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.onNoteListener, View.OnClickListener {
    //ui
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    FloatingActionButton mFab;

    //variable
    ArrayList<Note> mNotes =new  ArrayList<> ();
    NotesRecyclerAdapter mNotesRecyclerAdapter;
    private NoteRepository mNoteRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: created");
        mRecyclerView = findViewById(R.id.recyclerView);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mNoteRepository = new NoteRepository(this);

        initRecyclerView();
//        insetFakeData();
        retrieveNotes();

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes");
        
    }
    private void retrieveNotes(){
        mNoteRepository.retrieveNoteTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size()>0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
//
//    public void insetFakeData(){
//        for(int i =0;i<1000;i++){
//            Note note= new Note();
//            note.setTitle("title"+i);
//            note.setContent("content"+i);
//            note.setTimestamp("2020 Aug");
//            mNotes.add(note);
//        }
//        mNotesRecyclerAdapter.notifyDataSetChanged();
//    }
    public void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(mRecyclerView);
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes,this);
        mRecyclerView.setAdapter(mNotesRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onNoteClick(int position) {
//        Log.d(TAG, "onNoteClick:"+position);

        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent= new Intent(this,NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        mNotesRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}