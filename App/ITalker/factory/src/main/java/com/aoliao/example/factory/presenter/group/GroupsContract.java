package com.aoliao.example.factory.presenter.group;

import com.aoliao.example.factory.model.db.Group;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/20
 */

public interface GroupsContract extends BaseContract {
    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends RecyclerView<Presenter,Group>{

    }
}
