package com.aoliao.example.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.aoliao.example.common.R;
import com.aoliao.example.factory.model.Author;
import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 你的奥利奥
 * @version 2017/8/11
 */

public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, Author author) {
        if (author == null)
            return;
        setup(manager, author.getPortrait());
    }

    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null)
            url = "";
        manager.load(url)
                .placeholder(resourceId)//占位布局
                .centerCrop()
                .dontAnimate()//CircleImageView不能使用渐变动画
                .into(this);
    }
}
