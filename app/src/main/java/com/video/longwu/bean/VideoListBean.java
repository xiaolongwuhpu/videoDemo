package com.video.longwu.bean;

import java.io.Serializable;

public class VideoListBean implements Serializable {
    private String tvName;
    private String vurl;

    public VideoListBean(String tvName, String vurl) {
        this.tvName = tvName;
        this.vurl = vurl;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }

    /**
     * mdown : 0
     * wmarkPos : 0
     * publishTime : 1439567432000
     * vpic : http://pic9.iqiyipic.com/image/20150803/96/f9/v_109343020_m_601.jpg
     * tvQipuId : 385274600
     * payMarkUrl :
     * purType : 0
     * shortTitle : 航海王 第1集
     * type : 1
     * lgh : []
     * isProduced : 0
     * vurl : http://www.iqiyi.com/v_19rrok4nt0.html
     * plcdown : {"15":0,"17":0}
     * vid : e59fa071d268247291f7737c72ea37f8
     * timeLength : 1500
     * pd : 1
     * vn : 航海王 第1集
     * payMark : 0
     * exclusive : 1
     * id : 385274600
     * vt : 我是路飞！ 将要成为海贼王的男人
     * pds : 1 //第几集的意思
     */
    private  int playCount;//模拟 播放次数
    private  int commentCount;//模拟 评论个数


    private long publishTime;
    private String vpic;
    private int tvQipuId;
    private String shortTitle;
    private int isProduced;
    private String vid;
    private int timeLength;
    private int pd;
    //    private String vn;
    private int id;
    private String vt;
    private String pds;

    public VideoListBean(String vurl, String vpic, String shortTitle, String vt) {
        this.vurl = vurl;
        this.vpic = vpic;
        this.shortTitle = shortTitle;
        this.vt = vt;
    }

    public VideoListBean(String vurl, int playCount, int commentCount, String vpic, String shortTitle, String vt) {
        this.vurl = vurl;
        this.playCount = playCount;
        this.commentCount = commentCount;
        this.vpic = vpic;
        this.shortTitle = shortTitle;
        this.vt = vt;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getVpic() {
        return vpic;
    }

    public void setVpic(String vpic) {
        this.vpic = vpic;
    }

    public int getTvQipuId() {
        return tvQipuId;
    }

    public void setTvQipuId(int tvQipuId) {
        this.tvQipuId = tvQipuId;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public int getIsProduced() {
        return isProduced;
    }

    public void setIsProduced(int isProduced) {
        this.isProduced = isProduced;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getPds() {
        return pds;
    }

    public void setPds(String pds) {
        this.pds = pds;
    }

    @Override
    public String toString() {
        return "VideoListBean{" +
                "vurl='" + vurl + '\'' +
                ", vpic='" + vpic + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", vt='" + vt + '\'' +
                ", pds='" + pds + '\'' +
                '}';
    }
}
