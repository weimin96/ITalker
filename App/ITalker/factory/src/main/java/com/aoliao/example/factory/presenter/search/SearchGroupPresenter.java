package com.aoliao.example.factory.presenter.search;

import com.aoliao.example.factory.presenter.BasePresenter;

/**
 * @author 你的奥利奥
 * @version 2017/8/19
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter{
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
