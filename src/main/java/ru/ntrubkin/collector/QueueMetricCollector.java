package ru.ntrubkin.collector;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import ru.ntrubkin.Metric;

public class QueueMetricCollector {

    private final Queue<Metric> metrics = new ConcurrentLinkedDeque<>();

    public void add(Metric metric) {
        metrics.add(metric);
    }

    public List<Metric> collect() {
        List<Metric> result = new LinkedList<>();
        while (!metrics.isEmpty()) {
            result.add(metrics.poll());
        }
        return result;
    }

    public int getMetricsSize() {
        return metrics.size();
    }
}
