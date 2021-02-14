package com.heet.shiningfox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    EditText e1, e2;
    Button b1, b2;
    FirebaseAuth auth;
    String verificationId;
    FirebaseFirestore fstore;
    UserModel model;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        e1 = findViewById(R.id.reg_mobile);
        e2 = findViewById(R.id.reg_otp);
        b1 = findViewById(R.id.reg_sendotp);
        b2 = findViewById(R.id.reg_submit);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        preferences = getApplicationContext().getSharedPreferences("Users",0);
        editor = preferences.edit();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().toString().trim().equals("")) {
                    e1.setError("Enter Mobile Number");
                } else {
                    sendOTP(e1.getText().toString());
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e2.getText().toString().trim().equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please enter OTP first.", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, e2.getText().toString());
                    verifyOTP(credential);
                }
            }
        });

    }

    private void verifyOTP(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    model = new UserModel();
                    model.setUserMobile(e1.getText().toString());
                    model.setuId(FirebaseAuth.getInstance().getUid());

                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(model.getUserMobile())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                //Toast.makeText(RegistrationActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                                editor.putString("userMobile",model.getUserMobile());
                                editor.commit();
                                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();


                            } else {

                                FirebaseFirestore.getInstance().collection("USERS")
                                        .document(model.getUserMobile()).set(model)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                editor.putString("userMobile",model.getUserMobile());
                                                editor.commit();
                                                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
        });
    }

    private void sendOTP(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile, 60L, TimeUnit.SECONDS,
                RegistrationActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;

                        b1.setVisibility(View.GONE);
                        e2.setVisibility(View.VISIBLE);
                        b2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(RegistrationActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();

                        String code = phoneAuthCredential.getSmsCode();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                        verifyOTP(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}