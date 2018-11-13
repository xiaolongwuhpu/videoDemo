package com.video.longwu.http;



import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
//    @FormUrlEncoded   //post请求必须加上
    @GET("/")
    Observable<ResponseBody> getMovie(@Query("callback") String type);

    @GET("/")
    Observable<ResponseBody> getMovie();
}
