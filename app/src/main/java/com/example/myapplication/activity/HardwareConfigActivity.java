package com.example.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HardwareConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hardware_activity);

        TextView textView = findViewById(R.id.hardware_config_text);
        Button homePageButton=findViewById(R.id.homepage_button);
        StringBuilder sb = new StringBuilder();
        sb.append("Model: ").append(Build.MODEL).append("\n");
        sb.append("Brand: ").append(Build.BRAND).append("\n");
        sb.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n");
        sb.append("Device: ").append(Build.DEVICE).append("\n");
        sb.append("Hardware: ").append(Build.HARDWARE).append("\n");
        sb.append("Product: ").append(Build.PRODUCT).append("\n");
        sb.append("Board: ").append(Build.BOARD).append("\n");
        sb.append("Android Version: ").append(Build.VERSION.RELEASE).append("\n");
        sb.append("CPU Cores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        sb.append("Resolution: ").append(dm.widthPixels).append(" x ").append(dm.heightPixels).append("\n");
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(memInfo);
        sb.append("Total RAM: ").append(memInfo.totalMem / (1024 * 1024)).append(" MB\n");
        sb.append("Available RAM: ").append(memInfo.availMem / (1024 * 1024)).append(" MB\n\n");
        textView.setText(sb.toString());

        homePageButton.setOnClickListener(view->{
            Intent intent = new Intent(HardwareConfigActivity.this, com.example.myapplication.MainActivity.class);
            startActivity(intent);
        });
    }
}
