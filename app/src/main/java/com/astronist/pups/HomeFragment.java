package com.astronist.pups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astronist.pups.Adapter.StaggeredGridAdapter;
import com.astronist.pups.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private Context context;
    private TextView orderTextBtn;
    private DatabaseReference orderListReference;
    private String currentMonthName;
    private ProgressBar progressBar;
    private RecyclerView orderListRecView;
    private StaggeredGridAdapter staggeredGridAdapter;
    private ArrayList<Order> mOrderArrayList = new ArrayList<>();
    private StaggeredGridLayoutManager manager;
    public static final String TAG = "Order";
    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItView(view);
        progressBar.setVisibility(View.VISIBLE);
        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        orderListRecView.setLayoutManager(manager);
        staggeredGridAdapter = new StaggeredGridAdapter(context, mOrderArrayList);
        orderListRecView.setAdapter(staggeredGridAdapter);

        getAllOrderDetails();

        orderTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllOrderDetails() {

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
                        mOrderArrayList.add(orderInfo);
                        Log.d(TAG, "onChildAdded: " + mOrderArrayList.size());


                }
                staggeredGridAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inItView(View view) {
        orderTextBtn = view.findViewById(R.id.orderBtn);
        orderListRecView = view.findViewById(R.id.orderInfoRecView);
        progressBar = view.findViewById(R.id.progressBar);
    }
}