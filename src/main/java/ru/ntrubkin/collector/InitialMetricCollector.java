package ru.ntrubkin.collector;

import java.util.ArrayList;
import java.util.List;
import ru.ntrubkin.Metric;

public class InitialMetricCollector {

    private final List<Metric> metrics = new ArrayList<>();

    public void add(Metric metric) {
        metrics.add(metric);
    }

    public List<Metric> collect() {
        List<Metric> result = new ArrayList<>(metrics);
        metrics.clear();
        return result;
    }

    public int getMetricsSize() {
        return metrics.size();
    }
}
