package com.waruna.notes2.data.network;

import io.reactivex.functions.Consumer;
import retrofit2.Response;

public abstract class CallbackWrapper<T extends Response> implements Consumer<T> {

    protected abstract void onSuccess(T t);

    @Override
    public void accept(T t) throws Exception {

        if (t.isSuccessful()){
            onSuccess(t);
            throw new Exception("Test e 2");
        } else {
            throw new Exception("Test e");
        }
    }

}
