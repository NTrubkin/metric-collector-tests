package ru.ntrubkin.collector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ru.ntrubkin.Metric;

public class CopyOnWriteListMetricCollector {

    private final List<Metric> metrics = new CopyOnWriteArrayList<>();

    public void add(Metric metric) {
        metrics.add(metric);
    }

    public List<Metric> collect() {
        Iterator<Metric> iterator = metrics.iterator();
        metrics.clear();
        List<Metric> result = new LinkedList<>();
        iterator.forEachRemaining(result::add);
        return result;
    }

    public int getMetricsSize() {
        return metrics.size();
    }
}
