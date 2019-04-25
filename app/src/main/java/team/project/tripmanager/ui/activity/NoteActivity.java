package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.callback.CustomCallback;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Note;

public class NoteActivity extends TripBaseActivity {

    private AppCompatEditText notesEt;
    private AppCompatTextView notesTv;
    private Logger logger = new Logger(getClass());
    private Integer noteId;
    private boolean fromExplore;
    private  Menu menu;
    private int FLAG = 0;
    private boolean SAVE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        fromExplore = getIntent().getBooleanExtra("fromExplore", false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        notesEt = findViewById(R.id.notesEdt);
        notesTv = findViewById(R.id.notesTV);
        notesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SAVE = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toolbar.setTitle("Your Notes");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fetchNotesFromServer();
    }

    private void fetchNotesFromServer() {
        environment.getAPIService().getNote(tripId).enqueue(new CustomCallback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getNote() != null) {
                    Note note = response.body().getNote();
                    noteId = note.getNoteId();
                    if(note.getNoteText().length() > 0){
                        setNoteText(note.getNoteText());
                    }
                } else {
                    logger.warn("response.body() != null && response.body().getNote() != null False");
                }
            }
        });
    }

    private String getNoteText() {
        return notesEt.getText().toString();
    }

    private void setNoteText(String noteText) {
        notesTv.setText(noteText);
        notesTv.setTextColor(getResources().getColor(R.color.colorText));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.itemBtn:
                if(FLAG == 0){
                    menu.getItem(0).setIcon(R.drawable.ic_save_white_24dp);
                    notesTv.setVisibility(View.GONE);
                    notesEt.setText(notesTv.getText().toString());
                    notesEt.setVisibility(View.VISIBLE);
                    FLAG = 1;
                } else if(FLAG == 1){
                    updateNote(getNoteText());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote(String noteText) {
        environment.getAPIService().updateNote(tripId, noteId != null ? noteId : 0, noteText).enqueue(new CustomCallback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getMessage() != null) {
                    showToast(response.body().getMessage());
                } else {
                    logger.warn("response.body() != null && response.body().getMessage() != null False");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (!fromExplore) {
            getMenuInflater().inflate(R.menu.menu_note, menu);

        }
        return true;
    }

    private void showAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("NOTES ARE NOT SAVED");
        alertDialogBuilder.setMessage("Are you sure you want to leave without saving?");
        alertDialogBuilder.setPositiveButton("SAVE & EXIT", (arg0, arg1) -> {
            updateNote(getNoteText());
            SAVE = true;
            onBackPressed();
        });

        alertDialogBuilder.setNegativeButton("EXIT", (dialog, which) ->
                {
                    finish();
                    SAVE = true;
                    onBackPressed();
                }
        );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!SAVE){
            showAlert();
        } else {
            super.onBackPressed();
        }
    }
}
