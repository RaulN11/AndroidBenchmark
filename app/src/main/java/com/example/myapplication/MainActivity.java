package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.model.BenchmarkResult;
import com.example.myapplication.util.BenchmarkUtil;

import org.w3c.dom.Text;

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
        Button button=findViewById(R.id.exe_time_button);
        TextView textView=findViewById(R.id.exe_time_result);
        Button button1=findViewById(R.id.mem_access_button);
        TextView textView1=findViewById(R.id.mem_access_result);
        Button button2=findViewById(R.id.hardware_config_button);
        button.setOnClickListener(view->{
            textView.setText("Running...");
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
                runOnUiThread(()->textView.setText(String.format("Average: %.3f ms\nMin: %.3f ms\nMax: %.3f ms", avgMs, minMs, maxMs)));
            }).start();
        });
        button1.setOnClickListener(view->{
           textView1.setText("Running...");
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
               runOnUiThread(()->textView1.setText(String.format("Average: %.3f ms\nMin: %.3f ms\nMax: %.3f ms", avgMs, minMS, maxMs)));
           }).start();
        });
        button2.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, com.example.myapplication.HardwareConfigActivity.class);
            startActivity(intent);
        });



    }

}