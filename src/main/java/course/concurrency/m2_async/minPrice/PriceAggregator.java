package course.concurrency.m2_async.minPrice;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PriceAggregator {
    private ExecutorService executor = Executors.newCachedThreadPool();

  private static final int DEFAULT_TIMEOUT = 2900;

  private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        // place for your code

      List<CompletableFuture<Double>> completableFutures = shopIds.stream()
          .map(shopId -> CompletableFuture.supplyAsync(() -> priceRetriever.getPrice(shopId, itemId), executor)
          .completeOnTimeout(Double.POSITIVE_INFINITY, 2900, TimeUnit.MILLISECONDS)
          .exceptionally(ex -> Double.POSITIVE_INFINITY))
          .toList();

      CompletableFuture
          .allOf(completableFutures.toArray(CompletableFuture[]::new))
          .join();

      return completableFutures
          .stream()
          .mapToDouble(CompletableFuture::join)
          .filter(Double::isFinite)
          .min()
          .orElse(Double.NaN);

//      return shopIds.stream()
//          .map(shopId -> CompletableFuture
//              .supplyAsync(() -> priceRetriever.getPrice(itemId, shopId), executor)
//              .exceptionally(ex -> Double.POSITIVE_INFINITY)
//              .completeOnTimeout(Double.POSITIVE_INFINITY, 2900, TimeUnit.MILLISECONDS)
//          )
//          .reduce((future1, future2) -> future1.thenCombine(future2, Double::min))
//          .map(CompletableFuture::join)
//          .filter(Double::isFinite)
//          .orElse(Double.NaN);
    }
}
