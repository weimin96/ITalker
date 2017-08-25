package com.aoliao.example.factory.presenter.message;

import com.aoliao.example.factory.model.db.Group;
import com.aoliao.example.factory.model.db.Message;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.model.db.view.MemberUserModel;
import com.aoliao.example.factory.presenter.BaseContract;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/23
 */

public interface ChatContract {
    interface Presenter extends BaseContract.Presenter {
        // 发送文字
        void pushText(String content);

        // 发送语音
        void pushAudio(String path);

        // 发送图片
        void pushImages(String[] paths);

        // 重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }

    // 界面的基类
    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        // 初始化的Model
        void onInit(InitModel model);
    }

    // 人聊天的界面
    interface UserView extends View<User> {

    }

    // 群聊天的界面
    interface GroupView extends View<Group> {
        void showAdminOption(boolean isAdmin);

        //初始化成员信息
        void onInitGroupMembers(List<MemberUserModel> members,long moreCount);
    }
}
