package com.video.longwu.http.callback;

import okhttp3.Call;
import okhttp3.Response;

public class SimpleCallback extends Callback {

    @Override
    public Object parseNetworkResponse(Response response) throws Exception {
        return null;
    }

    @Override
    public void onError(Call call, Exception e) {

    }

    @Override
    public void onResponse(Object response) {

    }
}
