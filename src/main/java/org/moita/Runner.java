package org.moita;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class Runner {

    public static void main(String[] args)
    {
        ForkJoinPool pool = new ForkJoinPool();

        GrownCalculator grownCalculator = new GrownCalculator(values());

        pool.execute(grownCalculator);

        do
        {
            System.out.println("******************************************");
            System.out.println(format("Main: Parallelism: %d", pool.getParallelism()));
            System.out.println(format("Main: Active Threads: %d", pool.getActiveThreadCount()));
            System.out.println(format("Main: Task Count: %d", pool.getQueuedTaskCount()));
            System.out.println(format("Main: Steal Count: %d", pool.getStealCount()));
            System.out.println("******************************************");

            try
            {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
        while ((!grownCalculator.isDone()));

        pool.shutdown();

        Map<String, List<BigDecimal>> growns = grownCalculator.join();

        showGrownById(growns);
    }

    private static void showGrownById(Map<String, List<BigDecimal>> growns)
    {
        //growns.forEach((key, value) -> System.out.println(key + " | " + value));
    }

    private static Map<String, List<BigDecimal>> values()
    {
        Map<String, List<BigDecimal>> values = new HashMap<>();

        for (int i = 0; i < 1000000; i++)
        {
            values.put(UUID.randomUUID().toString(), asList(
                    new BigDecimal("1"),
                    new BigDecimal("3"),
                    new BigDecimal("4"),
                    new BigDecimal("8"),
                    new BigDecimal("11"),
                    new BigDecimal("14"),
                    new BigDecimal("21"),
                    new BigDecimal("31"),
                    new BigDecimal("33")));
        }

        return values;
    }
}
