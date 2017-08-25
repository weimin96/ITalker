package com.aoliao.example.factory.data.message;

import com.aoliao.example.factory.data.DbDataSource;
import com.aoliao.example.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository, MessageGroupRepository
 * 关注的对象是Message表
 *
 * @author 你的奥利奥
 * @version 2017/8/23
 */

public interface MessageDataSource extends DbDataSource<Message> {
}
