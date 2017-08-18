package com.aoliao.example.italker.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.StringRes;

import android.support.v4.content.res.ResourcesCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aoliao.example.factory.presenter.user.UpdateInfoPresenter;
import com.aoliao.example.italker.R;
import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import com.aoliao.example.common.app.Application;
import com.aoliao.example.common.app.PresenterFragment;
import com.aoliao.example.common.widget.PortraitView;
import com.aoliao.example.factory.presenter.user.UpdateInfoContract;
import com.aoliao.example.italker.activities.MainActivity;
import com.aoliao.example.italker.fragments.media.GalleryFragment;

import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {
    @BindView(R.id.im_sex)
    ImageView mSex;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    // 头像的本地路径
    private String mPortraitPath;
    private boolean isMan = true;


    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                //设置图片处理的格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                //设置压缩后的图片精度
                options.setCompressionQuality(96);
                //得到头像的缓存地址
                File dFile = Application.getPortraitTmpFile();
                //需要剪切的图片 剪切后图片的缓存地址
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dFile))
                        .withAspectRatio(1, 1)   //1：1比例
                        .withMaxResultSize(525, 520) //范围最大的尺寸
                        .withOptions(options)   //相关参数
                        .start(getActivity());
            }
            //Fragment中创建Fragment建议使用getChildFragmentManager()
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    //回调当前activity的onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 收到从Activity传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
        }
    }

    private void loadPortrait(Uri uri) {
        //得到头像地址
        mPortraitPath = uri.getPath();
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }

    @OnClick(R.id.im_sex)
    void onSexClick() {
        // 性别图片点击的时候触发
        isMan = !isMan; // 反向性别

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), isMan ?
                R.drawable.ic_sex_man : R.drawable.ic_sex_woman, null);
        mSex.setImageDrawable(drawable);
        // 设置背景的层级，切换颜色
        mSex.getBackground().setLevel(isMan ? 0 : 1);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = mDesc.getText().toString();
        // 调用P层进行注册
        mPresenter.update(mPortraitPath, desc, isMan);
    }

    @Override
    public void updateSucceed() {
        // 更新成功跳转到主界面
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mDesc.setEnabled(true);
        mPortrait.setEnabled(true);
        mSex.setEnabled(true);
        // 提交按钮可以继续点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        // 正在进行时，正在进行注册，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mDesc.setEnabled(false);
        mPortrait.setEnabled(false);
        mSex.setEnabled(false);
        // 提交按钮不可以继续点击
        mSubmit.setEnabled(false);
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }
}
