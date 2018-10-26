package com.mybacc.ssvip.home.latest.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mybacc.ssvip.R;
import com.mybacc.ssvip.home.latest.model.PostSection;
import com.mybacc.ssvip.home.latest.model.PostTab;

import java.util.List;

/**
 * Created by 0000- on 2016/6/9.
 */
public class LatestAdapter extends BaseSectionQuickAdapter<PostSection> {
    public LatestAdapter(Context context, List<PostSection> data) {
        super(context, R.layout.item_latest, R.layout.item_latest_section_header, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, PostSection postSection) {
        helper.setText(R.id.month_divider, postSection.header);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, PostSection postSection) {
        PostTab postTab = postSection.t;
        baseViewHolder.setText(R.id.tilte, postTab.getTitle());
        String cateNameAndLength = postTab.getCates().get(0).getCatename();
        if (!TextUtils.isEmpty(postTab.getDuration())) {
            int len = Integer.parseInt(postTab.getDuration());
            int minutes = len / 60;
            int secs = len - minutes * 60;
            cateNameAndLength += " / " + minutes + "'" + secs + "\"";
        }
        baseViewHolder.setText(R.id.cate_and_dur, cateNameAndLength);
        Glide.with(mContext)
                .load(postTab.getImage())
                .error(android.R.color.black)
                .crossFade()
                .into((ImageView) baseViewHolder.getView(R.id.iv_post));
    }


}
