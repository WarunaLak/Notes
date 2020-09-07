package com.waruna.notes2.ui.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.waruna.notes2.data.db.entities.User;
import com.waruna.notes2.data.network.responses.AuthResponse;
import com.waruna.notes2.data.repositories.AuthRepository;
import com.waruna.notes2.util.exceptions.ApiException;
import com.waruna.notes2.util.exceptions.NoInternetException;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository repository;
    private CompositeDisposable compositeDisposable;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        compositeDisposable = new CompositeDisposable();
    }

    public void login(final String email, String pass){
        compositeDisposable.add(repository.login(email, pass ,new AuthRepository.RequestListener() {
            @Override
            public void onError(Throwable t) {
                if (t instanceof HttpException) {
                    Log.e("Error h", t.getMessage());
                } else if (t instanceof ApiException) {
                    Log.e("Error a", t.getMessage());
                } else if (t instanceof NoInternetException) {
                    Log.e("Error n", t.getMessage());
                } else if (t instanceof Exception) {
                    Log.e("Error e", t.getMessage());
                }
            }

            @Override
            public void onSuccess(Response<AuthResponse> response) {
                if (response.body().isLogin()) {
                    User user = new User(response.body().getUserID(), email, response.body().isLogin());
                    repository.saveUser(user);
                } else {
                    Log.e("login :", "Fail , "+response.message());
                }
            }

        }));
    }

    public void register(final String email, String pass, String cPass){

        if (email.isEmpty()){
            // auth listener
            return;
        }

        if (pass.isEmpty()){
            // auth listener
            return;
        }

        if (!pass.equals(cPass)){
            // auth listener
            return;
        }

        compositeDisposable.add(repository.register(email, pass ,new AuthRepository.RequestListener() {
            @Override
            public void onError(Throwable t) {
                if (t instanceof HttpException) {
                    Log.e("Error h", t.getMessage());
                } else if (t instanceof ApiException) {
                    Log.e("Error a", t.getMessage());
                } else if (t instanceof NoInternetException) {
                    Log.e("Error n", t.getMessage());
                } else if (t instanceof Exception) {
                    Log.e("Error e", t.getMessage());
                }
            }

            @Override
            public void onSuccess(Response<AuthResponse> response) {
                User user = new User( response.body().getUserID(),email, response.body().isRegistered());
                repository.saveUser(user);
            }

        }));
    }

    public LiveData<User> getLoggedInUser(){
        return repository.getUser();
    }

    public void onStop() {
        compositeDisposable.clear();
    }
}
