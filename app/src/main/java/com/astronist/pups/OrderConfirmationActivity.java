package com.astronist.pups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.astronist.pups.Model.Order;
import com.astronist.pups.Model.Product;
import com.astronist.pups.Model.SlideItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {
    private SlideItem slideItem;
    private Product product;
    private ImageView proImage;
    private TextView quantity, amount, title, titleAmount, status, unit, demoUnit, currency;
    private EditText name, phone, dohsName, houseNo, roadNo;
    private FloatingActionButton plus, minus;
    private ExtendedFloatingActionButton cartBtn, orderBtn;
    private int counter = 1;
    private String totalPrice, currentTime, currentDate, monthName, userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference orderReference;
    public static final String TAG = "Order";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        inItView();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        quantity.setText(Integer.toString(counter));
        Intent intent = getIntent();
        slideItem = (SlideItem) intent.getSerializableExtra("itemInfoSd");
        product = (Product) intent.getSerializableExtra("itemInfoRev");

        if (slideItem != null) {
            proImage.setImageResource(slideItem.getProfileImage());
            title.setText(slideItem.getTitleName());
            titleAmount.setText(slideItem.getPrice());
            status.setText(slideItem.getStatus());
            unit.setText(slideItem.getUnit());
            demoUnit.setText(slideItem.getUnit());
            currency.setText(slideItem.getCurrency());
        }
        if (product != null) {
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
                if (counter > 1) {
                    counter--;
                    quantity.setText(Integer.toString(counter));
                    amountCalculation();
                }
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                goToOrderSetup();
            }
        });


    }

    private void amountCalculation() {
        int baseAmount = Integer.parseInt(titleAmount.getText().toString().trim());
        totalPrice = String.valueOf(counter * baseAmount);
        amount.setText(totalPrice);
    }

    private void goToOrderSetup() {
        String uName = name.getText().toString().trim();
        String uPhone = phone.getText().toString().trim();
        String uDohsName = dohsName.getText().toString().trim();
        String uHouseNo = houseNo.getText().toString().trim();
        String uRoadNo = roadNo.getText().toString().trim();

        String location = uDohsName + "," + uHouseNo + "," + uRoadNo;

        if (uName.isEmpty()) {
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        if (uPhone.isEmpty()) {
            phone.setError("Phone no is required!");
            phone.requestFocus();
            return;
        }
        if (uDohsName.isEmpty()) {
            dohsName.setError("DOHS is required!");
            dohsName.requestFocus();
            return;
        }
        if (uHouseNo.isEmpty()) {
            houseNo.setError("House no is required!");
            houseNo.requestFocus();
            return;
        }
        if (uRoadNo.isEmpty()) {
            roadNo.setError("Road no is required!");
            roadNo.requestFocus();
            return;
        }

        storeOrderDetails(uName, uPhone, location, title.getText().toString().trim(), unit.getText().toString().trim(),
                currency.getText().toString().trim(), quantity.getText().toString().trim(), amount.getText().toString().trim());

    }

    private void storeOrderDetails(final String uName, final String uPhone, final String location, final String title,
                                   final String unit, final String currency, final String quantity, final String amount) {

        if (firebaseAuth != null) {
            userId = user.getUid();
        }
        ///////////////Current Time///////////
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        currentTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        currentDate = myDateFormat.format(calendar.getTime());
        ///Current Date//////
        monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);

        orderReference = FirebaseDatabase.getInstance().getReference().child("Order").child(monthName);
        String pushId = orderReference.push().getKey();

        Order order = new Order(userId, currentTime, currentDate, uName, uPhone, location, title, unit, currency, quantity, amount);

        orderReference.child(pushId).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderConfirmationActivity.this, "Your Order Submit Successfully !", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderConfirmationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+ e.getLocalizedMessage());
            }
        });
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
        cartBtn = findViewById(R.id.cart_fab);
        orderBtn = findViewById(R.id.order_fab);

        title = findViewById(R.id.productTitle);
        titleAmount = findViewById(R.id.productUnitPrice);
        status = findViewById(R.id.availability);
        unit = findViewById(R.id.unit);
        demoUnit = findViewById(R.id.demoUnit);
        currency = findViewById(R.id.currency);

        progressBar = findViewById(R.id.progressBar);


    }
}