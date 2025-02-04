package course.concurrency.m3_shared.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;
    private final Object lock = new Object();

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);

    private volatile boolean stopped = false;

    public boolean propose(Bid bid) {
        if (bid.getPrice() > latestBid.getPrice() && !stopped) {
            synchronized (lock) {
                if (bid.getPrice() > latestBid.getPrice() && !stopped) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        synchronized (lock) {
            stopped = true;
            return latestBid;
        }
    }
}
