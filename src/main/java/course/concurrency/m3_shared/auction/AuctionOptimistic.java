package course.concurrency.m3_shared.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private Notifier notifier;
    private AtomicReference<Bid> latestBid;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
        latestBid = new AtomicReference<>(new Bid(0L, 0L, 0L));
    }


    public boolean propose(Bid bid) {
        Bid current;
        do {
            current = latestBid.get();
            if (bid.getPrice() <= current.getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(current, bid));

        notifier.sendOutdatedMessage(current);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }
}
