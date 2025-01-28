package course.concurrency.m3_shared.auction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifier {
    ExecutorService executor = Executors.newFixedThreadPool(1000);

    public void sendOutdatedMessage(Bid bid) {
        executor.execute(this::imitateSending);
//        }
    }

    private void imitateSending() {
        // don't remove this delay, deal with it properly
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    public void shutdown() {}
}
