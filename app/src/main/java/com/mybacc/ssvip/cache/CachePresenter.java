package com.mybacc.ssvip.cache;

import com.mybacc.ssvip.BasePresenter;
import com.mybacc.ssvip.player.model.VideoInfo;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by 0000- on 2016/6/22.
 */
public class CachePresenter extends BasePresenter<ICacheView> {


    public void initCacheList() {
        final RealmResults<VideoInfo> result = Realm.getDefaultInstance().where(VideoInfo.class).equalTo("downloadStatus", 2).findAll();
        result.addChangeListener(new RealmChangeListener<RealmResults<VideoInfo>>() {
            @Override
            public void onChange(RealmResults<VideoInfo> element) {
                updateList(result);
            }
        });
        updateList(result);
    }

    private void updateList(RealmResults<VideoInfo> result) {
        long count = Realm.getDefaultInstance().where(VideoInfo.class).equalTo("downloadStatus", 1).count();
        String title = "";
        if (count > 0) {
            title = Realm.getDefaultInstance().where(VideoInfo.class).equalTo("downloadStatus", 1).findFirst().getTitle();
        }
        mView.updateCachedList(new ArrayList<VideoInfo>(result), (int) count,title);
    }

    @Override
    public void onEvent(Object object) {

    }
}
