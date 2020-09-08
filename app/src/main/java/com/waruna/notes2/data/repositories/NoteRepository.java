package com.waruna.notes2.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.DatabaseClient;
import com.waruna.notes2.data.db.dao.NoteDao;
import com.waruna.notes2.data.db.NoteDatabase;
import com.waruna.notes2.data.db.dao.TempNoteDao;
import com.waruna.notes2.data.db.dao.UserDao;
import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;
import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.util.rxwrapper.CallbackWrapper;
import com.waruna.notes2.data.network.MyApi;
import com.waruna.notes2.data.network.NetworkConnectionInterceptor;
import com.waruna.notes2.data.network.RetrofitClient;
import com.waruna.notes2.data.network.responses.NotesResponse;
import com.waruna.notes2.util.rxwrapper.DatabaseCallbackWrapper;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class NoteRepository {

    public interface RequestListener {
        void onError(Throwable t);
        void onSuccess();
    }

    private NoteDao noteDao;
    private UserDao userDao;
    private TempNoteDao tempDao;
    private LiveData<List<Note>> allNotes;
    private MyApi api;

    public NoteRepository(Application application) {
        NoteDatabase database = DatabaseClient.getInstance(application);
        noteDao = database.noteDao();
        userDao = database.userDao();
        tempDao = database.tempNoteDao();
        allNotes = noteDao.getAllNote();
        api = RetrofitClient.getInstance(new NetworkConnectionInterceptor(application));
    }

    // network
    public Disposable fetchNotes(int userID, final RequestListener listener) {

        return api.getNotes(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                List<Note> notes = response.body().getNotes();
                                insertAllNotes(notes);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                listener.onError(throwable);
                            }
                        }
                );
    }

    public Disposable saveNote(int userID, Note note, final RequestListener listener){

        final Note n = note;

        return api.saveNote(userID,n.getTitle(), n.getDescription(), n.getPriority())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                n.setId(response.body().getNoteID());
                                insertNote(n);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                insertTempNote(n, TempNote.PENDING_TO_UPLOAD);
                            }
                        }
                );
    }

    public Disposable removeNote(Note note, final RequestListener listener){

        final Note n = note;

        return api.removeNote(n.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                deleteNote(n);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                insertTempNote(n, TempNote.PENDING_TO_REMOVE);
                            }
                        }
                );
    }

    public Disposable updateNote(Note note, final RequestListener listener){

        final Note n = note;

        return api.updateNote(n.getId(),n.getTitle(), n.getDescription(), n.getPriority())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                updateNote(n);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                deleteNote(n);
                                insertTempNote(n, TempNote.PENDING_TO_UPLOAD);
                            }
                        }
                );
    }

    // database
    public void insertNote(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                noteDao.insert(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void insertTempNote(final Note note, final String type) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                tempDao.insert(new TempNote(type,note));
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void insertAllNotes(final List<Note> noteList) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                noteDao.insertAll(noteList);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void updateNote(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                noteDao.update(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void deleteNote(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                noteDao.delete(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void deleteTempNote(final TempNote note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                tempDao.delete(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void deleteAllNotes() {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                noteDao.deleteAllNotes();
                tempDao.deleteAllNotes();
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<TempNote>> getAllTempNotes() {
        return tempDao.getAllNote();
    }

    public LiveData<User> getUser(){
        return userDao.getUser();
    }
}
