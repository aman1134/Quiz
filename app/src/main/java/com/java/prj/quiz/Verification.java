package com.java.prj.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {

    FirebaseAuth mAuth;
    String verid;
    TextInputEditText otp;
    MaterialButton login;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mAuth = FirebaseAuth.getInstance();

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("TAG", "onVerificationCompleted:" + credential);
                signInWithCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("TAG", "onVerificationFailed", e);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d("TAG", "onCodeSent:" + verificationId);
                verid = verificationId;
            }
        };

        System.out.println("\n pgone = "+ LogIn.phone);
        sendVerificationCode("+91" +LogIn.phone);

        otp = (TextInputEditText) findViewById(R.id.otp);
        login = (MaterialButton) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = otp.getText().toString();
                if(OTP.length() == 6)
                    otp.setError("Enter a valid OTP");
                else{
                    verifyCode(OTP);
                }
            }
        });

    }


    private void sendVerificationCode(String number){
        System.out.println("\n send verification code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            System.out.println("\n on verification complete callback" + "");
            if (code != null) {
                //verifying the code
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            verid = s;
        }
    };


    private void verifyCode(String code){
        System.out.println("\n verify code");
        System.out.println("\n verificationid = "+ verid);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        System.out.println("\n signin with credentials");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences sharedPreferences = Verification.this.getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.edit().putString("cno", LogIn.phone ).apply();
                            sharedPreferences.edit().putBoolean("logged", true).apply();
                            sharedPreferences.edit().putString("uid",mAuth.getCurrentUser().getUid()).apply();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/students");
                            ref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ContainerClass.User user = snapshot.getValue(ContainerClass.User.class);
                                    if(user == null) {
                                        SplashScreen.user = new ContainerClass.User();
                                        SplashScreen.user.setCno(LogIn.phone);
                                        SplashScreen.user.setUid(mAuth.getCurrentUser().getUid());
                                        snapshot.getRef().setValue(SplashScreen.user);
                                    }else{
                                        SplashScreen.user = user;
                                    }
                                    Toast.makeText(Verification.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                    startActivity(new Intent(Verification.this, MainActivity.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else{
                            Toast.makeText(Verification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

}