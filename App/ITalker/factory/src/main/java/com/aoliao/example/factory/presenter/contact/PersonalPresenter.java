package com.aoliao.example.factory.presenter.contact;

import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.data.helper.UserHelper;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.persistence.Account;
import com.aoliao.example.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * @author 你的奥利奥
 * @version 2017/8/20
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {

    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        // 个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(user);
                }
            }
        });
    }

    private void onLoaded(final User user) {
        this.user = user;
        // 是否就是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        // 是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        // 已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;

        // 切换到Ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view = getView();
                if (view == null)
                    return;
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHelloOrLogout(allowSayHello,isSelf);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
