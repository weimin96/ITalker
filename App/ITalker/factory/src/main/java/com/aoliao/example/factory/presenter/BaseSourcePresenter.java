package com.aoliao.example.factory.presenter;

import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.data.DbDataSource;

import java.util.List;

/**
 * 基础的仓库源的Presenter定义
 *
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
