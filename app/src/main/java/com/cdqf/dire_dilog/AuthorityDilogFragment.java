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
import android.widget.AdapterView;
import android.widget.ListView;

import com.cdqf.dire.R;
import com.cdqf.dire_dilgadapter.AuthorityAdapter;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 队长与队员权限对话框
 */
public class AuthorityDilogFragment extends DialogFragment {

    private String TAG = AuthorityDilogFragment.class.getSimpleName();

    private View view = null;

    //逻辑层
    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    //内容
    @BindView(R.id.lv_personal_dilog_context)
    public ListView lvPersonalDilogContext = null;

    private AuthorityAdapter authorityAdapter = null;

    //取消
    @BindView(R.id.rcrl_personal_dilog_cancel)
    public RCRelativeLayout rcrlPersonalDilogCancel = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.dilog_hear, null);
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
        authorityAdapter = new AuthorityAdapter(getContext());
        lvPersonalDilogContext.setAdapter(authorityAdapter);
    }

    /**
     * 注册监听器
     */
    private void initListener() {
        lvPersonalDilogContext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    /**
     * 初始化后
     */
    private void initBack() {
        getDialog().setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.rcrl_personal_dilog_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.rcrl_personal_dilog_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels - 60, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
