package com.video.longwu.http.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T> {

    public void onBefore(Request request) {

    }

    public void onAfter(){

    }

    public void progress(float progress){

    }

    public boolean validateReponse(Response response) {
        return response.isSuccessful();
    }

    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onError(Call call, Exception e);

    public abstract void onResponse(T response);

}
