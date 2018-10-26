package com.mybacc.ssvip.home.latest.view;

import com.mybacc.ssvip.home.latest.model.Banner;
import com.mybacc.ssvip.home.latest.model.PostSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpb on 16/6/8.
 */
public interface ILatestView {
    public void onReloadList(ArrayList<PostSection>sections);
    public void onLoadList(ArrayList<PostSection>sections);
    public void onGetBanner(List<Banner> banners);
}
