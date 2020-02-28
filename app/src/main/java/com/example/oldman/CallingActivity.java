package com.example.oldman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CallingActivity extends AppCompatActivity {
    private CardView ambulance,ola, callDady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        ambulance = findViewById(R.id.ambulanceNumber);
        ola = findViewById(R.id.olaLauncher);
        callDady = findViewById(R.id.callDad);
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        ola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchOla = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
                startActivity(launchOla);
            }
        });
        callDady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callYourDad();
            }
        });
    }
    private void makePhoneCall(){
        if (ContextCompat.checkSelfPermission(CallingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CallingActivity.this, new String[] { Manifest.permission.CALL_PHONE }, 1000);
        } else {
            String dial = "tel:"+"102";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    private void callYourDad(){
        if (ContextCompat.checkSelfPermission(CallingActivity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CallingActivity.this, new String[] { Manifest.permission.CALL_PHONE }, 2000);
        } else {
            String call = "tel:"+"9460574366";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(CallingActivity.this,"Now Give Permission Manually",Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 2000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callYourDad();
            } else {
                Toast.makeText(CallingActivity.this,"Please geant permission manually", Toast.LENGTH_LONG).show();
            }
        }
    }
}
