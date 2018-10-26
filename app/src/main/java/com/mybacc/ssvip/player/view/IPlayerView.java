package com.mybacc.ssvip.player.view;

import com.mybacc.ssvip.player.model.Comment;
import com.mybacc.ssvip.player.model.PostDetail;

import java.util.List;

/**
 * Created by 0000- on 2016/6/12.
 */
public interface IPlayerView {
    public void onGetPostDetail(PostDetail detail);

    public void onGetComments(List<Comment> comments);


}
