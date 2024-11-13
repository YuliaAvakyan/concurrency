package course.concurrency.m2_async.loadTest;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ReportServiceCF {

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    private LoadGenerator loadGenerator = new LoadGenerator();

    public Others.Report getReport() {
        CompletableFuture<Collection<Others.Item>> itemsCF =
                CompletableFuture.supplyAsync(() -> getItems(), executor);

        CompletableFuture<Collection<Others.Customer>> customersCF =
                CompletableFuture.supplyAsync(() -> getActiveCustomers(), executor);

        CompletableFuture<Others.Report> reportTask =
                customersCF.thenCombine(itemsCF,
                        (customers, orders) -> combineResults(orders, customers));

        return reportTask.join();
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
// ForkJoinPool.commonPool() - Execution time: 80034
// newFixedThreadPool(4) - Execution time: 137365
// newFixedThreadPool(32) - Execution time: 19651

// newCachedThreadPool() - Execution time: 15137


// вычислительная нагрузка - compute()
// ForkJoinPool.commonPool() - Execution time: 8064
// newFixedThreadPool(4) - Execution time: 10343
// newFixedThreadPool(32) - Execution time: 9927

// newCachedThreadPool() - Execution time: 9723


//CompletableFuture - просто удобный апи по работе с подзадачами, а всю работу выполняют те же экзекьюторы. Так что зависимости между типом задачи, числом ядер и количеством задач такие же.
//
//Когда подойдёт экзекьютор по умолчанию?
//Так как в стандартном экзекьюторе число потоков почти равно числу ядер, он идеально подойдёт для вычислительных задач.
//
//Если в CompletableFuture отправляются блокирующие задачи, лучше определить свой экзекьютор.
// Если нагрузка небольшая, и одновременно выполняются немного задач, то подойдёт
//
//executor = Executors.newCachedPool();
//Если нагрузка серьёзная, подбираем подходящий fixed пул, чтобы потоки не занимали много памяти и пропускная способность оставалась на уровне.