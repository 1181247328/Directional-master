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
import android.widget.ListView;

import com.cdqf.dire.R;
import com.cdqf.dire_dilgadapter.ScenicDilogAdapter;
import com.cdqf.dire_state.DireState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 搜索景区列表
 */
public class ScenicDilogFragment extends DialogFragment {

    //当前打印名称
    private String TAG = ScenicDilogFragment.class.getSimpleName();

    //事件通用总线(用的第三方)
    private EventBus eventBus = EventBus.getDefault();

    //中间逻辑层
    private DireState direState = DireState.getDireState();

    //布局view
    private View view = null;

    /*****本页组件注册********/
    //景区内容
    @BindView(R.id.lv_scenic_item_list)
    public ListView lvScenicItemList = null;

    //景区内容适配器
    private ScenicDilogAdapter scenicDilogAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.dilog_scenic, null);
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
        scenicDilogAdapter = new ScenicDilogAdapter(getContext());
        lvScenicItemList.setAdapter(scenicDilogAdapter);
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

    @OnClick({})
    public void onClick(View v) {

    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, dm.heightPixels / 2);
    }
}
