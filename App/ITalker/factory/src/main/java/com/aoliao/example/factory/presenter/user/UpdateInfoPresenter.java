package com.aoliao.example.factory.presenter.user;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.aoliao.example.common.app.Application;
import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.helper.UserHelper;
import com.aoliao.example.factory.model.api.account.UserUpdateModel;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.net.UploadHelper;
import com.aoliao.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @author 你的奥利奥
 * @version 2017/8/18
 */

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();
        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
        } else {
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadPortrait(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        //上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        //构建Model
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        //进行网络请求，上传
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoaded(UserCard card) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        //此时从网络送回来，并不保证在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSucceed();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        //此时从网络送回来，并不保证在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
