package com.astronist.pups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astronist.pups.Adapter.ProductAdapter;
import com.astronist.pups.Adapter.SliderAdapter;
import com.astronist.pups.Model.CartList;
import com.astronist.pups.Model.Product;
import com.astronist.pups.Model.SlideItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeOrderActivity extends AppCompatActivity {

    private RecyclerView groceryRecView, specialRecView, beautyRecView;
    private ViewPager2 viewPager2;
    private RelativeLayout cartItemLay;
    private ArrayList<Product> groceryProductList = new ArrayList<>();
    //private ArrayList<SlideItem> slideImageList = new ArrayList<>();
    private ArrayList<SlideItem> specialProductList = new ArrayList<>();
    private ArrayList<Product> beautyProductList = new ArrayList<>();
    private ProductAdapter mProductAdapter;
    public static final String TAG = "MakeOrder";
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference cartReference;
    private TextView cartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        groceryList();
        //sliderImageList();
        specialList();
        beautyList();
        inItView();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mProductAdapter = new ProductAdapter(MakeOrderActivity.this, groceryProductList);
        groceryRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        groceryRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

        viewPager2.setAdapter(new SliderAdapter(MakeOrderActivity.this, specialProductList));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        /*mProductAdapter = new ProductAdapter(MakeOrderActivity.this, specialProductList);
        specialRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        specialRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();*/

        mProductAdapter = new ProductAdapter(MakeOrderActivity.this, beautyProductList);
        beautyRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        beautyRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

        getCartItemCount();

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
                    if(cartListArrayList.size()<1){
                        cartItemCount.setVisibility(View.GONE);
                    }else{
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

   /* private void sliderImageList() {

        SlideItem slideItem = new SlideItem(R.drawable.potato);
        slideImageList.add(slideItem);
        slideItem = new SlideItem(R.drawable.chilli);
        slideImageList.add(slideItem);
        slideItem = new SlideItem(R.drawable.onion);
        slideImageList.add(slideItem);
        slideItem = new SlideItem(R.drawable.potato);
        slideImageList.add(slideItem);
        slideItem = new SlideItem(R.drawable.garlic);
        slideImageList.add(slideItem);
        slideItem = new SlideItem(R.drawable.chilli);
        slideImageList.add(slideItem);
    }*/

    private void groceryList() {
        Product product = new Product(R.drawable.potato, "Potato", "25", "কেজি","৳","Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.tometo, "Tomato", "35", "কেজি","৳","Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.onion, "Onion", "45","কেজি", "৳","Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.garlic, "Garlic", "55", "কেজি","৳","Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.brinjal, "Brinjal", "35", "কেজি","৳","Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.chilli, "Green Chili", "40", "কেজি","৳","Available");
        groceryProductList.add(product);

    }

    private void specialList() {
        SlideItem slideItem = new SlideItem(R.drawable.benson, "Benson", "290","Packet", "৳","Available");
        specialProductList.add(slideItem);
        slideItem = new SlideItem(R.drawable.benson_switch, "Benson Switch", "290","Packet", "৳","Available");
        specialProductList.add(slideItem);
        slideItem = new SlideItem(R.drawable.marlboro, "Marlboro", "300", "Packet","৳","Available");
        specialProductList.add(slideItem);
        slideItem = new SlideItem(R.drawable.danhil, "Danhill", "300", "Packet","৳","Available");
        specialProductList.add(slideItem);
        slideItem = new SlideItem(R.drawable.goldleaf, "Gold leaf", "190", "Packet","৳","Available");
        specialProductList.add(slideItem);
        slideItem = new SlideItem(R.drawable.hollywood, "Hollywood", "120", "Packet","৳","Available");
        specialProductList.add(slideItem);

    }

    private void beautyList() {
        Product product = new Product(R.drawable.fare_loely, "Fare & Lovely", "120", "Packet","৳","Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.hand_senitizer, "Sanitizer", "100", "Bottle","৳","Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.senitizer, "Sanitizer", "120", "Bottle","৳","Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.kn95, "KN95", "100", "Piece","৳","Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mask, "Regular mask", "10", "Piece","৳","Available");
        beautyProductList.add(product);
    }

    private void inItView() {
        groceryRecView = findViewById(R.id.groceryRecView);
        viewPager2 = findViewById(R.id.viewPagerImage);
        //specialRecView = findViewById(R.id.pupSpecialItemsRecView);
        beautyRecView = findViewById(R.id.beautyItemsRecViewiew);
        cartItemLay = findViewById(R.id.auctionNotificationAction);
        cartItemCount = findViewById(R.id.notificationCountTv);
    }
}