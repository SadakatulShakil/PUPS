package com.astronist.pups.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.CartItemListActivity;
import com.astronist.pups.Model.CartList;
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
import java.util.List;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.viewHolder> {
    private Context context;
    private ArrayList<CartList> cartArrayList;
    public static final String TAG = "CartAdapter";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    public boolean showShimmer = true;

    public UserCartAdapter(Context context, ArrayList<CartList> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public UserCartAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_view, parent, false);
        return new UserCartAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCartAdapter.viewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();

        }else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            final CartList cartList = cartArrayList.get(position);
            String pName = cartList.getProductName();
            String pDate = cartList.getDate();
            String quantity = cartList.getProductQuantity();
            String unit = cartList.getUnit();
            String price = cartList.getTotalPrice();
            String currency = cartList.getCurrency();
            String pTime = cartList.getTime();
            String mainDate = pDate + " : " + pTime;

            holder.productTitle.setBackground(null);
            holder.productTitle.setText(pName);
            holder.orderDate.setBackground(null);
            holder.orderDate.setText(mainDate);
            holder.category.setBackground(null);
            holder.amount.setBackground(null);
            holder.quantity.setText(quantity);
            holder.unit.setText(unit);
            holder.price.setText(price);
            holder.currency.setText(currency);
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

            holder.removeCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + "item clicked");

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Remove Cart Item !");
                    alertDialog.setMessage("Are You Sure To Remove Your Item ?");
                    alertDialog.setIcon(R.drawable.ic_dialog_cart);

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            user = firebaseAuth.getCurrentUser();

                            cartRef.child(user.getUid()).child(cartList.getPushId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(context, CartItemListActivity.class);
                                        context.startActivity(intent);
                                        ((Activity) context).finish();
                                        Toast.makeText(context, pName + " is Deleted from Cart ! ",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onComplete: " + cartList.getPushId());
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

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmer ? SHIMMER_ITEM_NUMBER : cartArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView productTitle, orderDate, quantity, price, unit, currency, removeCart;
        private LinearLayout category, amount;
        private ShimmerFrameLayout shimmerFrameLayout;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            orderDate = itemView.findViewById(R.id.orderDateTime);
            unit = itemView.findViewById(R.id.unit);
            quantity = itemView.findViewById(R.id.productQuantity);
            price = itemView.findViewById(R.id.productUnitPrice);
            currency = itemView.findViewById(R.id.currency);
            removeCart = itemView.findViewById(R.id.removeCart);
            category = itemView.findViewById(R.id.categoryLay);
            amount = itemView.findViewById(R.id.amountLay);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerItem);

        }
    }
}
