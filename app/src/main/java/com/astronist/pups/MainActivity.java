package com.astronist.pups;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainViewLayout, verifyLayout;
    private LinearLayout googleBtn, customFbLogIn;
    private Button btnContinue, nextBtn;
    private EditText phoneNumberEt, verificationBox;
    private TextView countryCode;
    private static final int GOOGLE_SIGN_IN_REQUEST = 112;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private String verificationOTP;
    public static final String TAG = "Login";
    private TelephonyManager telephonyManager;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inItView();

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(MainActivity.this, FragmentMenuContainerActivity.class);
            startActivity(intent);
        }

        telephonyManager = (TelephonyManager) this.getSystemService(MainActivity.this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            Log.e(TAG, "onCreate: " + telephonyManager.getLine1Number());
        }


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEt.getText().toString().trim();
                String mainNumber = countryCode.getText().toString().trim() + phoneNumber;
                if (phoneNumber.isEmpty()) {
                    phoneNumberEt.setError("Phone number is required!");
                    phoneNumberEt.requestFocus();
                    return;
                }
                else{
                    mainViewLayout.setVisibility(View.GONE);
                    verifyLayout.setVisibility(View.VISIBLE);
                    DoOTPLogIn(mainNumber);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(verificationBox.getText().toString().trim());
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoGoogleLogIn();
            }
        });

        customFbLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoFacebookLogIn();
            }
        });
    }

    /////////////////////////function//////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Log.e(TAG, "onCreate: " + telephonyManager.getLine1Number() );

                }
        }
    }

    private void verifyOTP(String otp) {
        PhoneAuthCredential otpCredential = PhoneAuthProvider.getCredential(verificationOTP, otp);
        firebaseAuth.signInWithCredential(otpCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser otpUser = task.getResult().getUser();
                    SendUserData((otpUser));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+ e.getLocalizedMessage() );
            }
        });
    }

    private void SendUserData(FirebaseUser otpUser) {
        finish();
        Intent intent = new Intent(MainActivity.this, FragmentMenuContainerActivity.class);
        startActivity(intent);
    }

    private void DoOTPLogIn(String phoneNumber) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verifyToken, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationOTP = verifyToken;
                PhoneAuthProvider.ForceResendingToken token = forceResendingToken;
            }
        };
        PhoneAuthProvider
                .getInstance()
                .verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, MainActivity.this, phoneCallBack);
    }

    /////////////Facebook login setUp with firebase//////////////////
    private void DoFacebookLogIn() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook success!");
                handleFacebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: " + "Login Cancel!");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + "Login Error!");

            }
        });

    }

    private void handleFacebookLogin(LoginResult loginResult) {
        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: " + fuser.getUid());

                            finish();
                            Intent intent = new Intent(MainActivity.this, FragmentMenuContainerActivity.class);
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failure: " + e.getLocalizedMessage());
            }
        });
    }

    /////////////Google login setUp with firebase//////////////////
    private void DoGoogleLogIn() {
        ////////////////Creating google sign in options object/////////////
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("962466355829-gf33re4asdjr1j9lk8vsr6m4ltfogc2g.apps.googleusercontent.com")
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        ///////////////Creating google Client object/////////////

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

        //////launching google login intent////////////////
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                processFireBaseLoginStep(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processFireBaseLoginStep(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            finish();
                            Intent intent = new Intent(MainActivity.this, FragmentMenuContainerActivity.class);
                            intent.putExtra("gPlusResult", account);
                            startActivity(intent);

                        }
                    }
                });
    }

    /////////////Google login setUp with firebase//////////////////

    private void inItView() {
        customFbLogIn = findViewById(R.id.fbLay);
        googleBtn = findViewById(R.id.gPlusLay);

        btnContinue = findViewById(R.id.continueBtn);
        nextBtn = findViewById(R.id.nextBtn);

        phoneNumberEt = findViewById(R.id.phone);
        verificationBox = findViewById(R.id.verifiedCode);

        mainViewLayout = findViewById(R.id.mainLay);
        verifyLayout = findViewById(R.id.verificationLay);
        countryCode = findViewById(R.id.countryCode);
    }
}