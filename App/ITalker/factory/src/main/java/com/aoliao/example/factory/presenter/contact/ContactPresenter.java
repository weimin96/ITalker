package com.aoliao.example.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.aoliao.example.common.widget.recycler.RecyclerAdapter;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.helper.UserHelper;
import com.aoliao.example.factory.data.user.ContactDataSource;
import com.aoliao.example.factory.data.user.ContactRepository;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.presenter.BasePresenter;
import com.aoliao.example.factory.presenter.BaseRecyclerPresenter;
import com.aoliao.example.factory.presenter.BaseSourcePresenter;
import com.aoliao.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/20
 */

public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ContactContract.View>
implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {
    private ContactDataSource mDataSource;
    public ContactPresenter(ContactContract.View view) {
        //初始化数据仓库
        super(new ContactRepository(),view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
    }

    //子线程
    @Override
    public void onDataLoaded(List<User> users) {
        //无论怎么操作，数据变更最终都会通知到这里
        final ContactContract.View view=getView();
        RecyclerAdapter<User> adapter=view.getRecyclerAdapter();
        List<User> old=adapter.getItems();
        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //调用基类方法进行界面刷新
        refreshData(result,users);

    }
}
