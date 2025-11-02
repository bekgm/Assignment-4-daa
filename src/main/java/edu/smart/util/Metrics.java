package edu.smart.util;

import java.util.HashMap;
import java.util.Map;

/** Simple metrics collector with counters and nanos timers. */
public class Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private final Map<String, Long> times = new HashMap<>();

    public void inc(String key){ counters.merge(key, 1L, Long::sum); }
    public void add(String key, long delta){ counters.merge(key, delta, Long::sum); }
    public long get(String key){ return counters.getOrDefault(key, 0L); }

    public void time(String key, Runnable r){
        long t0 = System.nanoTime();
        r.run();
        long t1 = System.nanoTime();
        times.merge(key, t1 - t0, Long::sum);
    }
    public long nanos(String key){ return times.getOrDefault(key, 0L); }
}
