package com.aoliao.example.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.aoliao.example.common.app.Activity;
import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.italker.R;
import com.aoliao.example.italker.fragments.user.UpdateInfoFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

//用户信息界面，提供用户信息修改
public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    public static void show(Context context){
        context.startActivity(new Intent(context,UserActivity.class));
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();

        //初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView,GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        //拿到glide当前的drawable
                        Drawable drawable=resource.getCurrent();
                        //使用适配类进行包装
                        drawable= DrawableCompat.wrap(drawable);
                        //设置着色效果和颜色
                        drawable.setColorFilter(UiCompat.getColor(getResources(),R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);//蒙版
                        //设置给imageView
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * activity收到剪切图片进行回调
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}
