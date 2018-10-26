package com.mybacc.ssvip.cache;

import com.mybacc.ssvip.player.model.VideoInfo;

import java.util.List;

/**
 * Created by 0000- on 2016/6/22.
 */
public interface ICacheView {
    public void updateCachedList(List<VideoInfo> videoInfoList,int cachingCount,String title);
}
