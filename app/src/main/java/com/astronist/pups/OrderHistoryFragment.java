package com.astronist.pups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astronist.pups.Adapter.StaggeredGridAdapter;
import com.astronist.pups.Adapter.UserOrderAdapter;
import com.astronist.pups.Model.CartList;
import com.astronist.pups.Model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderHistoryFragment extends Fragment {

    private Context context;
    private String currentMonthName;
    private DatabaseReference orderListReference;
    private RecyclerView orderListRecView;
    private UserOrderAdapter userOrderAdapter;
    private ProgressBar progressBar;
    private ArrayList<Order> mOrderArrayList = new ArrayList<>();
    private LinearLayoutManager manager;
    public static final String TAG = "Order";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private RelativeLayout cartItemLay;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private DatabaseReference cartReference;
    private TextView cartItemCount;
    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //progressBar.setVisibility(View.VISIBLE);
        getCartItemCount();
        Calendar calendar = Calendar.getInstance();
        currentMonthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        orderListRecView.setLayoutManager(manager);
        userOrderAdapter = new UserOrderAdapter(context, mOrderArrayList, currentMonthName);
        orderListRecView.setAdapter(userOrderAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderDetails();
                userOrderAdapter.showShimmer = false;
                //staggeredGridAdapter.notifyDataSetChanged();
            }
        },2000);

        cartItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CartItemListActivity.class);
                context.startActivity(intent);
            }
        });



    }

    private void getCartItemCount() {
        String userId = user.getUid();
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child(userId);

        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);
                    Log.d(TAG, "onDataChange: "+ cartListArrayList.size());
                    if(cartListArrayList.size()>=1){
                        cartItemCount.setVisibility(View.VISIBLE);
                        String cartCount = String.valueOf(cartListArrayList.size());
                        cartItemCount.setText(cartCount);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getOrderDetails() {
        Calendar calendar = Calendar.getInstance();
        currentMonthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        orderListReference = FirebaseDatabase.
                getInstance().
                getReference("Order")
                .child(currentMonthName);
        orderListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mOrderArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Order orderInfo = userSnapshot.getValue(Order.class);
                    if(user.getUid().equals(orderInfo.getUserId())){

                        mOrderArrayList.add(orderInfo);
                    }
                    Log.d(TAG, "onChildAdded: " + mOrderArrayList.size());


                }
                userOrderAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void inItView(View view) {
        orderListRecView = view.findViewById(R.id.orderItemRecView);
        progressBar = view.findViewById(R.id.progressBar);
        cartItemLay = view.findViewById(R.id.auctionNotificationAction);
        cartItemCount = view.findViewById(R.id.notificationCountTv);
    }
}