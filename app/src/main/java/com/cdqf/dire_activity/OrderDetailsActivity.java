package com.cdqf.dire_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_adapter.OrderDetailsAdapter;
import com.cdqf.dire_state.BaseActivity;
import com.cdqf.dire_state.DireState;
import com.cdqf.dire_state.StatusBarCompat;
import com.cdqf.dire_view.ListViewForScrollView;
import com.gcssloop.widget.RCRelativeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity {

    private String TAG = OrderDetailsActivity.class.getSimpleName();

    public static OrderDetailsActivity orderDetailsActivity = null;

    private Context context = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DireState direState = DireState.getDireState();

    private Gson gson = new Gson();

    //返回
    @BindView(R.id.rl_orderdetails_return)
    public RelativeLayout rlOrderdetailsReturn = null;

    //商品集合
    @BindView(R.id.lvsv_orderdetails_list)
    public ListViewForScrollView lvsvOrderdetailsList = null;

    private OrderDetailsAdapter orderDetailsAdapter = null;

    //商品价格
    @BindView(R.id.tv_orderdetails_allprice)
    public TextView tvOrderdetailsAllprice = null;

    //状态
    @BindView(R.id.tv_order_item_forpayment)
    public TextView tvOrderItemForpayment = null;

    //状态图
    @BindView(R.id.ll_order_item_one)
    public LinearLayout llOrderItemOne = null;

    //取消订单
    @BindView(R.id.rcrl_order_item_one)
    public RCRelativeLayout rcrl_order_item_one = null;

    //去支付
    @BindView(R.id.rcrl_order_item_three)
    public RCRelativeLayout rcrlOrderItemThree = null;

    //订单编号
    @BindView(R.id.tv_orderdetails_ordernumber)
    public TextView tvOrderdetailsOrdernumber = null;

    //支付宝订单号
    @BindView(R.id.tv_orderdetails_paynumber)
    public TextView tvOrderdetailsPaynumber = null;

    //创建时间
    @BindView(R.id.tv_orderdetails_creationtime)
    public TextView tvOrderdetailsCreationtime = null;

    //付款时间
    @BindView(R.id.tv_orderdetails_paymenttime)
    public TextView tvOrderdetailsPaymenttime = null;

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
        setContentView(R.layout.activity_orderdetails);

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

    }

    private void initView() {

    }

    private void initAdapter() {
        orderDetailsAdapter = new OrderDetailsAdapter(context);
        lvsvOrderdetailsList.setAdapter(orderDetailsAdapter);
    }

    private void initListener() {

    }

    private void initBack() {
        initPull();
    }

    private void initPull() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }

    private void initIntent(Class<?> activity, int position) {
        Intent intent = new Intent(context, activity);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick({R.id.rl_orderdetails_return, R.id.rcrl_order_item_one, R.id.rcrl_order_item_three})
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.rl_orderdetails_return:
                finish();
                break;
            //取消订单
            case R.id.rcrl_order_item_one:
                break;
            //去支付
            case R.id.rcrl_order_item_three:
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
