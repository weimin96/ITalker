package com.aoliao.example.common.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aoliao.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 你的奥利奥
 * @version 2017/8/10
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>> implements
        View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {
    private final List<T> mDataList;
    private AdapterListener<T> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<T>(), listener);
    }

    public RecyclerAdapter(List<T> dataList, AdapterListener<T> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 得到布局的类型
     *
     * @param position position
     * @return xml文件的id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position position
     * @param data     data
     * @return xml文件的id
     */
    protected abstract int getItemViewType(int position, T data);

    /**
     * 创建一个viewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型 约定为xml布局id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        //得到LayoutInflater用于把xml初始化为view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);

        //设置view的tag为viewHolder，进行双向绑定 达到重用目的
        //这里避免tag被占用，使用了key，第二个参数为object类型
        root.setTag(R.id.tag_recycler_holder, holder);

        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //进行界面注解绑定
        holder.mUnbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.mCallback = this;
        return holder;
    }

    protected abstract ViewHolder<T> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个viewHolder上
     *
     * @param holder   holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        //得到需要绑定的数据
        T data = mDataList.get(position);
        //触发holder的绑定方法
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 返回整个集合
     * @return List<T>
     */
    public List<T> getItems(){
        return mDataList;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null) {
            ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
            int pos = viewHolder.getAdapterPosition();
            mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param listener listener
     */
    public void setListener(AdapterListener<T> listener) {
        this.mListener = listener;
    }

    /**
     * item监听
     *
     * @param <T> data
     */
    public interface AdapterListener<T> {
        void onItemClick(ViewHolder holder, T data);

        void onItemLongClick(ViewHolder holder, T data);
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data data
     */
    public void add(T data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据并通知插入
     *
     * @param dataList dataList
     */
    @SafeVarargs
    public final void add(T... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = dataList.length;
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据并通知插入
     *
     * @param dataList dataList
     */
    public void add(Collection<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = dataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    @Override
    public void update(T t, ViewHolder<T> holder) {
        //得到当前viewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            mDataList.remove(pos);
            mDataList.add(pos, t);
            notifyItemChanged(pos);
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList dataList
     */
    public void replace(Collection<T> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {
        private AdapterCallback<T> mCallback;
        private Unbinder mUnbinder;
        protected T mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(T data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候调用
         *
         * @param t 绑定的数据
         */
        protected abstract void onBind(T t);

        public void updateData(T data) {
            if (this.mCallback != null) {
                this.mCallback.update(data, this);
            }
        }
    }

    /**
     * 对回调接口做一次实现AdapterListener
     *
     * @param <T>
     */
    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {

        @Override
        public void onItemClick(ViewHolder holder, T t) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, T t) {

        }
    }
}
