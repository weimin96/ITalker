package com.aoliao.example.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author 你的奥利奥
 * @version 2017/8/22
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
