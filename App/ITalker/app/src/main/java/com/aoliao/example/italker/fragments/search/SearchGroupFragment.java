package com.aoliao.example.italker.fragments.search;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.common.app.PresenterFragment;
import com.aoliao.example.factory.model.card.GroupCard;
import com.aoliao.example.factory.presenter.search.SearchContract;
import com.aoliao.example.factory.presenter.search.SearchGroupPresenter;
import com.aoliao.example.italker.R;
import com.aoliao.example.italker.activities.SearchActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment,SearchContract.GroupView{


    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected SearchGroupPresenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {

    }
}
