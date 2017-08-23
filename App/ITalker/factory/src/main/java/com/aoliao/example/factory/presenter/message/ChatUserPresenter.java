package com.aoliao.example.factory.presenter.message;

import com.aoliao.example.factory.data.helper.UserHelper;
import com.aoliao.example.factory.data.message.MessageRepository;
import com.aoliao.example.factory.model.db.Message;
import com.aoliao.example.factory.model.db.User;

/**
 * @author 你的奥利奥
 * @version 2017/8/23
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();

        // 从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
