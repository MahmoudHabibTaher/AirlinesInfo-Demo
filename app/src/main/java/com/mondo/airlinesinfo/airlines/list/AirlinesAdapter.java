package com.mondo.airlinesinfo.airlines.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mondo.airlinesinfo.R;
import com.mondo.airlinesinfo.airlines.data.Airline;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class AirlinesAdapter extends RecyclerView.Adapter<AirlinesAdapter.AirlineViewHolder> {

    private static final String TAG = AirlinesAdapter.class.getSimpleName();

    private Context mContext;
    private List<Airline> mAirlines;
    private ItemListener mItemListener;

    public AirlinesAdapter(Context context, List<Airline> airlines) {
        mContext = context;
        mAirlines = airlines;
    }

    @Override
    public AirlineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AirlineViewHolder(LayoutInflater.from(mContext).inflate(R.layout
                .layout_airline_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AirlineViewHolder holder, int position) {
        final Airline airline = mAirlines.get(position);

        holder.mAirlineNameTextView.setText(airline.getName());
        Glide.with(mContext).load(airline.getLogo()).into(holder.mLogoImageView);

        if (airline.isFavorite()) {
            holder.mFavoriteImageView.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.mFavoriteImageView.setImageResource(R.drawable.ic_favorite_normal);
        }

        holder.mFavoriteImageView.setOnClickListener(view -> {
            if (mItemListener != null) {
                mItemListener.onItemFavoriteClick(airline);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            if (mItemListener != null) {
                mItemListener.onItemSelected(airline);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAirlines.size();
    }

    public void setItemListener(ItemListener listener) {
        mItemListener = listener;
    }

    public interface ItemListener {
        void onItemSelected(@NonNull Airline airline);

        void onItemFavoriteClick(@NonNull Airline airline);
    }

    public static class AirlineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.airline_name_text_view)
        TextView mAirlineNameTextView;

        @BindView(R.id.airline_logo_image_view)
        ImageView mLogoImageView;

        @BindView(R.id.airline_favorite_image_view)
        ImageView mFavoriteImageView;

        public AirlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
