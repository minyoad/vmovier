package com.mybacc.ssvip.home.channel.presenter;

import com.mybacc.ssvip.BasePresenter;
import com.mybacc.ssvip.data.HttpResultFunc;
import com.mybacc.ssvip.data.Repository;
import com.mybacc.ssvip.data.RxHelp;
import com.mybacc.ssvip.home.channel.view.IChannelView;
import com.mybacc.ssvip.home.model.Cate;

import java.util.List;

import rx.Observable;

/**
 * Created by 0000- on 2016/6/9.
 */
public class ChannelPresenter extends BasePresenter<IChannelView>  {



    public void getAllChannel() {
        Observable<List<Cate>> observable = Repository.getInstance()
                .getVmovierApi()
                .getCateList()
                .map(new HttpResultFunc<List<Cate>>());
        new RxHelp<List<Cate>>().toSubscribe(observable, new RxHelp.OnNext<List<Cate>>() {
            @Override
            public void onNext(List<Cate> cates) {
                mView.onLoadChannels(cates);
            }
        });
    }

    @Override
    public void onEvent(Object object) {

    }
}
