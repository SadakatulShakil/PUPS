package com.astronist.pups.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Model.SlideItem;
import com.astronist.pups.OrderConfirmationActivity;
import com.astronist.pups.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.viewHolder> {

    private Context context;
    private ArrayList<SlideItem> slideItemArrayList;

    public SliderAdapter(Context context, ArrayList<SlideItem> slideItemArrayList) {
        this.context = context;
        this.slideItemArrayList = slideItemArrayList;
    }

    @NonNull
    @Override
    public SliderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_product_view, parent, false);
        return new SliderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.viewHolder holder, int position) {
        final SlideItem  slideItem = slideItemArrayList.get(position);
        String title = slideItem.getTitleName();
        String price = slideItem.getPrice();
        String status = slideItem.getStatus();
        String unit = slideItem.getUnit();
        String currency = slideItem.getCurrency();

        holder.sliderImage.setImageResource(slideItem.getProfileImage());
        holder.productName.setText(title);
        holder.productPrice.setText(price);
        holder.status.setText(status);
        holder.unit.setText(unit);
        holder.currency.setText(currency);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderConfirmationActivity.class);
                intent.putExtra("itemInfoSd", slideItem);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return slideItemArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView sliderImage;
        private TextView productName, productPrice, status, unit, currency;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.image_slider);
            productName = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productUnitPrice);
            status = itemView.findViewById(R.id.availability);
            unit = itemView.findViewById(R.id.unit);
            currency = itemView.findViewById(R.id.currency);
        }
    }
}
