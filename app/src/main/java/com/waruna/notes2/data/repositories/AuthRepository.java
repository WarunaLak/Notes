package com.waruna.notes2.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.DatabaseClient;
import com.waruna.notes2.data.db.NoteDatabase;
import com.waruna.notes2.data.db.dao.UserDao;
import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.data.network.MyApi;
import com.waruna.notes2.data.network.NetworkConnectionInterceptor;
import com.waruna.notes2.data.network.RetrofitClient;
import com.waruna.notes2.data.network.responses.AuthResponse;
import com.waruna.notes2.util.rxwrapper.CallbackWrapper;
import com.waruna.notes2.util.rxwrapper.DatabaseCallbackWrapper;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AuthRepository {

    public interface RequestListener {
        void onError(Throwable t);
        void onSuccess(Response<AuthResponse> response);
    }

    private UserDao userDao;
    private MyApi api;

    public AuthRepository(Application application) {
        NoteDatabase database = DatabaseClient.getInstance(application);
        userDao = database.userDao();
        api = RetrofitClient.getInstance(new NetworkConnectionInterceptor(application));
    }

    public Disposable login (String email, String pass, final RequestListener listener) {
        return api.login(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<AuthResponse>>() {

                            @Override
                            protected void onSuccess(Response<AuthResponse> response) {
                                listener.onSuccess(response);
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

    public Disposable register (String email, String pass, final RequestListener listener) {

        return api.register(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CallbackWrapper<Response<AuthResponse>>() {

                            @Override
                            protected void onSuccess(Response<AuthResponse> response) {
                                listener.onSuccess(response);
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

    public void saveUser(final User user) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                userDao.insert(user);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseCallbackWrapper<Boolean>());
    }

    public LiveData<User> getUser() {
        return userDao.getUser();
    }
}
