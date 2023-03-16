# Metric collector tests

## Предыстория
Появилась потребность написать MetricCollector.
Его назначение - принимать метрики на хранение во внутренний буфер, отдать все метрики по требованию.
Изначально было написано решение `InitialMetricCollector`.
Выяснилось, что метрики могут быть собраны параллельно добавлению новых.
Был предложен ряд решений проблемы.
В этом репозитории тестируются эти решения с помощью `jcstress`

## Пояснения кода
В каждый коллектор были добавлен метод `getMetricsSize()`.
Причина - Размер буфера коллектора является одним из результатов теста и его необходимо проверить.
На тесты новый метод влиять не должен

Далее идет краткое объяснение апи `jcstress`.
В качестве альтернативы можно прочесть [статью на jfeatures.com](https://jfeatures.com/blog/jcstress)

Методы тестов, помеченные `@Actor`, имитируют реальное использование коллектора.
В них также могут записывать некоторые результаты, отлавливаться исключения.
Они выполняются параллельно друг другу.

Методы тестов, помеченные `@Arbiter`, необходимы только для записи оставшихся результатов.
Они не участвуют в тестировании, выполняются после всех `@Actor` методов.

Классы `*_Result` - это хранилище результатов.
Они состоят из фиксированного количества ячеек определенных типов.
Например, класс `IIZ_Result` - это хранилище результатов `int`, `int`, `bool`
В тесте `InitialMetricCollectorTest`:
- в первую ячейку `int` кладется количество оставшихся метрик в коллекторе
- во вторую ячейку `int` кладется количество собранных метрик
- в третью ячейку `bool` кладется ответ на вопрос, упал ли актор сбора метрик

В `@Outcome` определяются различные комбинации результатов.
- ACCEPTABLE - Полностью корректное поведение тестируемого кода.
- ACCEPTABLE_INTERESTING - Некорректное поведение тестируемого кода. Хотя сам тест считается "пройденным".
- FORBIDDEN - Некорректное и неожиданное поведение, проваливающее тест.

## Запуск
Сборка
```shell
mvn clean package
```

Запуск всех тестов
```shell
java -jar target/jcstress.jar -v 
```

Запуск тестов по regex "содержит InitialMetricCollector"
```shell
java -jar target/jcstress.jar -v -t ".*InitialMetricCollector.*" 
```

## Результаты
Подробный отчет в папке `results`
```
RUN RESULTS:
  Interesting tests: 2 matching test results.

.......... [OK] ru.ntrubkin.test.CopyOnWriteListMetricCollectorTest

  Results across all configurations:

  RESULT        SAMPLES     FREQ       EXPECT  DESCRIPTION
    0, 0     32,468,804    0.70%  Interesting  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
    0, 1  2,376,210,936   51.02%   Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
    1, 0  2,249,025,756   48.29%   Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику

.......... [OK] ru.ntrubkin.test.InitialMetricCollectorTest

  Results across all configurations:

       RESULT        SAMPLES     FREQ       EXPECT  DESCRIPTION
  0, 0, false     80,142,912    1.48%  Interesting  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
   0, 0, true         16,948   <0.01%  Interesting  Упали во время сбора накопленных метрик
  0, 1, false  3,380,382,891   62.27%   Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
  1, 0, false  1,968,357,625   36.26%   Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику


  Failed tests: No matches.

  Error tests: No matches.

  All remaining tests: 4 matching test results.

.......... [OK] ru.ntrubkin.test.QueueMetricCollectorTest

  Results across all configurations:

  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0              0    0.00%   Forbidden  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
    0, 1  3,196,296,901   54.46%  Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
    1, 0  2,672,739,155   45.54%  Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику

.......... [OK] ru.ntrubkin.test.SwapReferenceMetricCollectorTest

  Results across all configurations:

  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0              0    0.00%   Forbidden  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
    0, 1  2,261,104,992   32.62%  Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
    1, 0  4,670,279,864   67.38%  Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику

.......... [OK] ru.ntrubkin.test.SwapReferenceNoVolatileMetricCollectorTest

  Results across all configurations:

  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0              0    0.00%   Forbidden  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
    0, 1  2,096,857,616   29.30%  Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
    1, 0  5,059,141,640   70.70%  Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику

.......... [OK] ru.ntrubkin.test.SynchronizedMetricCollectorTest

  Results across all configurations:

  RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
    0, 0              0    0.00%   Forbidden  Метрика пропала - ее нет ни в коллекторе, ни среди собранных
    0, 1  2,607,366,227   51.01%  Acceptable  Сначала добавили новую метрику, затем собрали накопленные метрики
    1, 0  2,503,623,109   48.99%  Acceptable  Сначала собрали накопленные метрики, затем добавили новую метрику
```
