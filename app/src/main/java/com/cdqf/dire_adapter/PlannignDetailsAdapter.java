package com.cdqf.dire_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdqf.dire.R;
import com.cdqf.dire_class.Ble;
import com.cdqf.dire_find.PlannignFind;
import com.cdqf.dire_state.DireState;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 规划路线适配器
 * Created by liu on 2017/10/24.
 */

public class PlannignDetailsAdapter extends RecyclerView.Adapter<PlannignDetailsAdapter.ViewHolder> {

    private String TAG = PlannignDetailsAdapter.class.getSimpleName();

    private Context context = null;

    private EventBus eventBus = EventBus.getDefault();

    private DireState direState = DireState.getDireState();

    private Map<Integer, Ble> bleMapList = new HashMap<>();

//    private RoadDaetails roadDaetails = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();

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

    public PlannignDetailsAdapter(Context context) {
        this.context = context;
        imageLoader = direState.getImageLoader(context);
//        roadDaetails = new RoadDaetails();
    }

    public void setBleMapList(Map<Integer, Ble> bleMapList) {
        this.bleMapList = bleMapList;
        notifyDataSetChanged();
    }

    //    public PlannignDetailsAdapter(Context context, RoadDaetails roadDaetails) {
//        this.context = context;
//        this.roadDaetails = roadDaetails;
//        imageLoader = plantState.getImageLoader(context);
//    }
//
//    public void setRoadDaetails(RoadDaetails roadDaetails){
//        this.roadDaetails = roadDaetails;
//        notifyDataSetChanged();
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_planningdetails, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        imageLoader.displayImage(roadDaetails.getList().get(position).getHttpPic(),holder.imageView,plantState.getImageLoaderOptions(R.mipmap.not_loaded,R.mipmap.not_loaded,R.mipmap.not_loaded));
        holder.textView.setText(name[position]);
        holder.imageView.setImageResource(image[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (direState.getBleMapList().get(position).isTitle()) {
                    eventBus.post(new PlannignFind(position));
                } else {
                    direState.initToast(context, "请先完成前面的步骤", true, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
//        return roadDaetails.getList().size();
        return direState.getBleMapList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_planningdetails_item_pituce);
            textView = view.findViewById(R.id.tv_planningdetails_item_names);
        }
    }
}
