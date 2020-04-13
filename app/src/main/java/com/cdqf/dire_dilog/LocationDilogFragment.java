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
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_find.DetailsFind;
import com.cdqf.dire_state.DireState;
import com.gcssloop.widget.RCRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class LocationDilogFragment extends DialogFragment {

    private String TAG = LocationDilogFragment.class.getSimpleName();

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private int position = 0;

    private View view = null;

    private int[] image = {
            R.mipmap.test_image_one,
            R.mipmap.test_image_two,
            R.mipmap.test_image_three,
    };

    private String[] name = {
            "英名廊",
            "烈士群雕",
            "突破湘江纪念碑",
    };

    @BindView(R.id.tv_location_dilog_name)
    public TextView tvLocationDilogName = null;

    @BindView(R.id.iv_location_dilog_back)
    public ImageView ivLocationDilogBack = null;

    @BindView(R.id.rcrl_location_dilog_three)
    public RCRelativeLayout rcrlLocationDilogThree = null;

    @BindView(R.id.rcrl_location_dilog_five)
    public RCRelativeLayout rcrlLocationDilogFive = null;

    public void setPosition(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.dilog_location, null);
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
        tvLocationDilogName.setText(name[position]);
        ivLocationDilogBack.setImageResource(image[position]);
    }

    @OnClick({R.id.rcrl_location_dilog_five, R.id.rcrl_location_dilog_three})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rcrl_location_dilog_five:
                if ((position + 1) == direState.getBleMapList().size()) {

                } else {
                    if (position + 1 == 1) {
                        direState.getTitleList().put(position, true);
                    }
                    direState.getTitleList().put(position + 1, true);
                    direState.getBleMapList().get(position + 1).setTitle(true);
                }
                dismiss();
                break;
            case R.id.rcrl_location_dilog_three:
                eventBus.post(new DetailsFind(position));
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
