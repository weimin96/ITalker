package com.aoliao.example.factory.data.user;

import android.text.TextUtils;

import com.aoliao.example.factory.data.helper.DbHelper;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.model.db.UserFollow;
import com.aoliao.example.factory.persistence.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    //单线程池，一个一个的处理卡片
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new UserDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        mExecutor.execute(new UserCardHandler(cards));
    }

    /*
    线程调度的时候会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;


        private UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<User> users = new ArrayList<>();
            List<UserFollow> originFollows = new ArrayList<>();
            List<UserFollow> targetFollows = new ArrayList<>();

            for (UserCard card : cards) {
                //进行过滤操作
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                //添加操作
                users.add(card.build());

                //添加关注列表
                originFollows.add(card.buildOriginFollow());
                targetFollows.add(card.buildTargetFollow());
            }
            //进行数据库存储，并分发通知，异步操作
            DbHelper.save(User.class, users.toArray(new User[0]));
            DbHelper.save(UserFollow.class, originFollows.toArray(new UserFollow[0]));
            DbHelper.save(UserFollow.class, targetFollows.toArray(new UserFollow[0]));
        }
    }
}
