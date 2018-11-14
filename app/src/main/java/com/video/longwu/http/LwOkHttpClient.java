package com.video.longwu.http;


import com.video.longwu.http.builder.GetBuilder;
import com.video.longwu.http.builder.PostFileBuilder;
import com.video.longwu.http.builder.PostFormBuilder;
import com.video.longwu.http.request.RequestCall;
import com.video.longwu.http.callback.Callback;
import com.video.longwu.http.callback.SimpleCallback;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Response;


public class LwOkHttpClient {
    public static final long DEFAULT_MILLISECONDS = 20000L;
    private volatile static LwOkHttpClient mInstance;
    private okhttp3.OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public LwOkHttpClient(okhttp3.OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new okhttp3.OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static LwOkHttpClient initClient(okhttp3.OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (LwOkHttpClient.class) {
                if (mInstance == null) {
                    mInstance = new LwOkHttpClient(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static LwOkHttpClient getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public okhttp3.OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

//    http://media8.smartstudy.com/avatar/ipqbocvm_ji7joYVq.png

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post()
    {
        return new PostFormBuilder();
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = new SimpleCallback();
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (call.isCanceled()) {
                    sendFailResultCallback(call, new IOException("Canceled!"), finalCallback);
                    return;
                }

                if (!finalCallback.validateReponse(response)) {
                    sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback);
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                }
            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

