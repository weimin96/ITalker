package com.aoliao.example.factory.presenter.group;

import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.data.helper.GroupHelper;
import com.aoliao.example.factory.model.db.view.MemberUserModel;
import com.aoliao.example.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/25
 */

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();

        // 异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();

            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}
