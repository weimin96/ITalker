package com.aoliao.example.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoliao.example.common.app.PresenterToolbarActivity;
import com.aoliao.example.common.widget.PortraitView;
import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.persistence.Account;
import com.aoliao.example.factory.presenter.contact.PersonalContract;
import com.aoliao.example.factory.presenter.contact.PersonalPresenter;
import com.aoliao.example.italker.R;
import com.bumptech.glide.Glide;

import net.qiujuer.genius.res.Resource;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View {
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;
    private boolean isAllowSayHello;
    private boolean isAllowLogout;

    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_name)
    TextView mName;
    @BindView(R.id.txt_desc)
    TextView mDesc;
    @BindView(R.id.txt_follows)
    TextView mFollows;
    @BindView(R.id.txt_following)
    TextView mFollowing;
    @BindView(R.id.btn_say_hello)
    Button mSayHello;

    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            // 进行关注操作
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    void onSayHelloOrLogoutClick() {
        // 发起聊天的点击
        User user = mPresenter.getUserPersonal();
        if (user == null)
            return;
        if (user.getId().equals(Account.getUserId())) {
            Factory.logout();
        } else {
            MessageActivity.show(this, user);
        }
    }

    /**
     * 更改关注菜单状态
     */
    private void changeFollowItemStatus() {
        if (mFollowItem == null) {
            return;
        }
        //根据状态设置颜色
        Drawable drawable = mIsFollowUser ? getResources()
                .getDrawable(R.drawable.ic_favorite) :
                getResources().getDrawable(R.drawable.ic_favorite_border);
//        Drawable drawable=ResourcesCompat.getDrawable(
//                getResources(),mIsFollowUser?R.drawable.ic_favorite:R.drawable.ic_favorite_border,null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
        mFollowItem.setIcon(drawable);
    }


    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        Glide.with(this)
                .load(user.getPortrait())
                .asBitmap()
                .centerCrop()
                .into(mHeader);
        mPortrait.setup(Glide.with(this), user);
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        hideLoading();
    }

    @Override
    public void allowSayHelloOrLogout(boolean isAllowSayHello, boolean isAllowLogout) {
        mSayHello.setVisibility(isAllowLogout||isAllowSayHello ? View.VISIBLE : View.GONE);
        if (isAllowLogout){
            mSayHello.setText(R.string.btn_logout);
        }else if (isAllowSayHello){
            mSayHello.setText(R.string.btn_send_start);
        }
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollowUser = isFollow;
        changeFollowItemStatus();
    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }
}
