package com.astronist.pups;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Adapter.UserCartAdapter;
import com.astronist.pups.Model.CartList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CartItemListActivity extends AppCompatActivity {

    private RecyclerView cartListRecView;
    private UserCartAdapter cartAdapter;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference cartReference;
    private LinearLayoutManager manager;
    public static final String TAG = "Cart";
    private ProgressBar progressBar;
    private TextView grandAmount, grandUnit;
    private int gTotal=0;
    private String currency;
    private CartList cartList;
    private ExtendedFloatingActionButton placeOrderBtn;
    private String monthName;
    private DatabaseReference cartToOrderReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_list);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Log.d(TAG, "onCreate: " + user.getUid());

        manager = new LinearLayoutManager(CartItemListActivity.this, RecyclerView.VERTICAL, false);
        cartListRecView.setLayoutManager(manager);
        cartAdapter = new UserCartAdapter(CartItemListActivity.this, cartListArrayList);
        cartListRecView.setAdapter(cartAdapter);

        //progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserCartList();
                cartAdapter.showShimmer = false;
                //staggeredGridAdapter.notifyDataSetChanged();
            }
        },2000);

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUpCartOrder();
            }
        });
    }

    private void setUpCartOrder() {
        for(int j = 0; j<cartListArrayList.size(); j++)
        {
            cartList = new CartList(cartListArrayList.get(j).getUserId(), cartListArrayList.get(j).getPushId(),
                    cartListArrayList.get(j).getTime(), cartListArrayList.get(j).getDate(), cartListArrayList.get(j).getName(),
                    cartListArrayList.get(j).getContact(), cartListArrayList.get(j).getLocation(), cartListArrayList.get(j).getProductName(),
                    cartListArrayList.get(j).getUnit(), cartListArrayList.get(j).getCurrency(), cartListArrayList.get(j).getProductQuantity(),
                    cartListArrayList.get(j).getTotalPrice(), cartListArrayList.get(j).getCategory(), cartListArrayList.get(j).getDeliveryStatus());

            Calendar calendar = Calendar.getInstance();
            ///Current Date//////
            monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

            cartToOrderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(monthName);

            cartToOrderReference.child(cartListArrayList.get(j).getPushId()).setValue(cartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(CartItemListActivity.this, "Your Order Placed Successfully !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CartItemListActivity.this, OrderSuccessfulActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CartItemListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());


                }
            });
        }

        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        cartReference.child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CartItemListActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private void getUserCartList() {

        String userId = user.getUid();
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child(userId);

        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartListArrayList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    CartList cartList = userSnapshot.getValue(CartList.class);

                    cartListArrayList.add(cartList);

                }

                for(int i = 0; i<cartListArrayList.size(); i++){
                    gTotal+= Integer.parseInt(cartListArrayList.get(i).getTotalPrice());
                    currency = cartListArrayList.get(i).getCurrency();
                }
                Log.d(TAG, "onDataCheck: " +  gTotal);
                String grandTotal = String.valueOf(gTotal);
                grandAmount.setText(grandTotal);
                grandUnit.setText(currency);
                cartAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CartItemListActivity.this, FragmentMenuContainerActivity.class);
        startActivity(intent);
        finish();
    }

    private void inItView() {
        cartListRecView = findViewById(R.id.cartInfoRecView);
        progressBar = findViewById(R.id.progressBar);
        grandAmount = findViewById(R.id.grandItemAmount);
        grandUnit = findViewById(R.id.grandUnit);
        placeOrderBtn = findViewById(R.id.place_order_fab);
    }
}