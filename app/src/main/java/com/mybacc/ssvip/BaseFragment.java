package com.mybacc.ssvip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by 0000- on 2016/6/11.
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    public T presenter;

    public abstract T initPresenter();

    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentView(), null);
        ButterKnife.bind(this, rootView);
        presenter = initPresenter();
        presenter.attach((V) this);
        initViews();
        return rootView;
    }


    public abstract int getContentView();

    public abstract void initViews();

    @Override
    public void onDestroy() {
        presenter.dettach();
        super.onDestroy();
    }
}
