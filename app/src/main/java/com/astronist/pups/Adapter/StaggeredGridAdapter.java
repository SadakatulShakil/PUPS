package com.astronist.pups.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Model.Order;
import com.astronist.pups.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Random;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.viewHolder> {
    private Context context;
    private ArrayList<Order> orderArrayList;
    public boolean showShimmer = true;

    public StaggeredGridAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public StaggeredGridAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_view, parent, false);
        return new StaggeredGridAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredGridAdapter.viewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerLayout.startShimmer();

        }else{
            holder.shimmerLayout.stopShimmer();
            holder.shimmerLayout.setShimmer(null);

            final Order orderInfo = orderArrayList.get(position);
            Random rnd = new Random();
            int currentColor = Color.argb(104, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            holder.productLay.setBackgroundColor(currentColor);

            String pName = orderInfo.getProductName();
            String pDate = orderInfo.getDate();
            String pCategory = orderInfo.getCategory();
            String quantity = orderInfo.getProductQuantity();
            String unit = orderInfo.getUnit();
            String pQuantity = quantity+" "+unit;
            String price = orderInfo.getTotalPrice();
            String currency = orderInfo.getCurrency();
            String pTotalPrice = price+" "+currency;
            String pLocation = orderInfo.getLocation();

            holder.productTitle.setBackground(null);
            holder.productTitle.setText(pName);
            holder.location.setWidth(200);

            holder.demoDate.setBackground(null);
            holder.demoDate.setText("Order date");
            holder.demoDate.setPadding(0, 0, 0, 0);
            holder.orderDate.setBackground(null);
            holder.orderDate.setText(pDate);

            holder.demoCategory.setBackground(null);
            holder.demoCategory.setText("Category");
            holder.demoCategory.setPadding(0, 0, 0, 0);
            holder.productCategory.setBackground(null);
            holder.productCategory.setText(pCategory);

            holder.demoQuantity.setBackground(null);
            holder.demoQuantity.setText("Quantity");
            holder.demoQuantity.setPadding(0, 0, 0, 0);
            holder.quantity.setBackground(null);
            holder.quantity.setText(pQuantity);

            holder.demoPrice.setBackground(null);
            holder.demoPrice.setPadding(0, 0, 0, 0);
            holder.demoPrice1.setText("Total price :");
            holder.price.setText(pTotalPrice);

            holder.demoLocation.setBackground(null);
            holder.demoLocation.setText("Location");
            holder.demoLocation.setPadding(0, 0, 0, 0);
            holder.location.setBackground(null);
            holder.location.setWidth(200);
            holder.location.setText(pLocation);

            holder.deliveryStatus.setBackground(null);
            if(orderInfo.getDeliveryStatus().equals("Success")){
                holder.deliveryStatus.setImageResource(R.drawable.success);
            }else if(orderInfo.getDeliveryStatus().equals("Pending")){
                holder.deliveryStatus.setImageResource(R.drawable.pending);
            }

        }
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmer ? SHIMMER_ITEM_NUMBER : orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView productTitle, orderDate, productCategory, quantity, price, location, demoDate, demoCategory, demoPrice1, demoQuantity, demoLocation;
        private LinearLayout productLay, demoPrice;
        private ShimmerFrameLayout shimmerLayout;
        private ImageView deliveryStatus;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.titleTv);
            orderDate = itemView.findViewById(R.id.orderDate);
            productCategory = itemView.findViewById(R.id.productCategory);
            quantity = itemView.findViewById(R.id.productQuantity);
            price = itemView.findViewById(R.id.totalPrice);
            location = itemView.findViewById(R.id.location);
            productLay = itemView.findViewById(R.id.linearLay);
            shimmerLayout = itemView.findViewById(R.id.shimmerItem);
            demoDate = itemView.findViewById(R.id.demoDate);
            demoCategory = itemView.findViewById(R.id.demoCategory);
            demoQuantity = itemView.findViewById(R.id.demoQuantity);
            demoPrice = itemView.findViewById(R.id.demoPrice);
            demoLocation = itemView.findViewById(R.id.demoLocation);
            demoPrice1 = itemView.findViewById(R.id.demoPrice1);
            deliveryStatus = itemView.findViewById(R.id.demoStatus);

        }
    }
}
