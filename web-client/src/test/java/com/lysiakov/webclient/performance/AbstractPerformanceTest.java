package com.lysiakov.webclient.performance;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractPerformanceTest {

  private final static double SECOND_IN_MILLISECONDS = 60000;

  private final int NUMBER_OF_THREADS;
  private final int NUMBER_OF_EXECUTIONS;

  private ExecutorService executorService;

  public AbstractPerformanceTest() {
    NUMBER_OF_THREADS = 10;
    NUMBER_OF_EXECUTIONS = 100;
  }

  public AbstractPerformanceTest(int numberOfThreads, int numberOfExecutions) {
    NUMBER_OF_THREADS = numberOfThreads;
    NUMBER_OF_EXECUTIONS = numberOfExecutions;
  }

  @BeforeEach
  public void init() {
    executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  }

  protected <T> void runWebClientCallConcurrently(String testName, Supplier<T> supplier) throws InterruptedException, ExecutionException {

    // Create callable list to repeatedly run the same web-client call
    var callableList = createCallableList(supplier);
    // Run all threads at the same time
    List<Future<BigDecimal[]>> futures = executorService.invokeAll(callableList);
    // Collect resulting array contains request time per every iteration
    List<BigDecimal[]> resultingArrays = new ArrayList<>(NUMBER_OF_THREADS);
    for (Future<BigDecimal[]> future : futures) {
      resultingArrays.add(future.get());
    }
    // Measure average time of every indexed call
    BigDecimal[] averageResults = calculateAverageExecution(resultingArrays);
    printResults(testName, averageResults);
  }

  private <T> List<WebClientCall<T>> createCallableList(Supplier<T> supplier) {
    return IntStream.range(0, NUMBER_OF_THREADS)
        .mapToObj(i -> new WebClientCall<>(supplier, NUMBER_OF_EXECUTIONS))
        .toList();
  }

  /**
   * Measuring average execution time per every indexed web-client call within all threads (clients)
   * Example:
   * Thread 1: [5,10,20]
   * Thread 2: [3,4,6]
   * Thread 3: [2,9,12]
   * resulting array: [3.333,7.666,12.666]
   *
   * @return resulting array with average time of indexed web-client call
   */
  private BigDecimal[] calculateAverageExecution(List<BigDecimal[]> resultingCollection) {
    // convert to arrays for simplicity
    BigDecimal[][] arrays = resultingCollection.toArray(new BigDecimal[NUMBER_OF_THREADS][NUMBER_OF_EXECUTIONS]);

    // take the first array as starting point
    BigDecimal[] sums = arrays[0].clone();

    for (int i = 1; i < arrays.length; i++) {
      // how can we take average if the inputs are different lengths!
      if (arrays[i].length != sums.length) {
        throw new RuntimeException("Input arrays are of differing dimensions!");
      }
      // add this array to our running sum
      for (int j = 0; j < sums.length; j++) {
        sums[j] = sums[j].add(arrays[i][j]);
      }
    }

    BigDecimal[] averages = new BigDecimal[sums.length];
    // calculate average per request
    BigDecimal numberOfArrays = BigDecimal.valueOf(arrays.length);
    for (int k = 0; k < sums.length; k++) {

      averages[k] = sums[k].divide(numberOfArrays, MathContext.DECIMAL64);
    }
    return averages;
  }

  private void printResults(String testName, BigDecimal[] averageResults) {
    System.out.println(testName + " finished");
    System.out.println("Total number of threads: " + NUMBER_OF_THREADS);
    System.out.println("Total number of requests per thread: " + NUMBER_OF_EXECUTIONS);
    System.out.println("Total number of requests per test: " + NUMBER_OF_THREADS * NUMBER_OF_EXECUTIONS);
    double averageExecutionTime = averageRequestTime(averageResults);
    System.out.println("Average execution for single call is: " + averageExecutionTime);
    System.out.println("Average number of requests per second " + (int)(SECOND_IN_MILLISECONDS / averageExecutionTime));

/*    System.out.println("Execution time for charts");
    for (BigDecimal averageResult : averageResults) {
      System.out.println(averageResult.doubleValue());
    }*/
  }

  private double averageRequestTime(BigDecimal[] averageResults) {
    BigDecimal sum = Arrays.stream(averageResults).reduce(BigDecimal.ZERO, BigDecimal::add);
    return sum.divide(BigDecimal.valueOf(NUMBER_OF_EXECUTIONS), MathContext.DECIMAL64).doubleValue();
  }

}
