package com.bwsoft.athena.jmx;

public interface QueueSamplerMXBean { 
    public QueueSample getQueueSample(); 
    public void clearQueue(); 
} 