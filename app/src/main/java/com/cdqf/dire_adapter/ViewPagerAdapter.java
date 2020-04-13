package com.cdqf.dire_adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;

    private int mChildCount = 0;

    public ViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。  
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//删除页卡  
    }


    @Override
    public View instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(mListViews.get(position));//添加页卡
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mListViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if(mChildCount > 0){
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
