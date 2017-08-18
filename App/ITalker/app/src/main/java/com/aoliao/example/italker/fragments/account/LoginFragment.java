package com.aoliao.example.italker.fragments.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;

import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.common.app.PresenterFragment;
import com.aoliao.example.factory.presenter.account.LoginContract;
import com.aoliao.example.factory.presenter.account.LoginPresenter;
import com.aoliao.example.italker.R;
import com.aoliao.example.italker.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.name;


public class LoginFragment extends PresenterFragment<LoginContract.Presenter> implements LoginContract.View {
    private AccountTrigger mTrigger;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到activity的引用
        mTrigger= (AccountTrigger) context;
    }

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone=mPhone.getText().toString();
        String password=mPassword.getText().toString();
        mPresenter.login(phone,password);
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegister(){
        mTrigger.triggerView();
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        mLoading.stop();
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);

    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
