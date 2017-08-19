package com.aoliao.example.factory.presenter.search;

import com.aoliao.example.factory.model.card.GroupCard;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.presenter.BaseContract;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/19
 */

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }

    interface UserView extends BaseContract.View<Presenter>{
        void onSearchDone(List<UserCard> userCards);
    }

    interface GroupView extends BaseContract.View<Presenter>{
        void onSearchDone(List<GroupCard> groupCards);
    }
}
