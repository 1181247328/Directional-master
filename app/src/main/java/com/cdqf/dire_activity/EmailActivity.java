package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.EmailAdapter;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_utils.HttpRequestWrap;
import com.google.gson.Gson;
import com.xw.repo.XEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 电子邮箱
 */
public class EmailActivity extends BaseActivity {
    private String TAG = EmailActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private HttpRequestWrap httpRequestWrap = null;

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    //返回
    @BindView(R.id.rl_email_return)
    public RelativeLayout rlEmailReturn = null;

    //确定
    @BindView(R.id.tv_email_determine)
    public TextView tvEmailDetermine = null;

    //输入信息
    @BindView(R.id.xet_email_number)
    public XEditText xetEmailNumber = null;

    @BindView(R.id.lv_email_list)
    public ListView lvEmailList = null;

    private String[] email = {
            "@qq.com",
            "@163.com",
            "@126.com",
    };

    private EmailAdapter emailAdapter = null;

    private List<String> emailList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //API19以下用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //加载布局
        setContentView(R.layout.activity_email);

        //API>=20以上用于沉侵式菜单栏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //沉侵
            StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.black));
        }

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();
    }

    private void initAgo() {
        context = this;
        httpRequestWrap = new HttpRequestWrap(context);
        ButterKnife.bind(this);
    }

    private void initView() {

    }

    private void initAdapter() {
        emailAdapter = new EmailAdapter(context);
        lvEmailList.setAdapter(emailAdapter);
    }

    private void initListener() {
        lvEmailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xetEmailNumber.setText(emailList.get(position));
                emailList.clear();
                emailAdapter.setEmailList(emailList);
            }
        });
        xetEmailNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailList.clear();
                String name = xetEmailNumber.getText().toString();
                for (int i = 0; i < email.length; i++) {
                    emailList.add(name + email[i]);
                }
                emailAdapter.setEmailList(emailList);
            }
        });
    }

    private void initBack() {
        for (int i = 0; i < email.length; i++) {
            emailList.add(email[i]);
        }
        emailAdapter.setEmailList(emailList);
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    @OnClick({R.id.rl_email_return, R.id.tv_email_determine})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_email_return:
                finish();
                break;
            //确定
            case R.id.tv_email_determine:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "---启动---");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "---恢复---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
    }
}
