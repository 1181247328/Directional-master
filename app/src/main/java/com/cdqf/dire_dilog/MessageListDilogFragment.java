package com.cdqf.dire_dilog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 其它选择对话框
 */
public class MessageListDilogFragment extends DialogFragment {

    private String TAG = PayDilogFragment.class.getSimpleName();

    private View view = null;

    private DireState plantState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private int pay = 0;

    private int payPosition = 0;

    private int type = 0;

    private double price = 0;

    //0=立即购买1=全部支付2=待付款
    public void initPayPrice(int type, double price) {
        this.type = type;
        this.price = price;
    }

    public void initPay(int type, double position) {
        this.type = type;
    }

    public void initPayPricePosition(int type, double price, int position) {
        this.type = type;
        this.price = price;
        this.payPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.dilog_message, null);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        //初始化前
        initAgo();

        //初始化控件
        initView();

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

    }

    /**
     * 初始化控件
     */
    private void initView() {

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

    }


    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
