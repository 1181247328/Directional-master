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
import com.cdqf.dire_find.LineCompleteFind;
import com.cdqf.dire_find.LinePositionFind;
import com.cdqf.dire_find.LoginCancelFind;
import com.cdqf.dire_find.MainLoginFind;
import com.cdqf.dire_find.MyContactFind;
import com.cdqf.dire_find.PersonalReturnFind;
import com.cdqf.dire_find.PersonalSaveFind;
import com.cdqf.dire_find.PlayersAddFind;
import com.cdqf.dire_find.PlayersDeleteTwoFind;
import com.cdqf.dire_find.RouteLoginFind;
import com.cdqf.dire_find.SearchFind;
import com.cdqf.dire_state.DataCleanManager;
import com.cdqf.dire_state.DireState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 疑问对话框
 */
public class WhyDilogFragment extends DialogFragment {

    private String TAG = WhyDilogFragment.class.getSimpleName();

    private View view = null;

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private String title;

    private String context;

    private String cancel;

    private String determine;

    private int type = 0;

    private int position = 0;

    //标题
    @BindView(R.id.tv_why_dilog_title)
    public TextView tvWhyDilogTitle = null;

    //内容
    @BindView(R.id.tv_why_dilog_context)
    public TextView tvWhyDilogContext = null;

    //取消
    @BindView(R.id.tv_why_dilog_cancel)
    public TextView tvWhyDilogCancel = null;

    //确定
    @BindView(R.id.tv_why_dilog_determine)
    public TextView tvWhyDilogDetermine = null;

    public void setInit(int type, String title, String context, String cancel, String determine) {
        this.type = type;
        this.title = title;
        this.context = context;
        this.cancel = cancel;
        this.determine = determine;
    }

    public void setInit(int type, String title, String context, String cancel, String determine, int position) {
        this.type = type;
        this.title = title;
        this.context = context;
        this.cancel = cancel;
        this.determine = determine;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_why, null);
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
        tvWhyDilogTitle.setText(title);
        tvWhyDilogContext.setText(context);
        tvWhyDilogCancel.setText(cancel);
        tvWhyDilogDetermine.setText(determine);
    }

    @OnClick({R.id.tv_why_dilog_cancel, R.id.tv_why_dilog_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_why_dilog_cancel:
                dismiss();
                break;
            case R.id.tv_why_dilog_determine:
                switch (type) {
                    //MainActivty是否登录
                    case 0:
                        eventBus.post(new MainLoginFind());
                        break;
                    //MyActivity退出登录
                    case 1:
                        eventBus.post(new LoginCancelFind());
                        break;
                    //PersonalActivity返回
                    case 2:
                        eventBus.post(new PersonalReturnFind());
                        break;
                    //PersonalActivity保存
                    case 3:
                        eventBus.post(new PersonalSaveFind());
                        break;
                    //MyActivity联系我们
                    case 4:
                        eventBus.post(new MyContactFind());
                        break;
                    //RouteActivity前往登录
                    case 5:
                        eventBus.post(new RouteLoginFind());
                        break;
                    //LineActivity选择路线
                    case 6:
                        eventBus.post(new LinePositionFind(position));
                        break;
                    //LineActivity选择路线
                    case 7:
                        eventBus.post(new LineCompleteFind(position));
                        break;
                    //MyActivity
                    case 8:
                        DataCleanManager.clearAllCache(getContext());
                        direState.initToast(getContext(), "已全部清除", true, 0);
                        break;
                    //PlayersActivity添加队员
                    case 9:
                        eventBus.post(new PlayersAddFind());
                        break;
                    //PlayersActivity删除队员
                    case 10:
                        eventBus.post(new PlayersDeleteTwoFind(position));
                        break;
                    //SearchActivity搜索中会馆同意前往
                    case 11:
                        eventBus.post(new SearchFind(position));
                        break;
                }
                break;
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels / 2 + 100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
