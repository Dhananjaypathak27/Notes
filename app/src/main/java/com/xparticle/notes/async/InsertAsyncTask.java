package com.xparticle.notes.async;

import android.os.AsyncTask;

import com.xparticle.notes.models.Note;
import com.xparticle.notes.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note,Void,Void> {

    private NoteDao mNoteDao;
    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNotes(notes);
        return null;
    }
}
