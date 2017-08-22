package com.aoliao.example.factory.data.message;

import com.aoliao.example.factory.model.card.MessageCard;

/**
 * 消息中心
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public interface MessageCenter {
    void dispatch(MessageCard... cards);

}
