package com.aoliao.example.factory.data.group;

import com.aoliao.example.factory.model.card.GroupCard;
import com.aoliao.example.factory.model.card.GroupMemberCard;

/**
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
