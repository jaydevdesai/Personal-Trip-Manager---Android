package team.project.tripmanager.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.callback.CustomCallback;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Note;

public class NoteActivity extends TripBaseActivity {

    private EditText notesEt;
    private Logger logger = new Logger(getClass());
    private Integer noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        notesEt = findViewById(R.id.notesEdt);
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
                    setNoteText(note.getNoteText());
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
        notesEt.setText(noteText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.saveBtn:
                updateNote(getNoteText());
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
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }
}
