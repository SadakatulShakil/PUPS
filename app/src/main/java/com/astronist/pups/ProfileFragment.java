package com.astronist.pups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.astronist.pups.Model.Address;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TextView logOutBt, editProfile;
    private ImageView profileImage;
    private TextView userName, userEmail, userPhone, userLocation;
    private GoogleSignInAccount googleSignInAccount;
    private LoginResult loginResult;
    private DatabaseReference userInfoRef;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inItView(view);
        Intent gIntent = getActivity().getIntent();

        googleSignInAccount = gIntent.getParcelableExtra("gPlusResult");
        loginResult = (LoginResult) gIntent.getSerializableExtra("fbResult");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Log.d(TAG, "inItView: " + user.getEmail() );
        Log.d(TAG, "inItView: " + user.getDisplayName());


        if(googleSignInAccount!=null){

                String imageUrl = String.valueOf(googleSignInAccount.getPhotoUrl());
                Picasso.get().load(imageUrl).into(profileImage);
                userName.setText(googleSignInAccount.getDisplayName());
                userEmail.setText(googleSignInAccount.getEmail());
                Picasso.get().load(imageUrl).into(profileImage);

        }

        if(firebaseAuth!= null){
            String fbImage = user.getPhotoUrl().toString();
            Picasso.get().load(fbImage).into(profileImage);
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
        }
        logOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        getAuthUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getAuthUserData() {

        userInfoRef = FirebaseDatabase.getInstance().getReference().child("Address");
        userInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Address addressInfo = userSnapshot.getValue(Address.class);


                        if (user.getUid().equals(addressInfo.getUserId())) {
                            if(addressInfo!= null){
                            userPhone.setText(addressInfo.getCustomerPhone());
                            userLocation.setText(addressInfo.getCustomerDohsName()+","+addressInfo.getCustomerHouseNo()
                                    +","+addressInfo.getCustomerRoadNo());

                        }else{
                                userName.setText("Update your contact");
                                userLocation.setText("Update your Location");
                            }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inItView(View view) {
        logOutBt = view.findViewById(R.id.logOutBtn);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userPhone = view.findViewById(R.id.userContact);
        profileImage = view.findViewById(R.id.profileImage);
        userLocation = view.findViewById(R.id.userLocation);
        editProfile = view.findViewById(R.id.profileEditBt);
    }
}