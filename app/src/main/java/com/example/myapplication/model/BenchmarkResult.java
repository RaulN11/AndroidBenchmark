package com.example.myapplication.model;

public class BenchmarkResult {
    public final long[] runtimes;
    public final long max_time;
    public final long min_time;
    public final double average;
    public BenchmarkResult(long[] runtimes, long max_time, long min_time, double average){
        this.runtimes=runtimes;
        this.max_time=max_time;
        this.min_time=min_time;
        this.average=average;
    }
    public long[] getRuntimes(){
        return runtimes;
    }
    public long getMax_time(){
        return max_time;
    }
    public long getMin_time(){
        return min_time;
    }
    public double getAverage(){
        return average;
    }
}
