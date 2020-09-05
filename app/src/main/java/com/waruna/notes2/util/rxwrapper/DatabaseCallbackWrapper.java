package com.waruna.notes2.util.rxwrapper;

import android.util.Log;

import io.reactivex.observers.DisposableObserver;

public class DatabaseCallbackWrapper<T> extends DisposableObserver<T> {

    //protected abstract void onDBError(Throwable e);

    @Override
    public void onNext(T t) {
        //
    }

    @Override
    public void onComplete() {
        //
    }

    @Override
    public void onError(Throwable e) {
        //onDBError(e);
        Log.e("Database operation ", e.getMessage());
    }
}
