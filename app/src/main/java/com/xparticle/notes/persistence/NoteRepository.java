package com.xparticle.notes.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.xparticle.notes.async.DeleteAsyncTask;
import com.xparticle.notes.async.InsertAsyncTask;
import com.xparticle.notes.async.UpdateAsyncTask;
import com.xparticle.notes.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);

    }
    public  void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }


    public LiveData<List<Note>> retrieveNoteTask(){

        return mNoteDatabase.getNoteDao().getNotes();
    }
    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
}
