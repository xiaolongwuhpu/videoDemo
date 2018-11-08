package com.video.longwu.bean;

import java.io.Serializable;

public class TVListBean implements Serializable {
    private String tvName;
    private String tvUrl;

    public TVListBean(String tvName, String tvUrl) {
        this.tvName = tvName;
        this.tvUrl = tvUrl;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getTvUrl() {
        return tvUrl;
    }

    public void setTvUrl(String tvUrl) {
        this.tvUrl = tvUrl;
    }
}
