package com.aoliao.example.italker.fragments.panel;

import android.view.View;

import com.aoliao.example.common.widget.recycler.RecyclerAdapter;
import com.aoliao.example.face.Face;
import com.aoliao.example.italker.R;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/25
 */

public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(beans, listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
