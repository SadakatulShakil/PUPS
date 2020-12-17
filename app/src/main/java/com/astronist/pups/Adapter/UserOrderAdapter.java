package com.astronist.pups.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Model.Order;
import com.astronist.pups.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.astronist.pups.OrderHistoryFragment.TAG;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.viewHolder> {
    private Context context;
    private ArrayList<Order> orderArrayList;
    private String currentMonth;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    public boolean showShimmer = true;

    public UserOrderAdapter(Context context, ArrayList<Order> orderArrayList, String currentMonth) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        this.currentMonth = currentMonth;
    }

    @NonNull
    @Override
    public UserOrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_view, parent, false);
        return new UserOrderAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.viewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerLayout.startShimmer();

        } else {
            holder.shimmerLayout.stopShimmer();
            holder.shimmerLayout.setShimmer(null);

            final Order orderInfo = orderArrayList.get(position);
            String pName = orderInfo.getProductName();
            String pDate = orderInfo.getDate();
            String quantity = orderInfo.getProductQuantity();
            String unit = orderInfo.getUnit();
            String price = orderInfo.getTotalPrice();
            String currency = orderInfo.getCurrency();
            String pTime = orderInfo.getTime();
            String mainDate = pDate + " : " + pTime;

            holder.productTitle.setText(pName);
            holder.productTitle.setBackground(null);
            holder.orderDate.setText(mainDate);
            holder.orderDate.setBackground(null);
            holder.quantity.setText(quantity);
            holder.category.setBackground(null);
            holder.unit.setText(unit);
            holder.amount.setBackground(null);
            holder.price.setText(price);
            holder.currency.setText(currency);
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Order").child(currentMonth);
            holder.removeCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    user = firebaseAuth.getCurrentUser();
                    cartRef.child(orderInfo.getPushId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, pName + " is Deleted from Order List ! ",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: " + orderInfo.getPushId());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        }
                    });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmer ? SHIMMER_ITEM_NUMBER : orderArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView productTitle, orderDate, quantity, price, unit, currency, removeCart;
        private ShimmerFrameLayout shimmerLayout;
        private LinearLayout category, amount;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            orderDate = itemView.findViewById(R.id.orderDateTime);
            unit = itemView.findViewById(R.id.unit);
            quantity = itemView.findViewById(R.id.productQuantity);
            price = itemView.findViewById(R.id.productUnitPrice);
            currency = itemView.findViewById(R.id.currency);
            removeCart = itemView.findViewById(R.id.removeCart);
            shimmerLayout = itemView.findViewById(R.id.shimmerItem);
            category = itemView.findViewById(R.id.categoryLay);
            amount = itemView.findViewById(R.id.amountLay);
        }
    }
}
