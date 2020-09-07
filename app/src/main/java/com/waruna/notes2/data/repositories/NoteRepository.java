package com.waruna.notes2.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.util.StringUtil;

import com.waruna.notes2.data.db.DatabaseClient;
import com.waruna.notes2.data.db.NoteDao;
import com.waruna.notes2.data.db.NoteDatabase;
import com.waruna.notes2.data.db.UserDao;
import com.waruna.notes2.data.db.entities.Note;
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
    private LiveData<List<Note>> allNotes;
    private MyApi api;

    public NoteRepository(Application application) {
        NoteDatabase database = DatabaseClient.getInstance(application);
        noteDao = database.noteDao();
        userDao = database.userDao();
        allNotes = noteDao.getAllNote();
        api = RetrofitClient.getInstance(new NetworkConnectionInterceptor(application));
    }

    public Disposable fetchNotes(int userID, final RequestListener listener) {

        return api.getNotes(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                List<Note> notes = response.body().getNotes();
                                insertAll(notes);
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
        n.setIsSync(0);

        return api.saveNote(userID,n.getTitle(), n.getDescription(), n.getPriority())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<NotesResponse>>() {

                            @Override
                            protected void onSuccess(Response<NotesResponse> response) {
                                n.setIsSync(1);
                                insert(n);
                            }

                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                listener.onError(throwable);
                                insert(n);
                            }
                        }
                );
    }

    public void insert(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                noteDao.insert(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void insertAll(final List<Note> noteList) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                noteDao.insertAll(noteList);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void update(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                noteDao.update(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void delete(final Note note) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                noteDao.delete(note);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public void remove(final int id) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                noteDao.removeNote(id);
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
            public Boolean call() throws Exception {
                noteDao.deleteAllNotes();
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

    public LiveData<User> getUser(){
        return userDao.getUser();
    }
}
