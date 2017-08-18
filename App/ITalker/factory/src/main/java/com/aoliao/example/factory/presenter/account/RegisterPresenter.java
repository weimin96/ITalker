package com.aoliao.example.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

import com.aoliao.example.common.Common;
import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.helper.AccountHelper;
import com.aoliao.example.factory.model.api.account.RegisterModel;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.persistence.Account;
import com.aoliao.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();
        //得到view
        RegisterContract.View view = getView();
        if (!checkMobile(phone)) {
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求
            //构造model，进行请求调用
            RegisterModel registerModel = new RegisterModel(phone, name, password, Account.getPushId());
            AccountHelper.register(registerModel, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) &&
                Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        //此时从网络送回来，并不保证在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final RegisterContract.View view = getView();
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
