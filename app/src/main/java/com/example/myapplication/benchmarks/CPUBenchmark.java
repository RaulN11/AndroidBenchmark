package com.example.myapplication.benchmarks;

public class CPUBenchmark {
    public static long testCpuPerformance() {
        long start = System.nanoTime();
        double result = 0.0;
        for (int i = 1; i <= 10_000_000; i++) {
            result += Math.sqrt(i) * Math.sin(i) * Math.cos(i / 2.0);
        }
        long end = System.nanoTime();
        return end-start;
    }
}
