package com.astronist.pups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.security.cert.PolicyNode;

public class OrderSuccessfulActivity extends AppCompatActivity {

    private TextView backToOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successful);

        backToOrderList = findViewById(R.id.backToPav);

        backToOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessfulActivity.this, FragmentMenuContainerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

}