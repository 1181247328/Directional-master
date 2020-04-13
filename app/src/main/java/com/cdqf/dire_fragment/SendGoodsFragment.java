package com.cdqf.dire_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cdqf.dire.R;
import com.cdqf.dire_activity.OrderDetailsActivity;
import com.cdqf.dire_adapter.SendGoodsAdapter;
import com.cdqf.dire_state.DireState;
import com.google.gson.Gson;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 已支付
 */
public class SendGoodsFragment extends Fragment implements View.OnClickListener {

    private String TAG = SendGoodsFragment.class.getSimpleName();

    private View view = null;

    private DireState direState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private int pageIndex = 0;

    private Gson gson = new Gson();

    //无
    @BindView(R.id.ll_sendgoods_there)
    public LinearLayout llSendGoodsThere = null;

    //存在
    @BindView(R.id.ll_sendgoods_are)
    public LinearLayout llSendGoodsAre = null;

    //刷新器
    @BindView(R.id.ptrl_sendgoods_pull)
    public PullToRefreshLayout ptrlSendGoodsPull = null;

    private ListView llSendGoodsList = null;

    private SendGoodsAdapter sendGoodsAdapter = null;

    private int payPosition;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    llSendGoodsThere.setVisibility(View.GONE);
                    ptrlSendGoodsPull.setVisibility(View.VISIBLE);
                    break;
                case 0x01:
                    llSendGoodsThere.setVisibility(View.VISIBLE);
                    ptrlSendGoodsPull.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sendgoods, null);

        initAgo();

        initView();

        initAdapter();

        initListener();

        initBack();

        return view;
    }

    private void initAgo() {
        ButterKnife.bind(this, view);
    }

    private void initView() {
        llSendGoodsList = (ListView) ptrlSendGoodsPull.getPullableView();
    }

    private void initAdapter() {
        sendGoodsAdapter = new SendGoodsAdapter(getContext());
        llSendGoodsList.setAdapter(sendGoodsAdapter);
    }

    private void initListener() {
        ptrlSendGoodsPull.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 上拉加载操作

            }
        });
        llSendGoodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initIntent(OrderDetailsActivity.class);
            }
        });
    }

    private void initBack() {
        initPull();
    }

    private void initPull() {

    }

    private void initIntent(Class<?> activity) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "---暂停---");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "---停止---");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "---重启---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---销毁---");
    }
}
