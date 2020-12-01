package com.astronist.pups.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Model.Order;
import com.astronist.pups.R;

import java.util.ArrayList;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.viewHolder> {
    private Context context;
    private ArrayList<Order> orderArrayList;

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
        final Order orderInfo = orderArrayList.get(position);
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

        holder.productTitle.setText(pName);
        holder.orderDate.setText(pDate);
        holder.productCategory.setText(pCategory);
        holder.quantity.setText(pQuantity);
        holder.price.setText(pTotalPrice);
        holder.location.setText(pLocation);
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView productTitle, orderDate, productCategory, quantity, price, location;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.titleTv);
            orderDate = itemView.findViewById(R.id.orderDate);
            productCategory = itemView.findViewById(R.id.productCategory);
            quantity = itemView.findViewById(R.id.productQuantity);
            price = itemView.findViewById(R.id.totalPrice);
            location = itemView.findViewById(R.id.location);

        }
    }
}
