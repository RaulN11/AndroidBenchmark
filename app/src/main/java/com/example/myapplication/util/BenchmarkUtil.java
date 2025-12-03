package com.example.myapplication.util;

import android.util.Log;

import com.example.myapplication.model.BenchmarkResult;
import android.opengl.GLSurfaceView;
import com.example.myapplication.gpu.MyGLRenderer;

public final class BenchmarkUtil {

    private static final String TAG = "BenchmarkUtil";

    public static BenchmarkResult measureExecutionTime(Runnable task, int iterations){
        if(task==null){
            throw new IllegalArgumentException("No task");
        }
        if(iterations<=0){
            throw new IllegalArgumentException("Not enough iterations");
        }
        int warmupIterations=50;
        for(int i=0; i<warmupIterations; i++){
            task.run();
        }
        long[] measurements=new long[iterations];
        long max=Long.MIN_VALUE;
        long min=Long.MAX_VALUE;
        long total=0;
        for(int i=0; i<iterations; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            measurements[i] = end - start;
            total+=measurements[i];
            if (measurements[i] > max) max = measurements[i];
            if (measurements[i] < min) min = measurements[i];
        }
        double average = (double) total / iterations;

        return new BenchmarkResult(measurements, max, min, average);
    }

    public static BenchmarkResult measureMemoryAccessTime(Runnable task, int iterations){
        if(task==null){
            throw new IllegalArgumentException("No task");
        }
        if(iterations<=0){
            throw new IllegalArgumentException("Not enough iterations");
        }
        int warmupIteration=50;
        for(int i=0; i<warmupIteration; i++){
            task.run();
        }
        long[] measurements= new long[iterations];
        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        long total = 0;
        for(int i = 0; i < iterations; i++){
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            measurements[i] = end - start;
            total += measurements[i];
            if((measurements[i]) > max) max = measurements[i];
            if((measurements[i]) < min) min = measurements[i];
        }
        double average = (double) total / iterations;
        return new BenchmarkResult(measurements,max, min, average);
    }

    public static BenchmarkResult measureGraphicsTime(GLSurfaceView glSurfaceView, MyGLRenderer renderer, int iterations) {
        Log.d(TAG, "measureGraphicsTime started with " + iterations + " iterations");

        if (glSurfaceView == null || renderer == null || iterations <= 0) {
            Log.e(TAG, "Invalid arguments for graphics benchmark");
            throw new IllegalArgumentException("Invalid arguments for graphics benchmark");
        }

        long[] measurements = new long[iterations];
        long max = Long.MIN_VALUE;
        long min = Long.MAX_VALUE;
        long total = 0;

        final Object lock = new Object();
        final long[] lastResult = { -1 };

        renderer.setListener(nanoTime -> {
            synchronized (lock) {
                lastResult[0] = nanoTime;
                Log.d(TAG, "Received result: " + nanoTime + "ns");
                lock.notifyAll();
            }
        });

        for (int i = 0; i < iterations; i++) {
            Log.d(TAG, "Starting iteration " + i);

            synchronized (lock) {
                lastResult[0] = -1;
            }

            glSurfaceView.queueEvent(() -> {
                Log.d(TAG, "queueEvent executed, calling startMeasurement");
                renderer.startMeasurement();
            });

            glSurfaceView.requestRender();
            Log.d(TAG, "requestRender called");

            synchronized (lock) {
                long waitStart = System.currentTimeMillis();
                while (lastResult[0] == -1) {
                    try {
                        lock.wait(5000); // 5 second timeout
                        if (lastResult[0] == -1) {
                            long waitTime = System.currentTimeMillis() - waitStart;
                            Log.e(TAG, "Timeout waiting for result after " + waitTime + "ms");
                            renderer.setListener(null);
                            return null;
                        }
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Interrupted while waiting", e);
                        Thread.currentThread().interrupt();
                        renderer.setListener(null);
                        return null;
                    }
                }
            }

            long nanoTime = lastResult[0];
            measurements[i] = nanoTime;
            total += nanoTime;
            if (nanoTime > max) max = nanoTime;
            if (nanoTime < min) min = nanoTime;

            Log.d(TAG, "Iteration " + i + " complete: " + nanoTime + "ns");
        }

        renderer.setListener(null);
        Log.d(TAG, "All iterations complete");

        double average = (double) total / iterations;
        return new BenchmarkResult(measurements, max, min, average);
    }
}