package com.astronist.pups.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Model.Product;
import com.astronist.pups.R;

import java.util.ArrayList;

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.viewHolder> {
    private Context context;
    private ArrayList<Product> specialArrayList;

    public SpecialAdapter(Context context, ArrayList<Product> specialArrayList) {
        this.context = context;
        this.specialArrayList = specialArrayList;
    }

    @NonNull
    @Override
    public SpecialAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view, parent, false);
        return new SpecialAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialAdapter.viewHolder holder, int position) {
        final Product product = specialArrayList.get(position);
        String title = product.getTitleName();
        String price = product.getPrice();
        String status = product.getStatus();

        holder.productImage.setImageResource(product.getProfileImage());
        holder.productName.setText(title);
        holder.productPrice.setText(price);
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice, status;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productUnitPrice);
            status = itemView.findViewById(R.id.availability);

        }
    }
}
