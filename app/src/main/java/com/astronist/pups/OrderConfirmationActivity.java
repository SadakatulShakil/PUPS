package com.astronist.pups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.astronist.pups.Model.Product;
import com.astronist.pups.Model.SlideItem;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OrderConfirmationActivity extends AppCompatActivity {
    private SlideItem slideItem;
    private Product product;
    private ImageView proImage;
    private TextView quantity, amount, title, titleAmount, status, unit, demoUnit, currency;
    private EditText name, phone, dohsName, houseNo, roadNo;
    private FloatingActionButton plus, minus;
    private ExtendedFloatingActionButton cart, order;
    private int counter = 1;
    private String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        inItView();
        quantity.setText(Integer.toString(counter));
        Intent intent = getIntent();
        slideItem = (SlideItem) intent.getSerializableExtra("itemInfoSd");
        product = (Product) intent.getSerializableExtra("itemInfoRev");

        if(slideItem!= null){
            proImage.setImageResource(slideItem.getProfileImage());
            title.setText(slideItem.getTitleName());
            titleAmount.setText(slideItem.getPrice());
            status.setText(slideItem.getStatus());
            unit.setText(slideItem.getUnit());
            demoUnit.setText(slideItem.getUnit());
            currency.setText(slideItem.getCurrency());
        }
        if(product!= null){
            proImage.setImageResource(product.getProfileImage());
            title.setText(product.getTitleName());
            titleAmount.setText(product.getPrice());
            status.setText(product.getStatus());
            unit.setText(product.getUnit());
            demoUnit.setText(product.getUnit());
            currency.setText(product.getCurrency());
        }
        amount.setText(titleAmount.getText().toString().trim());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                quantity.setText(Integer.toString(counter));
                amountCalculation();

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>1) {
                    counter--;
                    quantity.setText(Integer.toString(counter));
                    amountCalculation();
                }
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderSetup();
            }
        });


    }

    private void amountCalculation() {
        int baseAmount = Integer.parseInt(titleAmount.getText().toString().trim());
        totalPrice = String.valueOf(counter*baseAmount);
        amount.setText(totalPrice);
    }

    private void goToOrderSetup() {



    }

    private void inItView() {
        proImage = findViewById(R.id.imageView2);
        quantity = findViewById(R.id.quantityTv);
        amount = findViewById(R.id.amountTv);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        dohsName = findViewById(R.id.dohsName);
        houseNo = findViewById(R.id.houseNo);
        roadNo = findViewById(R.id.roadNo);

        plus = findViewById(R.id.plusId);
        minus = findViewById(R.id.minusId);
        cart = findViewById(R.id.cart_fab);
        order = findViewById(R.id.order_fab);

        title = findViewById(R.id.productTitle);
        titleAmount = findViewById(R.id.productUnitPrice);
        status = findViewById(R.id.availability);
        unit = findViewById(R.id.unit);
        demoUnit = findViewById(R.id.demoUnit);
        currency = findViewById(R.id.currency);


    }
}