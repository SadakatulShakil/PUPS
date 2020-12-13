package com.astronist.pups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.astronist.pups.Model.Address;
import com.astronist.pups.Model.CartList;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderConfirmationActivity extends AppCompatActivity {
    private SlideItem slideItem;
    private Product product;
    private ImageView proImage;
    private TextView quantity, amount, title, titleAmount, status, unit, demoUnit, currency, loosieQuantity, loosieAmount, saveAddress;
    private EditText name, phone, dohsName, houseNo, roadNo;
    private FloatingActionButton plus, minus;
    private ExtendedFloatingActionButton cartBtn, orderBtn;
    private int counter = 1;
    private String totalPrice, currentTime, currentDate, monthName, userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference orderReference, cartReference;
    private DatabaseReference addressReference;
    public static final String TAG = "Order";
    private ProgressBar progressBar;
    private String category;
    private RadioGroup optionGroup, loosieGroup, categoryGroup;
    private RadioButton regRB, swRB, ltRB;
    private LinearLayout loosieDetailsLay, packetLay, increDcreLay;
    private String brandOptionType = "", loosieType, loosAmount, mainType = "Product";
    private int bandson = 15, marlboro = 15, goldleaf = 10, danhill = 18, hollywood = 6, lAmount;
    private Order order;
    private int checkCart = 0;
    private RelativeLayout cartItemLay;
    private ArrayList<CartList> cartListArrayList = new ArrayList<>();
    private TextView cartItemCount;

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
        category = intent.getStringExtra("category");

        getCartItemCount();

        if (category.equals("special")) {
            ///////cigarate category////////
            categoryGroup.setVisibility(View.VISIBLE);
        } else {
            categoryGroup.setVisibility(View.GONE);
            packetLay.setVisibility(View.VISIBLE);
            increDcreLay.setVisibility(View.VISIBLE);
        }

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

        getUserAddress();

        cartItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderConfirmationActivity.this, CartItemListActivity.class);
                startActivity(intent1);
            }
        });
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

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddressSetIp();
                getUserAddress();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////Do work for multiple order in cart/////////////////
                checkCart++;
                if (checkCart == 1) {
                    goToCartProductSetUp(mainType, brandOptionType);
                } else {
                    Toast.makeText(OrderConfirmationActivity.this, "This Prouct is already in Cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                goToOrderSetup(mainType, brandOptionType);
            }
        });


        ////////////////Radio group Work flow///////////////////

        if (title.getText().toString().trim().equals("Danhill")) {
            swRB.setClickable(false);
            ltRB.setClickable(false);
        }
        if (title.getText().toString().trim().equals("Hollywood")) {
            swRB.setClickable(false);
            ltRB.setClickable(false);
        }

        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton categoryType = (RadioButton) categoryGroup.findViewById(categoryGroup.getCheckedRadioButtonId());
                mainType = (String) categoryType.getText();
                optionGroup.setVisibility(View.VISIBLE);
            }
        });

        optionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton optionType = (RadioButton) optionGroup.findViewById(optionGroup.getCheckedRadioButtonId());
                brandOptionType = (String) optionType.getText();
                goToNextOption(brandOptionType);
                //Toast.makeText(OrderConfirmationActivity.this, "Brand Selected : "+brandOptionType.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });


        loosieGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton loosieOption = (RadioButton) loosieGroup.findViewById(loosieGroup.getCheckedRadioButtonId());
                loosieType = (String) loosieOption.getText();
                goToAnotherOption(loosieType, title.getText().toString().trim());
                // Toast.makeText(OrderConfirmationActivity.this, "Brand Selected : "+loosieType.toString().trim(), Toast.LENGTH_SHORT).show();

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
                    Log.d(TAG, "onDataChange: " + cartListArrayList.size());
                    if (cartListArrayList.size() < 1) {
                        cartItemCount.setVisibility(View.GONE);
                    } else {
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

    private void goToCartProductSetUp(String mainType, String brandOptionType) {
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

        storeCartDetails(uName, uPhone, location, title.getText().toString().trim(), unit.getText().toString().trim(),
                currency.getText().toString().trim(), quantity.getText().toString().trim(), amount.getText().toString().trim(),
                loosieQuantity.getText().toString().trim(), loosieAmount.getText().toString().trim(), mainType, brandOptionType);

    }


    private void storeCartDetails(final String uName, final String uPhone, final String location,
                                  final String title, String unit, final String currency,
                                  String quantity, String amount, final String loosieQuantity,
                                  final String loosieAmount, final String mainType, final String brandOptionType) {
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
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        String pushId = cartReference.push().getKey();

        CartList cartList = new CartList(userId, pushId, currentTime, currentDate, uName, uPhone,
                location, title, unit, currency, quantity, amount, mainType);

        cartReference.child(userId).child(pushId).setValue(cartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(OrderConfirmationActivity.this, "Your Product is added to Cart!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private void getUserAddress() {

        addressReference = FirebaseDatabase.getInstance().getReference().child("Address");
        addressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Address addressInfo = userSnapshot.getValue(Address.class);

                    if (user.getUid().equals(addressInfo.getUserId())) {
                        name.setText(addressInfo.getCustomerName());
                        phone.setText(addressInfo.getCustomerPhone());
                        dohsName.setText(addressInfo.getCustomerDohsName());
                        houseNo.setText(addressInfo.getCustomerHouseNo());
                        roadNo.setText(addressInfo.getCustomerRoadNo());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startAddressSetIp() {
        String uName = name.getText().toString().trim();
        String uPhone = phone.getText().toString().trim();
        String uDohsName = dohsName.getText().toString().trim();
        String uHouseNo = houseNo.getText().toString().trim();
        String uRoadNo = roadNo.getText().toString().trim();


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

        storeBioData(uName, uPhone, uDohsName, uHouseNo, uRoadNo);

    }

    private void storeBioData(final String uName, final String uPhone, final String uDohsName, final String uHouseNo, final String uRoadNo) {
        if (firebaseAuth != null) {
            userId = user.getUid();
        }

        addressReference = FirebaseDatabase.getInstance().getReference().child("Address");

        Address address = new Address(userId, uName, uPhone, uDohsName, uHouseNo, uRoadNo);

        addressReference.child(userId).setValue(address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OrderConfirmationActivity.this, "Your Address Saved Successfully !", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderConfirmationActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }


    private void goToAnotherOption(String loosieType, String title) {
        if (loosieType.equals("5 pieces")) {
            /////do for 5 pieces////
            loosieQuantity.setText("5");
            if (title.equals("Benson")) {
                lAmount = 5 * bandson;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Marlboro")) {
                lAmount = 5 * marlboro;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Danhill")) {
                lAmount = 5 * danhill;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Gold leaf")) {
                lAmount = 5 * goldleaf;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Hollywood")) {
                lAmount = 5 * hollywood;
                loosAmount = String.valueOf(lAmount);
            } else {
                lAmount = 5 * bandson;
                loosAmount = String.valueOf(lAmount);
            }
            loosieAmount.setText(loosAmount);

        } else if (loosieType.equals("10 pieces")) {
            /////do for 10 pieces////
            loosieQuantity.setText("10");
            if (title.equals("Benson")) {
                lAmount = 10 * bandson;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Marlboro")) {
                lAmount = 10 * marlboro;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Danhill")) {
                lAmount = 10 * danhill;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Gold leaf")) {
                lAmount = 10 * goldleaf;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Hollywood")) {
                lAmount = 10 * hollywood;
                loosAmount = String.valueOf(lAmount);
            } else {
                lAmount = 10 * bandson;
                loosAmount = String.valueOf(lAmount);
            }
            loosieAmount.setText(loosAmount);

        } else if (loosieType.equals("15 pieces")) {
            /////do for 15 pieces////
            loosieQuantity.setText("15");
            if (title.equals("Benson")) {
                lAmount = 15 * bandson;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Marlboro")) {
                lAmount = 15 * marlboro;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Danhill")) {
                lAmount = 15 * danhill;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Gold leaf")) {
                lAmount = 15 * goldleaf;
                loosAmount = String.valueOf(lAmount);
            } else if (title.equals("Hollywood")) {
                lAmount = 15 * hollywood;
                loosAmount = String.valueOf(lAmount);
            } else {
                lAmount = 15 * bandson;
                loosAmount = String.valueOf(lAmount);
            }
            loosieAmount.setText(loosAmount);

        }

    }


    private void goToNextOption(String brandOptionType) {
        if (brandOptionType.equals("Packet")) {
            packetLay.setVisibility(View.VISIBLE);
            increDcreLay.setVisibility(View.VISIBLE);
            loosieDetailsLay.setVisibility(View.GONE);
            loosieGroup.setVisibility(View.GONE);
        } else {
            packetLay.setVisibility(View.GONE);
            increDcreLay.setVisibility(View.GONE);
            loosieDetailsLay.setVisibility(View.VISIBLE);
            loosieGroup.setVisibility(View.VISIBLE);

        }

    }

    private void amountCalculation() {
        int baseAmount = Integer.parseInt(titleAmount.getText().toString().trim());
        totalPrice = String.valueOf(counter * baseAmount);
        amount.setText(totalPrice);
    }

    private void goToOrderSetup(String mainType, String brandOptionType) {
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
                currency.getText().toString().trim(), quantity.getText().toString().trim(), amount.getText().toString().trim(),
                loosieQuantity.getText().toString().trim(), loosieAmount.getText().toString().trim(), mainType, brandOptionType);

    }

    private void storeOrderDetails(final String uName, final String uPhone, final String location, final String title,
                                   String unit, final String currency, String quantity, String amount,
                                   final String loosieQuantity, final String loosieAmount,
                                   final String mainType, final String brandOptionType) {

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
        if (brandOptionType.equals("Loosie")) {
            amount = loosieAmount;
            quantity = loosieQuantity;
            unit = "Loosie";
        }
        order = new Order(userId, pushId, currentTime, currentDate, uName, uPhone, location, title, unit, currency, quantity, amount, mainType);

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
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
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

        optionGroup = findViewById(R.id.optionRGroup);
        loosieGroup = findViewById(R.id.loosieRGroup);
        categoryGroup = findViewById(R.id.categoryRGroup);

        regRB = findViewById(R.id.regularRb);
        swRB = findViewById(R.id.switchRb);
        ltRB = findViewById(R.id.liteRb);

        packetLay = findViewById(R.id.packetLay);
        loosieDetailsLay = findViewById(R.id.loosieDetailsLay);
        increDcreLay = findViewById(R.id.incrementLay);
        loosieQuantity = findViewById(R.id.loosieQuantity);
        loosieAmount = findViewById(R.id.loosieAmountTv);

        saveAddress = findViewById(R.id.saveBiodata);
        cartItemLay = findViewById(R.id.auctionNotificationAction);
        cartItemCount = findViewById(R.id.notificationCountTv);


    }
}