package com.aoliao.example.italker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.aoliao.example.common.app.Activity;
import com.aoliao.example.factory.persistence.Account;
import com.aoliao.example.italker.activities.AccountActivity;
import com.aoliao.example.italker.activities.MainActivity;
import com.aoliao.example.italker.fragments.assist.PermissionFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LauncherActivity extends Activity {

    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        //创建一个Drawable
        ColorDrawable drawable = new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                waitPushIdReceiverId();
            }
        });
    }

    private void waitPushIdReceiverId() {
        if (Account.isLogin()){
            //已经登陆情况下，判断是否绑定
            //如果没有绑定，等待广播接收器进行绑定
            if (Account.isBind()){
                skip();
                return;
            }
        }else {
            //没有登陆
            //如果拿到pushId
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushIdReceiverId();
            }
        }, 500);
    }

    //完成剩下的50%
    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    private void reallySkip() {
        //直到权限申请成功才能显示MainActivity
        if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
            //检查跳转到主页还是登陆
            if (Account.isLogin()) {
                MainActivity.show(this);
            }else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, final Runnable endCallback) {
        //获取一个最终的颜色
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500);//时间
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);//开始结束值
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final Property<LauncherActivity, Object> property = new Property<LauncherActivity, Object>(Object.class, "color") {

        @Override
        public void set(LauncherActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }

        @Override
        public Object get(LauncherActivity object) {
            return object.mBgDrawable.getColor();
        }
    };
}
