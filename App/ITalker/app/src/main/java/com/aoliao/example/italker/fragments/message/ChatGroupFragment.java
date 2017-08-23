package com.aoliao.example.italker.fragments.message;


import android.support.v4.app.Fragment;

import com.aoliao.example.factory.model.db.Group;
import com.aoliao.example.factory.presenter.message.ChatContract;
import com.aoliao.example.factory.presenter.message.ChatGroupPresenter;
import com.aoliao.example.italker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView{


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;//new ChatGroupPresenter(this);
    }

    @Override
    public void onInit(Group group) {

    }
}
