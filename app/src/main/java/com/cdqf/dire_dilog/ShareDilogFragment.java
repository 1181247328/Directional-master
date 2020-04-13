package com.cdqf.dire_dilog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.ShareQQFind;
import com.cdqf.dire_find.ShareZnoeFind;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.ShareUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 分享
 */
public class ShareDilogFragment extends DialogFragment {

    private String TAG = ShareDilogFragment.class.getSimpleName();

    private View view = null;

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private String fileImage = null;

    //微信好友
    @BindView(R.id.ll_share_item_wetn)
    public LinearLayout llShareItemWetn = null;

    //朋友圈
    @BindView(R.id.ll_share_item_wetntwo)
    public LinearLayout llShareItemWetntwo = null;

    //QQ好友
    @BindView(R.id.ll_share_item_qq)
    public LinearLayout llShareItemQq = null;

    //QQ空间
    @BindView(R.id.ll_share_item_qqtwo)
    public LinearLayout llShareItemQqtwo = null;

    //取消
    @BindView(R.id.tv_share_item_cancel)
    public TextView tvShareItemCancel = null;

    /**
     * 传送的图片
     *
     * @param file
     */
    public void setFile(String file) {
        this.fileImage = file;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.item_dilog_share, null);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //初始化前
        initAgo();

        //初始化控件
        initView();

        //适配器
        initAdapter();

        //注册监听器
        initListener();

        //初始化后
        initBack();
        return view;
    }

    /**
     * 初始化前
     */
    private void initAgo() {
        ButterKnife.bind(this, view);
    }

    /**
     * 初始化控件
     */
    private void initView() {
    }

    private void initAdapter() {

    }

    /**
     * 注册监听器
     */
    private void initListener() {

    }

    /**
     * 初始化后
     */
    private void initBack() {
//        getDialog().setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.ll_share_item_wetn, R.id.ll_share_item_wetntwo, R.id.ll_share_item_qq, R.id.ll_share_item_qqtwo, R.id.tv_share_item_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            //微信好友
            case R.id.ll_share_item_wetn:
                ShareUtils.sharePictureToWechatFriend(getContext(), new File(fileImage));
                dismiss();
                break;
            //朋友圈
            case R.id.ll_share_item_wetntwo:
                ShareUtils.sharePictureToTimeLine(getContext(), new File(fileImage));
                dismiss();
                break;
            //QQ好友
            case R.id.ll_share_item_qq:
//                ShareUtils.sharePictureToQQFriend(getContext(), new File(fileImage));
                eventBus.post(new ShareQQFind(fileImage));
                dismiss();
                break;
            //QQ空间
            case R.id.ll_share_item_qqtwo:
//                direState.initToast(getContext(), "暂末开通", true, 0);
                eventBus.post(new ShareZnoeFind(fileImage));
                dismiss();
                break;
            //取消
            case R.id.tv_share_item_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

