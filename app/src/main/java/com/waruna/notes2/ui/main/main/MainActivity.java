package com.waruna.notes2.ui.main.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.waruna.notes2.R;
import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;
import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.ui.auth.login.LoginActivity;
import com.waruna.notes2.ui.main.NoteViewModel;
import com.waruna.notes2.ui.main.edit.AddEditNoteActivity;
import com.waruna.notes2.ui.main.main.adapter.Item;
import com.waruna.notes2.ui.main.main.adapter.NoteAdapter;
import com.waruna.notes2.util.notelist.ListItemUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        adapter.submitList(items);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // update Recyclerview
                //Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
                //adapter.submitList(notes);
                assert notes != null;
                ListItemUtil.generateFromNote(items, notes);
                adapter.notifyDataSetChanged();
            }
        });

        noteViewModel.getAllTempNotes().observe(this, new Observer<List<TempNote>>() {
            @Override
            public void onChanged(List<TempNote> tempNotes) {
                ListItemUtil.generateFromTempNote(items, tempNotes);
                adapter.notifyDataSetChanged();
            }
        });

        noteViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                noteViewModel.setUser(user);
                noteViewModel.fetchNotes();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModel.delete(adapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {

                String title = "", dec = "";
                int id = 0, priority = 0, type;

                type = item.getNoteType();

                if (item.getNoteType() == Item.ITEM_NOTE_DEFAULT){
                    id = item.getNote().getId();
                    title = item.getNote().getTitle();
                    dec = item.getNote().getDescription();
                    priority = item.getNote().getPriority();
                } else if (item.getNoteType() == Item.ITEM_NOTE_TEMP){
                    id = item.getTempNote().getId();
                    title = item.getTempNote().getNote().getTitle();
                    dec = item.getTempNote().getNote().getDescription();
                    priority = item.getTempNote().getNote().getPriority();
                }

                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, id);
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, title);
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, dec);
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, priority);
                intent.putExtra(AddEditNoteActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.save(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            int type = data.getIntExtra(AddEditNoteActivity.EXTRA_TYPE, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            TempNote tn = ListItemUtil.getTempNote(id, items);
            Item item = new Item(note,tn,type,0);
            noteViewModel.update(item);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sync:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        noteViewModel.onStop();
        super.onStop();
    }
}