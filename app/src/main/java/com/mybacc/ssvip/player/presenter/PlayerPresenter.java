package com.mybacc.ssvip.player.presenter;

import com.mybacc.ssvip.BasePresenter;
import com.mybacc.ssvip.data.HttpResultFunc;
import com.mybacc.ssvip.data.Repository;
import com.mybacc.ssvip.data.RxHelp;
import com.mybacc.ssvip.player.model.Comment;
import com.mybacc.ssvip.player.model.PostDetail;
import com.mybacc.ssvip.player.model.VideoInfo;
import com.mybacc.ssvip.player.view.IPlayerView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.List;

import io.realm.Realm;
import rx.Observable;


/**
 * Created by 0000- on 2016/6/12.
 */
public class PlayerPresenter extends BasePresenter<IPlayerView> implements IPlayerPresenter {


    @Override
    public void getPostDetail(String postId) {
        Observable<PostDetail> observable = Repository.getInstance().getVmovierApi()
                .getPostDetail(Integer.parseInt(postId))
                .map(new HttpResultFunc<PostDetail>());
        addRxHelp(new RxHelp<PostDetail>().toSubscribe(observable, new RxHelp.OnNext<PostDetail>() {
            @Override
            public void onNext(PostDetail postDetail) {
                mView.onGetPostDetail(postDetail);
            }
        }));
    }

    @Override
    public void getComments(int p, String postId) {
        Observable<List<Comment>> observable = Repository.getInstance().getVmovierApi()
                .getComments(p, Integer.parseInt(postId), 0)
                .map(new HttpResultFunc<List<Comment>>());
        addRxHelp(
                new RxHelp<List<Comment>>().toSubscribe(observable, new RxHelp.OnNext<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> comments) {
                        mView.onGetComments(comments);
                    }
                }));
    }

    @Override
    public void onEvent(Object object) {

    }

    public void addDownload(final VideoInfo videoInfo) {
        FileDownloader.getImpl().create(videoInfo.getSource_link())
                .setPath(FileDownloadUtils.getDefaultSaveFilePath(videoInfo.getSource_link()))
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(downloadListener);
        Realm.getDefaultInstance().beginTransaction();
        videoInfo.setDownloadId(FileDownloadUtils.generateId(videoInfo.getQiniu_url(), FileDownloadUtils.getDefaultSaveFilePath(videoInfo.getQiniu_url())));
        videoInfo.setDownloadStatus(1);
        Realm.getDefaultInstance().copyToRealm(videoInfo);
        Realm.getDefaultInstance().commitTransaction();
    }

    private FileDownloadListener downloadListener = new FileDownloadSampleListener() {
        @Override
        protected void completed(final BaseDownloadTask task) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    VideoInfo info = realm.where(VideoInfo.class).equalTo("downloadId", task.getId()).findFirst();
                    info.setDownloadStatus(2);
                }
            });
        }
    };
}
