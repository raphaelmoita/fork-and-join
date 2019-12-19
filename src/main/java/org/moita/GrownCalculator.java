package org.moita;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.RecursiveTask;

import static java.math.BigDecimal.ZERO;

public class GrownCalculator extends RecursiveTask<Map<String, List<BigDecimal>>>
{
    private Map<String, List<BigDecimal>> historicalRevenue;

    public GrownCalculator(Map<String, List<BigDecimal>> historicalRevenue)
    {
        assertNotEmpty(historicalRevenue);
        this.historicalRevenue = historicalRevenue;
    }

    private void assertNotEmpty(Map<String, List<BigDecimal>> historicalRevenue)
    {
        if (historicalRevenue == null || historicalRevenue.size() == 0)
        {
            throw new IllegalStateException("Invalid historical values!");
        }
    }

    @Override
    protected Map<String, List<BigDecimal>> compute()
    {
        Map<String, List<BigDecimal>> growns = new HashMap<>();

        Iterator<Map.Entry<String, List<BigDecimal>>> iterator =
                historicalRevenue.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, List<BigDecimal>> entry = iterator.next();

            while (iterator.hasNext())
            {
                Map.Entry<String, List<BigDecimal>> entryTask = iterator.next();
                Map<String, List<BigDecimal>> mapTask = new HashMap<>();
                mapTask.put(entryTask.getKey(), entryTask.getValue());
                GrownCalculator task = new GrownCalculator(mapTask);
                task.fork();
                growns.putAll(task.join());
            }

            growns.put(entry.getKey(), grown(entry.getValue()));
        }

        return growns;
    }

    private List<BigDecimal> grown(List<BigDecimal> historical)
    {
        BigDecimal previous = ZERO;

        List<BigDecimal> grown = new ArrayList<>();

        for (BigDecimal current : historical) {

            grown.add(current.subtract(previous));

            previous = current;
        }

        return grown;
    }
}
