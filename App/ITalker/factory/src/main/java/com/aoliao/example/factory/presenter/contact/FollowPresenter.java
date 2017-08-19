package com.aoliao.example.factory.presenter.contact;

import android.support.annotation.StringRes;

import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.helper.UserHelper;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @author 你的奥利奥
 * @version 2017/8/19
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
implements FollowContract.Presenter,DataSource.Callback<UserCard>{
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }


    @Override
    public void onDataNotAvailable(@StringRes final int strRes) {
        final FollowContract.View view=getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }

    @Override
    public void onDataLoaded(final UserCard card) {
        final FollowContract.View view=getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(card);
                }
            });
        }
    }
}
