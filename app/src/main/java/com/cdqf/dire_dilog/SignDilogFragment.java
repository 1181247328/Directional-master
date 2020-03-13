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

import com.cdqf.dire.R;
import com.cdqf.dire_find.SignFind;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 开始活动
 */
public class SignDilogFragment extends DialogFragment {
    private String TAG = SignDilogFragment.class.getSimpleName();

    private View view = null;

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private String code;

    //稍等
    @BindView(R.id.rcrl_sign_dilog_moment)
    public RCRelativeLayout rcrlSignDilogMoment = null;

    //开始
    @BindView(R.id.rcrl_sign_dilog_start)
    public RCRelativeLayout rcrlSignDilogStart = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_sign, null);
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
        getDialog().setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.rcrl_sign_dilog_moment, R.id.rcrl_sign_dilog_start})
    public void onClick(View v) {
        switch (v.getId()) {
            //稍等
            case R.id.rcrl_sign_dilog_moment:
                dismiss();
                break;
            //开始活动
            case R.id.rcrl_sign_dilog_start:
                eventBus.post(new SignFind());
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels - 150, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}