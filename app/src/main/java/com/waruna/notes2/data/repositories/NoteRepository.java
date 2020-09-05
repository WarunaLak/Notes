package com.waruna.notes2.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.DatabaseClient;
import com.waruna.notes2.data.db.NoteDao;
import com.waruna.notes2.data.db.NoteDatabase;
import com.waruna.notes2.data.db.entities.Note;
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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NoteRepository {

    public interface RequestListener {
        void onError(Throwable t);
    }

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private Retrofit retrofit;
    private MyApi myApi;

    public NoteRepository(Application application) {
        NoteDatabase database = DatabaseClient.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNote();
        retrofit = RetrofitClient.getInstance(new NetworkConnectionInterceptor(application));
        myApi = retrofit.create(MyApi.class);
    }

    public Disposable fetchNotes(final RequestListener listener) {

        Disposable disposable = myApi.getNotes()
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
                            public void accept(Throwable throwable) throws Exception {
                                listener.onError(throwable);
                            }
                        }
                );

        return disposable;
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
}
