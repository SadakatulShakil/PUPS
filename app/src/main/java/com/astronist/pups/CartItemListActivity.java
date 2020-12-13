package com.astronist.pups;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astronist.pups.Adapter.UserCartAdapter;
import com.astronist.pups.Model.CartList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    private int total, gTotal=0;

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

        progressBar.setVisibility(View.VISIBLE);
        getUserCartList();
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
                    gTotal+= Integer.parseInt(cartListArrayList.get(i).getTotalPrice());;
                }
                Log.d(TAG, "onDataCheck: " +  gTotal);
                cartAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView() {
        cartListRecView = findViewById(R.id.cartInfoRecView);
        progressBar = findViewById(R.id.progressBar);
        grandAmount = findViewById(R.id.grandItemAmount);
        grandUnit = findViewById(R.id.grandUnit);
    }
}