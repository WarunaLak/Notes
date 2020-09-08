package com.waruna.notes2.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;
import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.data.repositories.NoteRepository;
import com.waruna.notes2.ui.main.main.adapter.Item;
import com.waruna.notes2.util.exceptions.NoInternetException;
import com.waruna.notes2.util.notelist.ListItemUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<TempNote>> allTempNotes;
    private CompositeDisposable compositeDisposable;
    private User user;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        allTempNotes = repository.getAllTempNotes();
        compositeDisposable = new CompositeDisposable();
    }

    public void fetchNotes() {
        if (user != null && user.isLogin()) {
            compositeDisposable.add(repository.fetchNotes(user.getUserID(), new NoteRepository.RequestListener() {
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

                @Override
                public void onSuccess() {
                }
            }));
        }
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    public void save(Note note) {
        if (user != null && user.isLogin()){
            compositeDisposable.add(repository.saveNote( user.getUserID(), note, new NoteRepository.RequestListener() {
                @Override
                public void onError(Throwable t) {
                    Log.e("save error", t.getMessage());
                }

                @Override
                public void onSuccess() {}
            }));
        } else {
            repository.insertTempNote(note, TempNote.PENDING_TO_UPLOAD);
        }
    }

    public void update(Item item) {

        Note note = item.getNote();

        if (item.getNoteType() == Item.ITEM_NOTE_DEFAULT) {

            if (user != null && user.isLogin()){
                compositeDisposable.add(repository.updateNote( note, new NoteRepository.RequestListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("save error", t.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                    }
                }));
            } else {
                repository.deleteNote(note);
                repository.insertTempNote(note, TempNote.PENDING_TO_UPLOAD);
            }

        } else if (item.getNoteType() == Item.ITEM_NOTE_TEMP){

            TempNote tn = item.getTempNote();

            if (tn != null) {
                repository.deleteTempNote(tn);
                save(note);
            }
        }
    }

    public void delete(Item item) {

        if (item.getNoteType() == Item.ITEM_NOTE_DEFAULT) {

            final Note note = item.getNote();

            if (user != null && user.isLogin()){
                compositeDisposable.add(repository.removeNote( note, new NoteRepository.RequestListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.e("delete error", t.getMessage());
                    }

                    @Override
                    public void onSuccess() {}
                }));
            } else {
                repository.deleteNote(note);
                repository.insertTempNote(note, TempNote.PENDING_TO_REMOVE);
            }
        } else if (item.getNoteType() == Item.ITEM_NOTE_TEMP){
            repository.deleteTempNote(item.getTempNote());
        }

    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<TempNote>> getAllTempNotes() {
        return allTempNotes;
    }

    public LiveData<User> getUser(){
        return repository.getUser();
    }

    public void setUser(User user){
        this.user = user;
    }

}
