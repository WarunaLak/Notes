package com.waruna.notes2.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.repositories.NoteRepository;
import com.waruna.notes2.util.NoInternetException;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private CompositeDisposable compositeDisposable;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        compositeDisposable = new CompositeDisposable();
    }

    public void fetchNotes() {
        compositeDisposable.add(repository.fetchNotes(new NoteRepository.RequestListener() {
            @Override
            public void onError(Throwable t) {
                if (t instanceof HttpException) {
                    Log.e("Error h", t.getMessage());
                } else if (t instanceof NoInternetException) {
                    Log.e("Error n", t.getMessage());
                } else if (t instanceof Exception) {
                    Log.e("Error e", t.getMessage());
                }
            }
        }));
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}
