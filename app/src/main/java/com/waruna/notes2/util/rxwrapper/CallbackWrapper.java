package com.waruna.notes2.util.rxwrapper;

import com.waruna.notes2.util.exceptions.ApiException;

import io.reactivex.functions.Consumer;
import retrofit2.Response;

public abstract class CallbackWrapper<T extends Response> implements Consumer<T> {

    protected abstract void onSuccess(T t);

    @Override
    public void accept(T t) throws Exception {

        if (t.isSuccessful()){
            onSuccess(t);
        } else {
            throw new ApiException(t.errorBody().string()+" : "+t.code());
        }
    }

}
