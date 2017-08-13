package com.aoliao.example.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.aoliao.example.common.R;
import com.aoliao.example.common.widget.recycler.RecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.aoliao.example.common.R.id.view_shade;


public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3; // 最大选中图片数量
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024; // 最小的图片大小
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private List<Image> mSelectedImages = new LinkedList<>();//方便查找
    private SelectedChangeListener mListener;
    private Adapter mAdapter = new Adapter();

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                // Cell点击操作，如果说我们的点击是允许的，那么更新对应的Cell的状态
                // 然后更新界面，同理；如果说不能允许点击（已经达到最大的选中数量）那么就不刷新界面
                if (onItemSelectClick(image)) {
                    //noinspection unchecked
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 初始化方法
     *
     * @param loaderManager Loader管理器
     * @param listener      listener
     * @return 一个LOADER_ID，可用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);//初始化一个loader,并注册回调事件
        return LOADER_ID;
    }

    /**
     * cell点击具体逻辑
     *
     * @param image 被点击的image
     * @return true 已处理
     */
    private boolean onItemSelectClick(Image image) {
        // 是否需要进行刷新
        boolean notifyRefresh;
        //之前是否存在
        if (mSelectedImages.contains(image)) {
            //移除
            mSelectedImages.remove(image);
            image.isSelect = false;
            // 状态已经改变则需要更新
            notifyRefresh = true;
        } else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        // 如果数据有更改，
        // 那么我们需要通知外面的监听者我们的数据选中改变了
        if (notifyRefresh)
            notifySelectChanged();
        return true;
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        // 得到监听者，并判断是否有监听者，然后进行回调数量变化
        SelectedChangeListener listener = mListener;
        listener.onSelectedCountChanged(mSelectedImages.size());//回调图片数量
    }

    /**
     * 得到选中的图片的全部地址
     *
     * @return 返回一个数组
     */
    public String[] getSelectedPath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image : mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 可以进行清空选中的图片
     */
    public void clear() {
        for (Image image : mSelectedImages) {
            // 一定要先重置状态
            image.isSelect = false;
        }
        mSelectedImages.clear();
        // 通知更新
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知Adapter数据更改的方法
     *
     * @param images 新的数据
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,    //图片id
                MediaStore.Images.Media.DATA,   //图片路径
                MediaStore.Images.Media.DATE_ADDED // 图片的创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC");//倒叙查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Image> images = new ArrayList<>();
            // 判断是否有数据
            if (data != null && data.getCount() > 0) {
                // 移动游标到开始
                data.moveToFirst();
                int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                do {
                    // 循环读取，直到没有下一条数据
                    int id = data.getInt(indexId);
                    String path = data.getString(indexPath);
                    long dateTime = data.getLong(indexDate);

                    File file = new File(path);
                    if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                        // 如果没有图片，或者图片大小太小，则跳过
                        continue;
                    }
                    // 添加一条新的数据
                    Image image = new Image();
                    image.id = id;
                    image.path = path;
                    image.date = dateTime;
                    images.add(image);
                } while (data.moveToNext());
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // 当Loader销毁或者重置了, 进行界面清空
        }
    }

    /**
     * 内部类型
     */
    private static class Image {
        int id;
        String path;
        Long date;
        boolean isSelect;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image data) {
            return R.layout.cell_galley;
        }

        @Override
        protected RecyclerAdapter.ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShape;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mPic = (ImageView) itemView.findViewById(R.id.im_image);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);
            mShape = itemView.findViewById(view_shade);
        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存
                    .placeholder(R.color.grey_200)//默认颜色
                    .into(mPic);
            mShape.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 对外的一个监听器
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);
    }
}
