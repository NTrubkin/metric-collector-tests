package ru.ntrubkin.collector;

import java.util.ArrayList;
import java.util.List;
import ru.ntrubkin.Metric;

public class SynchronizedMetricCollector {

    private final List<Metric> metrics = new ArrayList<>();

    public synchronized void add(Metric metric) {
        metrics.add(metric);
    }

    public synchronized List<Metric> collect() {
        List<Metric> result = new ArrayList<>(metrics);
        metrics.clear();
        return result;
    }

    public int getMetricsSize() {
        return metrics.size();
    }
}
