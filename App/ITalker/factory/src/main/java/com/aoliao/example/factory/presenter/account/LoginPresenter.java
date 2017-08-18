package com.aoliao.example.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.helper.AccountHelper;
import com.aoliao.example.factory.model.api.account.LoginModel;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.persistence.Account;
import com.aoliao.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @author 你的奥利奥
 * @version 2017/8/17
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {
    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        //此时从网络送回来，并不保证在主线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final @StringRes int strRes) {
        final LoginContract.View view = getView();
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
