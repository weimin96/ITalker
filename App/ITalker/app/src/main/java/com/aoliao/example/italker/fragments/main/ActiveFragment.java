package com.aoliao.example.italker.fragments.main;

import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.common.widget.GalleyView;
import com.aoliao.example.italker.R;

import butterknife.BindView;

public class ActiveFragment extends Fragment {
    @BindView(R.id.gv_galleyView)
    GalleyView mGalleyView;

    public ActiveFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGalleyView.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
