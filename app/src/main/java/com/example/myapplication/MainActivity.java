package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.model.BenchmarkResult;
import com.example.myapplication.util.BenchmarkUtil;
import com.example.myapplication.gpu.MyGLRenderer;
import com.example.myapplication.gpu.GPUSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button exe_time_button = findViewById(R.id.exe_time_button);
        TextView exe_time_result = findViewById(R.id.exe_time_result);
        Button mem_access_button = findViewById(R.id.mem_access_button);
        TextView mem_access_result = findViewById(R.id.mem_access_result);
        Button hardware_config_button = findViewById(R.id.hardware_config_button);
        Button graphics_processing_button = findViewById(R.id.graphics_processing_button);
        TextView graphics_processing_result = findViewById(R.id.graphics_processing_result);

        final GPUSurfaceView glSurfaceView = new GPUSurfaceView(this);
        final MyGLRenderer glRenderer = glSurfaceView.getRenderer();

        exe_time_button.setOnClickListener(view->{
            exe_time_result.setText("Running...");
            new Thread(()->{
                Runnable task=()->{
                    int sum=0;
                    for(int i=0; i < 10000000; i++){
                        sum+=i;
                    }
                };
                BenchmarkResult result= BenchmarkUtil.measureExecutionTime(task,100);
                double avgMs = result.getAverage() / 1000000.0;
                double maxMs = result.getMax_time() / 1000000.0;
                double minMs = result.getMin_time() / 1000000.0;
                runOnUiThread(()-> exe_time_result.setText(String.format("Average: %.3f ms\nMin: %.3f ms\nMax: %.3f ms", avgMs, minMs, maxMs)));
            }).start();
        });
        mem_access_button.setOnClickListener(view->{
            mem_access_result.setText("Running...");
            new Thread(()->{
                Runnable task=()->{
                    int[] data=new int[1000000];
                    for(int i=0; i<1000000; i++) data[i]=i;
                    int sum = 0;
                    for (int i = 0; i < 100; i++) {
                        int index = (int) (Math.random() * data.length);
                        sum += data[index];
                    }
                };
                BenchmarkResult result = BenchmarkUtil.measureMemoryAccessTime(task,100);
                double avgMs = result.getAverage() / 1000000.0;
                double maxMs = result.getMax_time() / 1000000.0;
                double minMS = result.getMin_time() / 1000000.0;
                runOnUiThread(()-> mem_access_result.setText(String.format("Average: %.3f ms\nMin: %.3f ms\nMax: %.3f ms", avgMs, minMS, maxMs)));
            }).start();
        });
        graphics_processing_button.setOnClickListener(view -> {
            graphics_processing_result.setText("Running...");
            new Thread(() -> {
                BenchmarkResult result = BenchmarkUtil.measureGraphicsTime(glSurfaceView, glRenderer, 50);
                if (result != null) {
                    double avgMs = result.getAverage() / 1000000.0;
                    double maxMs = result.getMax_time() / 1000000.0;
                    double minMs = result.getMin_time() / 1000000.0;
                    runOnUiThread(() -> graphics_processing_result.setText(String.format("Average: %.3f ms\nMin: %.3f ms\nMax: %.3f ms", avgMs, minMs, maxMs)));
                } else {
                    runOnUiThread(() -> graphics_processing_result.setText("Graphics Test Failed."));
                }
            }).start();
        });
        hardware_config_button.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, com.example.myapplication.HardwareConfigActivity.class);
            startActivity(intent);
        });
    }
}