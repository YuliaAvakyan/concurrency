package course.concurrency.m3_shared.collections;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class RestaurantService {

    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private Map<String, Long> stat = new ConcurrentHashMap<>();
//    private final Map<String, LongAdder> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
      stat.merge(restaurantName, 1L, (k,v) -> k + 1);
//      stat.computeIfAbsent(restaurantName, k -> new LongAdder()).increment();
        // your code
    }

    public Set<String> printStat() {
        // your code
        return stat.entrySet()
            .stream()
            .map(stringLongAdderEntry -> stringLongAdderEntry.getKey() + " - " + stringLongAdderEntry.getValue().longValue())
            .collect(toSet());
    }
}
