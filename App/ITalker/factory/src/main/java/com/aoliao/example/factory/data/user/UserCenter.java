package com.aoliao.example.factory.data.user;

import com.aoliao.example.factory.model.card.UserCard;

/**
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public interface UserCenter {
    //分发处理一堆用户卡片的信息，并分发到数据库
    void dispatch(UserCard... cards);
}