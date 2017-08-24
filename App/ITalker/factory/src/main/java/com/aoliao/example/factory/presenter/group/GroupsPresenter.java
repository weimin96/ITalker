package com.aoliao.example.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.aoliao.example.factory.data.group.GroupsDataSource;
import com.aoliao.example.factory.data.group.GroupsRepository;
import com.aoliao.example.factory.data.helper.GroupHelper;
import com.aoliao.example.factory.model.db.Group;
import com.aoliao.example.factory.presenter.BaseSourcePresenter;
import com.aoliao.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/24
 */

public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据, 以后可以优化到下拉刷新中
        // 只有用户下拉进行网络请求刷新
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        // 对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 界面刷新
        refreshData(result, groups);
    }
}
