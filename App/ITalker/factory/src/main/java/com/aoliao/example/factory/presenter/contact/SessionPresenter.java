package com.aoliao.example.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.aoliao.example.factory.data.message.SessionDataSource;
import com.aoliao.example.factory.data.message.SessionRepository;
import com.aoliao.example.factory.model.db.Session;
import com.aoliao.example.factory.presenter.BaseSourcePresenter;
import com.aoliao.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/23
 */

public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;

        // 差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 刷新界面
        refreshData(result, sessions);
    }
}
