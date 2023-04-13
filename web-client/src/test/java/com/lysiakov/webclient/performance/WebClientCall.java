package com.lysiakov.webclient.performance;

import com.google.common.base.Stopwatch;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class WebClientCall<T> implements Callable<BigDecimal[]> {

  private final Supplier<T> supplier;
  private final BigDecimal[] executionTime;

  public WebClientCall(Supplier<T> supplier, int numberOfExecutions) {
    this.supplier = supplier;
    executionTime = new BigDecimal[numberOfExecutions];
  }

  @Override
  public BigDecimal[] call() {
    for (int i = 0; i < executionTime.length; i++) {
      runAndMeasureExecutionTime(i);
    }
    return executionTime;
  }

  /**
   * Measure time taken to execute web-call.
   * Include (de)serialization time for both client and service and network time for a request
   * Write resulting time to array
   * @param index index of the current iteration
   */
  private void runAndMeasureExecutionTime(int index) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    supplier.get();
    executionTime[index] = BigDecimal.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

}
