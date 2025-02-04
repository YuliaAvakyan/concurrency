package course.concurrency.m3_shared.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private Notifier notifier;
    private AtomicMarkableReference<Bid> latestBid;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, 0L), false);
    }

    public boolean propose(Bid bid) {
        Bid current;
        do {
            if (latestBid.isMarked()) {
                return false;
            }
            current = latestBid.getReference();
            if (bid.getPrice() <= current.getPrice()) {
                return false;
            }
        } while (!latestBid.compareAndSet(current, bid, false, false));

        notifier.sendOutdatedMessage(current);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {Bid latest;
        do {
            latest = latestBid.getReference();
        } while(!latestBid.attemptMark(latest, true));
        return latest;
    }
}
