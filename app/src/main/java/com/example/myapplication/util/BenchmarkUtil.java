package com.example.myapplication.util;

import com.example.myapplication.model.BenchmarkResult;
import android.opengl.GLSurfaceView;
import com.example.myapplication.gpu.MyGLRenderer;

public final class BenchmarkUtil {
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
        if (glSurfaceView == null || renderer == null || iterations <= 0) {
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
                lock.notifyAll();
            }
        });

        for (int i = 0; i < iterations; i++) {

            synchronized (lock) {
                lastResult[0] = -1;
            }

            glSurfaceView.queueEvent(renderer::startMeasurement);
            glSurfaceView.requestRender();

            synchronized (lock) {
                while (lastResult[0] == -1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
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
        }

        renderer.setListener(null);

        double average = (double) total / iterations;
        return new BenchmarkResult(measurements, max, min, average);
    }
}