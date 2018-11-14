package com.video.longwu.http;

public interface BaseNetWorkerService {
    //短缓存有效期为10分钟
    int CACHE_STALE_SHORT = 10 * 60;
    //长缓存有效期为1小时
    int CACHE_STALE_LONG = 60 * 60;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    String CACHE_CONTROL_NETWORK = "0";
    //查询缓存的Cache-Control设置
    String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
}
