package com.aoliao.example.italker.fragments.panel;

import android.view.View;
import android.widget.ImageView;

import com.aoliao.example.common.widget.recycler.RecyclerAdapter;
import com.aoliao.example.face.Face;
import com.aoliao.example.italker.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import butterknife.BindView;

/**
 * @author 你的奥利奥
 * @version 2017/8/25
 */

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null
                // drawable 资源 id
                && ((bean.preview instanceof Integer)
                // face zip 包资源路径
                || bean.preview instanceof String))
            Glide.with(itemView.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888) //设置解码格式8888，保证清晰度
                    .placeholder(R.drawable.default_face)
                    .into(mFace);
    }
}
