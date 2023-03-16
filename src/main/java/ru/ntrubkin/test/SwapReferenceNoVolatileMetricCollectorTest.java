/*
 * Copyright (c) 2017, Red Hat Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package ru.ntrubkin.test;

import java.util.List;
import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;
import ru.ntrubkin.Metric;
import ru.ntrubkin.collector.SwapReferenceNoVolatileMetricCollector;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;


// Результат: {количество оставшихся метрик в коллекторе}, {количество собранных метрик}
@JCStressTest
@Outcome(id = "1, 0",
         expect = ACCEPTABLE,
         desc = "Сначала собрали накопленные метрики, затем добавили новую метрику")

@Outcome(id = "0, 1",
         expect = ACCEPTABLE,
         desc = "Сначала добавили новую метрику, затем собрали накопленные метрики")

@Outcome(id = "0, 0",
         expect = FORBIDDEN,
         desc = "Метрика пропала - ее нет ни в коллекторе, ни среди собранных")

@Outcome(expect = FORBIDDEN,
         desc = "Остальные результаты не допустимы")
@State
public class SwapReferenceNoVolatileMetricCollectorTest {

    private final SwapReferenceNoVolatileMetricCollector collector = new SwapReferenceNoVolatileMetricCollector();
    private List<Metric> collectedMetrics;

    @Actor
    public void actor1() {
        collector.add(new Metric());
    }

    @Actor
    public void actor2() {
        collectedMetrics = collector.collect();
    }

    @Arbiter
    public void arbiter(II_Result result) {
        result.r1 = collector.getMetricsSize();
        result.r2 = collectedMetrics.size();
    }
}
