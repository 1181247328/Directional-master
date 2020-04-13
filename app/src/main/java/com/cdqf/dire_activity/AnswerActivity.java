package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_dilog.AnswerDilogFragment;
import com.cdqf.dire_find.AnswerFind;
import com.cdqf.dire_find.AnswerSumitFind;
import com.cdqf.dire_state.ACache;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.DirectAddaress;
import com.cdqf.dire_state.StatusBarCompat;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AnswerActivity extends BaseActivity {
    private String TAG = AnswerActivity.class.getSimpleName();

    private Context context = null;

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    private EventBus eventBus = EventBus.getDefault();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private ACache aCache = null;

    @BindView(R.id.srl_answer_pull)
    public SwipeRefreshLayout SwipeRefreshLayout = null;

    //返回
    @BindView(R.id.rl_answer_return)
    public RelativeLayout rlAnswerReturn = null;

    //标题
    @BindView(R.id.tv_answer_title)
    public TextView tvAnswerTitle = null;

    @BindView(R.id.sv_answer_pull)
    public ScrollView svAnswerPull = null;

    //图片
    @BindView(R.id.iv_answer_image)
    public ImageView ivAnswerImage = null;

    //题目
    @BindView(R.id.tv_answer_topic)
    public TextView tvAnswerTopic = null;

    //答案
    @BindView(R.id.tv_answer_one)
    public TextView tvAnswerOne = null;

    @BindView(R.id.tv_answer_two)
    public TextView tvAnswerTwo = null;

    @BindView(R.id.tv_answer_three)
    public TextView tvAnswerThree = null;

    @BindView(R.id.tv_answer_four)
    public TextView tvAnswerFour = null;

    private String ble = "";

    private String correct = "";

    private int type = 0;

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
        setContentView(R.layout.activity_answer);

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
        ButterKnife.bind(this);
        imageLoader = direState.getImageLoader(context);
        aCache = ACache.get(context);
        Intent intent = getIntent();
        ble = intent.getStringExtra("ble");
        type = intent.getIntExtra("type", 0);
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    private void initView() {

    }

    private void initAdapter() {

    }

    private void initListener() {
        svAnswerPull.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                SwipeRefreshLayout.setEnabled(svAnswerPull.getScrollY() == 0);
            }
        });
    }

    private void initBack() {
        SwipeRefreshLayout.setEnabled(false);
        for (int i = 0; i < direState.getDetailsGame().getAnswer().size(); i++) {
            if (TextUtils.equals(ble, direState.getDetailsGame().getAnswer().get(i).getBle())) {
                tvAnswerTitle.setText(direState.getDetailsGame().getAnswer().get(i).getTitle());
                imageLoader.displayImage(DirectAddaress.ADDRESS + direState.getDetailsGame().getImg(),
                        ivAnswerImage, direState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
                tvAnswerTopic.setText(direState.getDetailsGame().getAnswer().get(i).getTopic());
                tvAnswerOne.setText("A." + direState.getDetailsGame().getAnswer().get(i).getOne());
                tvAnswerTwo.setText("B." + direState.getDetailsGame().getAnswer().get(i).getTwo());
                tvAnswerThree.setText("C." + direState.getDetailsGame().getAnswer().get(i).getThree());
                tvAnswerFour.setText("D." + direState.getDetailsGame().getAnswer().get(i).getFour());
                correct = direState.getDetailsGame().getAnswer().get(i).getCorrect();
                break;
            }
        }
    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntentReward(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("live_id", direState.getDetailsGame().getId());
        startActivity(intent);
    }

    @OnClick({R.id.rl_answer_return, R.id.tv_answer_one, R.id.tv_answer_two, R.id.tv_answer_three, R.id.tv_answer_four})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_answer_return:
                finish();
                break;
            case R.id.tv_answer_one:
                AnswerDilogFragment answerDilogFragmentOne = new AnswerDilogFragment();
                answerDilogFragmentOne.setAnswer("A");
                answerDilogFragmentOne.show(getSupportFragmentManager(), "答题A");
                break;
            case R.id.tv_answer_two:
                AnswerDilogFragment answerDilogFragmentTwo = new AnswerDilogFragment();
                answerDilogFragmentTwo.setAnswer("B");
                answerDilogFragmentTwo.show(getSupportFragmentManager(), "答题B");
                break;
            case R.id.tv_answer_three:
                AnswerDilogFragment answerDilogFragmentThree = new AnswerDilogFragment();
                answerDilogFragmentThree.setAnswer("C");
                answerDilogFragmentThree.show(getSupportFragmentManager(), "答题C");
                break;
            case R.id.tv_answer_four:
                AnswerDilogFragment answerDilogFragmentFour = new AnswerDilogFragment();
                answerDilogFragmentFour.setAnswer("D");
                answerDilogFragmentFour.show(getSupportFragmentManager(), "答题D");
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
        eventBus.unregister(this);
    }

    public void onEventMainThread(AnswerFind a) {
        if (TextUtils.equals(a.correct, correct)) {
            direState.initToast(context, "回答正确", true, 0);
            for (int i = 0; i < direState.getDetailsGame().getAnswer().size(); i++) {
                if (TextUtils.equals(ble, direState.getDetailsGame().getAnswer().get(i).getBle())) {
                    direState.getDetailsGame().getAnswer().get(i).setSelete(true);
                    break;
                }
            }
            String details = gson.toJson(direState.getDetailsGame());
            Log.e(TAG, "---要保存的数据---" + details);
//            aCache.put("details" +type+ direState.getUser().getId() + direState.getDetailsGame().getId(), details);
            eventBus.post(new AnswerSumitFind());
            //确订是不是已经完成了所有的答题
            if (direState.getDetailsGame().getAnswer().get(direState.getDetailsGame().getAnswer().size() - 1).isSelete()) {
                initIntentReward(RewardActivity.class);
                DetailsGameActivity.detailsGameActivity.finish();
            }
            finish();
        } else {
            direState.initToast(context, "回答错误", true, 0);
        }
    }
}
