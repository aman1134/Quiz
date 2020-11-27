package com.java.prj.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LogIn extends AppCompatActivity {


    TextInputEditText phoneno;
    MaterialButton sendOTP;
    public static String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        phoneno = (TextInputEditText) findViewById(R.id.cno);
        sendOTP = (MaterialButton) findViewById(R.id.send_otp);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneno.getText().toString();
                if(phone.length() != 10)
                    phoneno.setError("PLease enter a valid no.");
                else{
                    Intent intent = new Intent(LogIn.this, Verification.class);
                    startActivity(intent);
                }
            }
        });

    }
}