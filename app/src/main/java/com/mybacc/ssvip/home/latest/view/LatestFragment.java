package com.mybacc.ssvip.home.latest.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mybacc.ssvip.BaseFragment;
import com.example.mybacc.ssvip.Henson;
import com.example.mybacc.ssvip.R;
import com.mybacc.ssvip.View.AutoScrollViewPager;
import com.mybacc.ssvip.home.latest.adapter.BannerAdapter;
import com.mybacc.ssvip.home.latest.adapter.LatestAdapter;
import com.mybacc.ssvip.home.latest.model.Banner;
import com.mybacc.ssvip.home.latest.model.PostSection;
import com.mybacc.ssvip.home.latest.presenter.LatestPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpb on 16/6/8.
 */
public class LatestFragment extends BaseFragment<ILatestView, LatestPresenter>
        implements ILatestView, BaseQuickAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.list_post)
    RecyclerView listPost;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;

    AutoScrollViewPager viewPager;
    LatestAdapter latestAdapter;
    List<PostSection> sections = new ArrayList<>();

    public static LatestFragment newInstance() {
        LatestFragment fragment = new LatestFragment();
        return fragment;
    }


    @Override
    public int getContentView() {
        return R.layout.fragment_latest;
    }

    @Override
    public void initViews() {


        latestAdapter = new LatestAdapter(getContext(), sections);

        latestAdapter.openLoadMore(10, true);
        latestAdapter.setOnRecyclerViewItemClickListener(this);

        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.latest_header, null);
        viewPager = (AutoScrollViewPager) headerView.findViewById(R.id.vp);
        viewPager.setInterval(3000);
        viewPager.setAutoScrollDurationFactor(10);

        latestAdapter.addHeaderView(headerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listPost.setLayoutManager(linearLayoutManager);
        listPost.setAdapter(latestAdapter);
        listPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                presenter.onListScroll(recyclerView, latestAdapter, newState);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        latestAdapter.setOnLoadMoreListener(this);
        presenter.loadTabPost(1);
        presenter.loadBanner();
    }

    @Override
    public void onItemClick(View view, int i) {
        PostSection section = (PostSection) latestAdapter.getItem(i);
        if (!section.isHeader) {
            Intent intent = Henson.with(getActivity())
                    .gotoPlayerActivity()
                    .fromCache(false)
                    .postId(section.t.getPostid())
                    .url(section.t.getRequest_url())
                    .build();
            startActivity(intent);
        }

    }


    @Override
    public LatestPresenter initPresenter() {
        return new LatestPresenter();
    }


    @Override
    public void onReloadList(ArrayList<PostSection> sections) {
        latestAdapter.setNewData(sections);
        latestAdapter.openLoadMore(10, true);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadList(ArrayList<PostSection> sections) {
        latestAdapter.notifyDataChangedAfterLoadMore(sections, true);
    }

    @Override
    public void onGetBanner(List<Banner> banners) {
        ArrayList<View> views = new ArrayList<>();
        for (Banner banner : banners) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_banner, null);
            view.setTag(banner);
            views.add(view);
        }
        viewPager.setAdapter(new BannerAdapter(views));
        viewPager.startAutoScroll();
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.onLoadMore();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.stopAutoScroll();
    }
}
