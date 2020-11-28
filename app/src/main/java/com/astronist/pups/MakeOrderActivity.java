package com.astronist.pups;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.astronist.pups.Adapter.BeautyAdapter;
import com.astronist.pups.Adapter.ProductAdapter;
import com.astronist.pups.Adapter.SpecialAdapter;
import com.astronist.pups.Model.Product;

import java.util.ArrayList;

public class MakeOrderActivity extends AppCompatActivity {

    private RecyclerView groceryRecView, specialRecView, beautyRecView;
    private ArrayList<Product> groceryProductList = new ArrayList<>();
    private ArrayList<Product> specialProductList = new ArrayList<>();
    private ArrayList<Product> beautyProductList = new ArrayList<>();
    private ProductAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        groceryList();
        specialList();
        beautyList();
        inItView();

        mProductAdapter = new ProductAdapter(MakeOrderActivity.this, groceryProductList);
        groceryRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        groceryRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

        mProductAdapter = new ProductAdapter(MakeOrderActivity.this, specialProductList);
        specialRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        specialRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

        mProductAdapter = new ProductAdapter(MakeOrderActivity.this, beautyProductList);
        beautyRecView.setLayoutManager(new LinearLayoutManager(MakeOrderActivity.this, RecyclerView.HORIZONTAL, false));
        beautyRecView.setAdapter(mProductAdapter);
        mProductAdapter.notifyDataSetChanged();

    }

    private void groceryList() {
        Product product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        groceryProductList.add(product);

    }

    private void specialList() {
        Product product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        specialProductList.add(product);

    }

    private void beautyList() {
        Product product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
        product = new Product(R.drawable.mango, "Potato", "25৳/কেজি", "Available");
        beautyProductList.add(product);
    }

    private void inItView() {
        groceryRecView = findViewById(R.id.groceryRecView);
        specialRecView = findViewById(R.id.pupSpecialItemsRecView);
        beautyRecView = findViewById(R.id.beautyItemsRecViewiew);
    }
}