package course.concurrency.m2_async.loadTest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class ReportServiceExecutors {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private LoadGenerator loadGenerator = new LoadGenerator();

    public Others.Report getReport() {
        Future<Collection<Others.Item>> iFuture =
                executor.submit(() -> getItems());
        Future<Collection<Others.Customer>> customersFuture =
                executor.submit(() -> getActiveCustomers());

        try {
            Collection<Others.Customer> customers = customersFuture.get();
            Collection<Others.Item> items = iFuture.get();
            return combineResults(items, customers);
        } catch (ExecutionException | InterruptedException ex) {}

        return new Others.Report();
    }

    private Others.Report combineResults(Collection<Others.Item> items, Collection<Others.Customer> customers) {
        return new Others.Report();
    }

    private Collection<Others.Customer> getActiveCustomers() {
        loadGenerator.work();
        loadGenerator.work();
        return List.of(new Others.Customer(), new Others.Customer());
    }

    private Collection<Others.Item> getItems() {
        loadGenerator.work();
        return List.of(new Others.Item(), new Others.Item());
    }

    public void shutdown() {
        executor.shutdown();
    }
}


// 4 ядра
// блокирующая нагрузка - sleep()
// newFixedThreadPool(4) - Execution time: 137260
// newFixedThreadPool(8) - Execution time: 69379
// newFixedThreadPool(16) - Execution time: 36144
// newFixedThreadPool(24) - Execution time: 24144
// newFixedThreadPool(32) - Execution time: 19647

// newCachedThreadPool() - Execution time: 15091

// newSingleThreadExecutor() - Execution time: 300012


// вычислительная нагрузка - compute()
// newFixedThreadPool(4) - Execution time: 9403
// newFixedThreadPool(8) - Execution time: 9086
// newFixedThreadPool(16) - Execution time: 9902
// newFixedThreadPool(24) - Execution time: 9836
// newFixedThreadPool(32) - Execution time: 9118

// newCachedThreadPool() - Execution time: 10550

// newSingleThreadExecutor() - Execution time: 16142