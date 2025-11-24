package com.example.myapplication.util;

import com.example.myapplication.model.BenchmarkResult;

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
}
