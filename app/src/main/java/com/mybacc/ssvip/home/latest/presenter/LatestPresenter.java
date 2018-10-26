package com.mybacc.ssvip.home.latest.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mybacc.ssvip.BasePresenter;
import com.mybacc.ssvip.data.HttpResultFunc;
import com.mybacc.ssvip.data.Repository;
import com.mybacc.ssvip.data.RxBus;
import com.mybacc.ssvip.data.RxHelp;
import com.mybacc.ssvip.home.latest.model.Banner;
import com.mybacc.ssvip.home.latest.model.PostSection;
import com.mybacc.ssvip.home.latest.model.PostTab;
import com.mybacc.ssvip.home.latest.view.ILatestView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by hpb on 16/6/8.
 */
public class LatestPresenter extends BasePresenter<ILatestView>  {


    public void loadBanner() {
        Observable<List<Banner>> observable = Repository.getInstance()
                .getVmovierApi()
                .getBanner()
                .map(new HttpResultFunc<List<Banner>>());
        new RxHelp<List<Banner>>().toSubscribe(observable, new RxHelp.OnNext<List<Banner>>() {
            @Override
            public void onNext(List<Banner> banners) {
                mView.onGetBanner(banners);
            }
        });
    }

    String day = "";
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");
    String today = dateFormat.format(new Date());

    public void loadTabPost(final int page) {
        Observable<List<PostTab>> observable = Repository.getInstance()
                .getVmovierApi()
                .getTabPost(page)
                .map(new HttpResultFunc<List<PostTab>>());
        new RxHelp<List<PostTab>>().toSubscribe(observable, new RxHelp.OnNext<List<PostTab>>() {
            @Override
            public void onNext(List<PostTab> postTabs) {
                LatestPresenter.this.page = page;
                ArrayList<PostSection> sections = new ArrayList<PostSection>();
                if (page == 1) {
                    sections.clear();
                    day = "";
                }
                for (PostTab tab : postTabs) {
                    String curr = dateFormat.format(new Date(tab.getPublish_time() * 1000));
                    if (!TextUtils.isEmpty(day) && !day.equals(curr)) {
                        sections.add(new PostSection(true, "- " + curr + " -"));
                    }
                    sections.add(new PostSection(tab));
                    day = curr;
                }
                if (page == 1) {
                    mView.onReloadList(sections);
                } else {
                    mView.onLoadList(sections);
                }
            }
        });
    }

    int page = 1;

    public void onRefresh() {
        page = 1;
        loadTabPost(page);
    }

    public void onLoadMore() {
        page++;
        loadTabPost(page);
    }
    String tabTitle="";

    public void onListScroll(RecyclerView recyclerView, BaseQuickAdapter adapter, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            PostSection section = (PostSection) adapter.getItem(pos);
            if (!section.isHeader) {
                String curr = dateFormat.format(new Date(section.t.getPublish_time() * 1000));
                if (!tabTitle.equals(curr)) {
                    tabTitle = curr;
                    if (tabTitle.equals(today)) {
                        curr = "最新";
                    }
                    if (RxBus.getInstance().hasObservers()) {
                        RxBus.getInstance().send(new UpdateTabTitleEvent(curr));
                    }
                }
            }
        }
    }

    public class UpdateTabTitleEvent {
        private String title;

        public UpdateTabTitleEvent(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    @Override
    public void onEvent(Object object) {

    }
}
