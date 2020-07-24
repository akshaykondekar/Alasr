package com.trikwetra.alasr.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.trikwetra.alasr.MosqueDetailsActivity;
import com.trikwetra.alasr.R;
import com.trikwetra.alasr.bean.MosqueBean;
import java.util.ArrayList;
import java.util.List;

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.ViewHolder> {

    Context context;
    List<MosqueBean> mosqueBeanList;
    private Bitmap mBitmap;
    float cornerRadius = 16.0f;

    public MosqueAdapter(ArrayList<MosqueBean> mosqueList, Context context) {
        this.mosqueBeanList = mosqueList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_mosque_details,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MosqueBean listItem = mosqueBeanList.get(position);

        mBitmap = (Bitmap) BitmapFactory.decodeResource(context.getResources(),
                R.drawable.mosque);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                context.getResources(),
                mBitmap
        );

        roundedBitmapDrawable.setCornerRadius(cornerRadius);
        roundedBitmapDrawable.setAntiAlias(true);
        holder.ivMosque.setImageDrawable(roundedBitmapDrawable);

        holder.tvMosName.setText(listItem.getMosqueName());
        holder.tvMosCity.setText(listItem.getMosqueCity());

        holder.clMosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailsActivity();
            }
        });

        holder.ivMosqueDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailsActivity();
            }
        });
    }

    private void openDetailsActivity() {
        context.startActivity(new Intent(context, MosqueDetailsActivity.class));
    }

    @Override
    public int getItemCount() {
        return mosqueBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivMosque,ivMosqueDetails;
        TextView tvMosName, tvMosCity;
        ConstraintLayout clMosque;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMosque = itemView.findViewById(R.id.iv_mosque);
            tvMosName = itemView.findViewById(R.id.tv_mos_name);
            tvMosCity = itemView.findViewById(R.id.tv_mos_city);
            clMosque = itemView.findViewById(R.id.cl_mosque);
            ivMosqueDetails = itemView.findViewById(R.id.iv_mosque_details);
        }
    }
}
