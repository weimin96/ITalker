package com.aoliao.example.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * @author 你的奥利奥
 * @version 2017/8/11
 */

public class NavHelper<T> {
    //所有的Tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    //用于初始化的一些参数
    private final Context mContext;
    private final int containerId;
    private final FragmentManager mFragmentManager;
    private final OnTabChangeListener<T> mListener;
    //当前的一个选中的tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager,
                     OnTabChangeListener<T> listener) {
        mContext = context;
        this.containerId = containerId;
        mFragmentManager = fragmentManager;
        mListener = listener;
    }

    /**
     * 添加tab
     *
     * @param menuId 菜单id
     * @param tab    tab
     * @return NavHelper
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前的tab
     *
     * @return currentTab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单操作
     *
     * @param menuId 菜单id
     * @return 是否能够处理这次点击
     */
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击的菜单对应的tab
        //如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行tab选择操作
     *
     * @param tab 用户点击的tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                notifyTabReselect(tab);
                return;
            }
        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChange(currentTab, oldTab);
    }

    /**
     * 进行fragment的调度
     *
     * @param newTab newTab
     * @param oldTab oldTab
     */
    private void doTabChange(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.mFragment != null) {
                //从界面移除但还在fragment缓存当中
                ft.detach(oldTab.mFragment);
            }
        }
        if (newTab != null) {
            if (newTab.mFragment == null) {
                //缓存起来
                newTab.mFragment = Fragment.instantiate(mContext, newTab.clx.getName(), null);
                ft.add(containerId, newTab.mFragment, newTab.clx.getName());
            } else {
                ft.attach(newTab.mFragment);
            }
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (mListener != null) {
            mListener.onTabChange(newTab, oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab) {
        //TODO 二次点击所做的操作
    }

    public interface OnTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);
    }

    /**
     * 所有tab的基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Class<?> clx;
        public T t;
        Fragment mFragment;

        public Tab(Class<?> clx, T t) {
            this.clx = clx;
            this.t = t;
        }
    }
}
