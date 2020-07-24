package com.trikwetra.alasr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.trikwetra.alasr.R;
import com.trikwetra.alasr.bean.MosqueBean;
import com.trikwetra.alasr.bean.PrayerBean;
import java.util.ArrayList;
import java.util.List;

public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.ViewHolder>
{
    Context context;
    List<PrayerBean> prayerBeans;
    private Boolean isVolumeOff = false;

    public PrayerAdapter(ArrayList<PrayerBean> prayerList, Context context) {
        this.context = context;
        this.prayerBeans = prayerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_prayer_details,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PrayerBean listItem = prayerBeans.get(position);

        holder.tvPrayerName.setText(listItem.getPrayerName());
        holder.tvPrayerTime.setText(listItem.getPrayerTime());
        holder.tvTimeRemaining.setText(listItem.getPrayerRemainigTime());
        holder.tvPrayerTotalTime.setText(listItem.getPrayerTotalTime());
        holder.ibtnVolume.setBackgroundResource(R.drawable.ic_volume_off);

        holder.ibtnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isVolumeOff){
                    holder.ibtnVolume.setBackgroundResource(R.drawable.ic_volume_off);
                }else{
                    holder.ibtnVolume.setBackgroundResource(R.drawable.ic_volume_on);
                }

                isVolumeOff = !isVolumeOff; // reverse
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return prayerBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrayerName,tvPrayerTime,tvTimeRemaining,tvPrayerTotalTime;
        private ImageButton ibtnVolume;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPrayerName = itemView.findViewById(R.id.tv_prayer_name);
            tvPrayerTime = itemView.findViewById(R.id.tv_prayer_time);
            tvTimeRemaining = itemView.findViewById(R.id.tv_time_remaining);
            tvPrayerTotalTime = itemView.findViewById(R.id.tv_prayer_total_time);
            ibtnVolume = itemView.findViewById(R.id.ibtn_volume);
        }
    }
}
