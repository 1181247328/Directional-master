package com.cdqf.dire_dilog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_state.DireState;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 语音播放器
 */
public class ForDilogFragment extends DialogFragment {
    private String TAG = ForDilogFragment.class.getSimpleName();

    private View view = null;

    private DireState plantState = DireState.getDireState();

    private EventBus eventBus = EventBus.getDefault();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private String path = "http://zwyfile.quanyubao.cn//ScenicSpots/UpdateScenicSpots//Voice/2017/12/22/201712221641342646.MP3";

    //中文
    private String httpVoice = "";

    //英文
    private String englishvoice = "";

    //true = 播放,false = 暂停
    private boolean isPlay = true;

    //是不是在播放中按了暂停
    private boolean isPause = false;

    private boolean isChina = true;

    private MediaPlayer mediaPlayer = null;

    //图片
    @BindView(R.id.iv_for_dilog_price)
    public ImageView ivForDilogPrice = null;

    //名称
    @BindView(R.id.iv_for_dilog_name)
    public TextView ivForDilogName = null;

    //控制
    @BindView(R.id.iv_for_dilog_control)
    public ImageView ivForDilogControl = null;

    //中文
    @BindView(R.id.tv_for_dilog_china)
    public TextView tvForDilogChina = null;

    //英文
    @BindView(R.id.tv_for_dilog_enhigt)
    public TextView tvForDilogEnhigt = null;

    public void setPath(String path) {
        this.path = path;
    }

    //图片
    public void setVoice(String httpVoice) {
        this.httpVoice = httpVoice;
    }

    public void setEnglishvoice(String englishvoice) {
        this.englishvoice = englishvoice;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        view = inflater.inflate(R.layout.dilog_for, null);
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
        imageLoader = plantState.getImageLoader(getContext());
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
        //点击外部不消失
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isPlay) {
                    plantState.initToast(getContext(), "请关闭正在播放的音频", true, 0);
                }
            }
        });

    }

    /**
     * 初始化后
     */
    private void initBack() {
//        ivForDilogName.setText(plantState.getForDetails().getSpotName());
        imageLoader.displayImage(path, ivForDilogPrice, plantState.getImageLoaderOptions(R.mipmap.not_loaded, R.mipmap.not_loaded, R.mipmap.not_loaded));
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels - 100, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @OnClick({R.id.iv_for_dilog_control, R.id.tv_for_dilog_china, R.id.tv_for_dilog_enhigt})
    public void onClick(View v) {
        switch (v.getId()) {
            //播放
            case R.id.iv_for_dilog_control:
                if (mediaPlayer != null && isPlay) {
                    isPlay = false;
                    mediaPlayer.start();
//                    ivForDilogControl.setImageResource(R.mipmap.music_pasue);
                    return;
                }
                //播放
                if (isPlay) {
                    getDialog().setCanceledOnTouchOutside(false);
                    isPlay = false;
//                    ivForDilogControl.setImageResource(R.mipmap.music_pasue);
                    try {
                        mediaPlayer = new MediaPlayer();
                        if (isChina) {
                            mediaPlayer.setDataSource(httpVoice);
                        } else {
                            mediaPlayer.setDataSource(englishvoice);
                        }
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.prepareAsync();
                        //装载完毕回调
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                            }
                        });
                        //声音播放
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                Log.e(TAG, "---播放完毕音乐----");
                                //播放完音乐
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                                isPlay = true;
                                getDialog().setCanceledOnTouchOutside(true);
//                                ivForDilogControl.setImageResource(R.mipmap.music_play);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    isPause = true;
                    isPlay = true;
                    getDialog().setCanceledOnTouchOutside(true);
//                    ivForDilogControl.setImageResource(R.mipmap.music_play);
                    //暂停
                    mediaPlayer.pause();
                }
                break;
            //中文
            case R.id.tv_for_dilog_china:
                if (!isPlay) {
                    plantState.initToast(getContext(), "请暂停当前播放的内容", true, 0);
                    return;
                }
                isChina = true;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlay = true;
                }
                tvForDilogChina.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tvForDilogChina.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.strategy_item_published));
                tvForDilogEnhigt.setTextColor(ContextCompat.getColor(getContext(), R.color.strategy_item_published));
                tvForDilogEnhigt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                break;
            case R.id.tv_for_dilog_enhigt:
                if (!isPlay) {
                    plantState.initToast(getContext(), "请暂停当前播放的内容", true, 0);
                    return;
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    isPlay = true;
                }
                isChina = false;
                tvForDilogChina.setTextColor(ContextCompat.getColor(getContext(), R.color.strategy_item_published));
                tvForDilogChina.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                tvForDilogEnhigt.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tvForDilogEnhigt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.strategy_item_published));
                break;
        }
    }
}
