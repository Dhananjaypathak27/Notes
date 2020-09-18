package com.xparticle.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xparticle.notes.models.Note;
import com.xparticle.notes.persistence.NoteRepository;
import com.xparticle.notes.util.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher
{


    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLE = 1;
    private static final int EDIT_MODE_DISABLE = 0;

    //ui
    LineEditText mLineEditText;
    EditText mEditTitle;
    TextView mViewTitle;
    ImageButton mBackArrow,mCheck;

    //var
    private boolean mIsNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mFinalNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mLineEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mBackArrow = findViewById(R.id.check_container);
        mCheck = findViewById(R.id.toolbar_check);
        mNoteRepository = new NoteRepository(this);

        if(getIncomingIntent()){
            //open in (edit mode)
            setNewNoteProperties();
            enableEditMode();
        }
        else {
            //open in (view mode)
            setNoteProperties();
            disableContentInteraction();
        }
        setListeners();
    }

    private void setListeners(){
        mLineEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this,this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note")){
            mInitialNote = getIntent().getParcelableExtra("selected_note");
            mFinalNote = getIntent().getParcelableExtra("selected_note");


            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            mFinalNote.setId(mInitialNote.getId());

            mMode = EDIT_MODE_DISABLE;
            mIsNewNote=false;
            return false;
        }
        mMode = EDIT_MODE_ENABLE;
        mIsNewNote=true;
        return true;
    }

    private void saveChange(){
        if(mIsNewNote){
            saveNewNote();
        }
        else{
            updateNote();
        }
    }

    private void updateNote(){
        mNoteRepository.updateNote(mFinalNote);
    }
    private void saveNewNote(){
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    private void disableContentInteraction(){
        mLineEditText.setKeyListener(null);
        mLineEditText.setFocusable(false);
        mLineEditText.setFocusableInTouchMode(false);
        mLineEditText.setCursorVisible(false);
        mLineEditText.clearFocus();
    }
    private void enableContentInteraction(){
        mLineEditText.setKeyListener(new EditText(this).getKeyListener());
        mLineEditText.setFocusable(true);
        mLineEditText.setFocusableInTouchMode(true);
        mLineEditText.setCursorVisible(true);
        mLineEditText.requestFocus();
    }


    private void enableEditMode(){
        mBackArrow.setVisibility(View.GONE);
        mCheck.setVisibility(View.VISIBLE);

        mEditTitle.setVisibility(View.VISIBLE);
        mViewTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_ENABLE;
        enableContentInteraction();

    }

    private void disableEditMode(){
        mBackArrow.setVisibility(View.VISIBLE);
        mCheck.setVisibility(View.GONE);

        mEditTitle.setVisibility(View.GONE);
        mViewTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_DISABLE;
        disableContentInteraction();

        String temp = mLineEditText.getText().toString();
        temp = temp.replace("\n","");
        temp = temp.replace(" ","");

        if(temp.length() >0){
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLineEditText.getText().toString());
            String timeStamp = Utility.getCurrentTimestamp();
            mFinalNote.setTimestamp(timeStamp);

            if(!mFinalNote.getContent().equals(mInitialNote.getContent())
                || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                    saveChange();
            }
        }

    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void setNoteProperties(){
        mViewTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mLineEditText.setText(mInitialNote.getContent());
    }
    private void setNewNoteProperties(){
        mViewTitle.setText("");
        mEditTitle.setText("");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "onDoubleTap: double tapped!");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.toolbar_check:{
                hideSoftKeyboard();
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
            case R.id.check_container:{
                finish();
                break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLE){
            onClick(mCheck);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLE){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}