package com.aoliao.example.common.widget.recycler;

/**
 * @author 你的奥利奥
 * @version 2017/8/10
 */

public interface AdapterCallback<T> {
    void update(T t,RecyclerAdapter.ViewHolder<T> holder);
}
