package com.video.longwu.activity.videolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.video.longwu.R;
import com.video.longwu.adapter.VideoListAdapter;
import com.video.longwu.bean.KaiYanResBean;
import com.video.longwu.bean.VideoListBean;
import com.video.longwu.http.MovieService;
import com.video.longwu.util.IMediaPlayer;
import com.video.longwu.util.SimpleOkhttpClientUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class VideoVerticalListActivity extends AppCompatActivity {
    private String TAG = "VideoVerticalListActivity";
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_vertical_list);
        ButterKnife.bind(this);
//        for (int i = 0; i < 20; i++) {
//            tvlistBean.add(new VideoListBean("我是" + i, VideoUrl.url));
//        }
//        initOkHttpClient();
        iMediaPlayer = new IMediaPlayer();
        getMovie();
    }

    VideoListAdapter adapter;
    private List<VideoListBean> tvlistBean = new ArrayList<>();

    private void initRecycle() {
        adapter = new VideoListAdapter(R.layout.item_video_player, tvlistBean);
        //初始化RecyclerView
        lm = new LinearLayoutManager(this);
        recycleview.setLayoutManager(lm);
        recycleview.addItemDecoration(new DividerDecoration(Color.GRAY, 1));
        recycleview.setAdapter(adapter);
        recycleview.addOnScrollListener(onScrollListener);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMovie();
            }
        });

//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                ImageView imageView = (ImageView) adapter.getViewByPosition(recycleview, position, R.id.iv_play);
//                MediaHelper.play(0);
//                ToastUtil.showLong("OnItemClick: " + position);
//            }
//        });

    }

    IMediaPlayer iMediaPlayer;
    private LinearLayoutManager lm;
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        //进行滑动
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //获取屏幕上显示的第一个条目和最后一个条目的下标
            int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
            //获取播放条目的下标
            int currentPosition = adapter.currentPosition;
            if ((firstVisibleItemPosition > currentPosition || lastVisibleItemPosition < currentPosition) && currentPosition > -1) {
                //让播放隐藏的条目停止
                iMediaPlayer.releaseMP();
                adapter.currentPosition = -1;
                adapter.notifyDataSetChanged();
            }
        }
    };

private String nextPageUrl = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";
    //进行网络请求
    private void getMovie() {
//        String baseUrl = "http://cache.video.iqiyi.com/jp/avlist/202861101/1/?callback=jsonp9";
        String baseUrl = nextPageUrl+"/";
        MovieService movieService = (MovieService) SimpleOkhttpClientUtil.getInstant().getApi(baseUrl,MovieService.class);
//      movieService.getMovie("jsonp9")
        movieService.getMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            String responseString = responseBody.string();

                            KaiYanResBean resBean = new Gson().fromJson(responseString, KaiYanResBean.class);
                            List<KaiYanResBean.ItemListBean> list = resBean.getItemList();
                            if (list == null || list.size() == 0) {
                                adapter.loadMoreEnd(true);
                                return;
                            }
                            nextPageUrl = resBean.getNextPageUrl();
                            for (KaiYanResBean.ItemListBean bean : list) {
                                if (bean.getData() == null || TextUtils.isEmpty(bean.getData().getPlayUrl()))
                                    continue;
                                int playCount = (int) (Math.random() * 1000);
                                int commentCount = (int) (Math.random() * 1000);
                                tvlistBean.add(new VideoListBean(bean.getData().getPlayUrl(), playCount, commentCount, bean.getData().getCover().getFeed(), bean.getData().getTitle(), bean.getData().getDescription()));
                            }

//                            AiQiYiHzwResBean resBean = new Gson().fromJson(VideoUrl.responseString, AiQiYiHzwResBean.class);
//                            List<AiQiYiHzwResBean.DataBean.VlistBean> list = resBean.getData().getVlist();
//                            if (list == null || list.size()==0) {
//                                adapter.loadMoreEnd(true);
//                                return;
//                            }
//                            for (int i = 0; i < list.size(); i++) {
//                                int playCount = (int) (Math.random() * 1000);
//                                int commentCount = (int) (Math.random() * 1000);
////                                tvlistBean.add(new VideoListBean(list.get(i).getVurl(),num1,num2,list.get(i).getVpic(), list.get(i).getShortTitle(),list.get(i).getVt()));
//                                tvlistBean.add(new VideoListBean(VideoUrl.url,playCount,commentCount,list.get(i).getVpic(), list.get(i).getShortTitle(),list.get(i).getVt()));
//                            }
                            initRecycle();
                            Log.i("responseString", tvlistBean.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("tag", e.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
