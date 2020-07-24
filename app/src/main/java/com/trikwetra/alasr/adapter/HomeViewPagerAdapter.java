package com.trikwetra.alasr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.trikwetra.alasr.R;
import com.trikwetra.alasr.bean.SelectedMosqueAdvBean;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder> {

    List<SelectedMosqueAdvBean> selectedMosqueAdvBeanList;
    Context context;
    ViewPager2 viewPager2;
    private Bitmap mBitmap;
    private float cornerRadius = 16.0f;

    public HomeViewPagerAdapter(Context context, ArrayList<SelectedMosqueAdvBean> selectedMosqueAdvBeanArrayList,
                                ViewPager2 vpSelMosAdv) {
        this.context = context;
        this.selectedMosqueAdvBeanList = selectedMosqueAdvBeanArrayList;
        this.viewPager2 = vpSelMosAdv;
    }

    @NonNull
    @Override
    public HomeViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_sel_mos_and_adv,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewPagerAdapter.ViewHolder holder, int position) {
        SelectedMosqueAdvBean listItem = selectedMosqueAdvBeanList.get(position);

        if(position == 0){
            holder.clSelMosDetails.setVisibility(View.VISIBLE);
            holder.clAdv.setVisibility(View.GONE);
            holder.tvMosName.setText(listItem.getMosqueName());
            holder.tvMosDistance.setText(listItem.getMosqueDistance());
            holder.tvMosTime.setText(listItem.getMosqueReachTime());

            mBitmap = BitmapFactory.decodeResource(context.getResources(),listItem.getAdvOrMosImage());
            RoundedBitmapDrawable roundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(),mBitmap);
            roundedBitmapDrawable.setCornerRadius(cornerRadius);
            roundedBitmapDrawable.setAntiAlias(true);
            holder.ivMosque.setImageDrawable(roundedBitmapDrawable);
        }
        else
        {
            holder.clSelMosDetails.setVisibility(View.GONE);
            holder.clAdv.setVisibility(View.VISIBLE);
            mBitmap = BitmapFactory.decodeResource(context.getResources(),listItem.getAdvOrMosImage());
            holder.ivAdv.setImageBitmap(mBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return selectedMosqueAdvBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout clSelMosDetails,clAdv;
        private ImageView ivAdv,ivMosque;
        private TextView tvMosName,tvMosDistance,tvMosTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clAdv = itemView.findViewById(R.id.cl_adv);
            clSelMosDetails = itemView.findViewById(R.id.cl_sel_mos_details);
            ivAdv = itemView.findViewById(R.id.iv_adv);
            tvMosName = itemView.findViewById(R.id.tv_mos_name);
            tvMosDistance = itemView.findViewById(R.id.tv_mos_distance);
            tvMosTime = itemView.findViewById(R.id.tv_mos_time);
            ivMosque = itemView.findViewById(R.id.iv_mosque);
        }
    }
}
