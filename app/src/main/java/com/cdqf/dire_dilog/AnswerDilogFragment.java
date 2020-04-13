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
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.AnswerFind;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 选择了答案
 */
public class AnswerDilogFragment extends DialogFragment {

    private String TAG = LocationDilogFragment.class.getSimpleName();

    private EventBus eventBus = EventBus.getDefault();

    private View view = null;

    private int position = 0;

    private String title = "";

    private int tag = 0;

    private DireState direState = DireState.getDireState();

    private String answer = "";

    //内容
    @BindView(R.id.tv_route_dilog_context)
    public TextView tvRouteDilogContext = null;

    //取消
    @BindView(R.id.tv_route_dilog_cancel)
    public RCRelativeLayout tvRouteDilogCancel = null;

    //确定
    @BindView(R.id.tv_route_dilog_determine)
    public RCRelativeLayout tvRouteDilogDetermine = null;

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_answer, null);
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
        tvRouteDilogContext.setText("您选择的答案为" + answer + ",是否确定");
    }

    @OnClick({R.id.tv_route_dilog_cancel, R.id.tv_route_dilog_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_route_dilog_cancel:
                dismiss();
                break;
            //确定
            case R.id.tv_route_dilog_determine:
                eventBus.post(new AnswerFind(answer));
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels / 2 + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
