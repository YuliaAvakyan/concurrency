package course.concurrency.m3_shared;

public class Counter {
    static final Object lock = new Object();
    static volatile int counter = 1;

    public static void first() throws InterruptedException {
        synchronized (lock) {
                while (counter != 1) {
                    lock.wait();
                }
                System.out.println(counter);
                counter = 2;
                lock.notifyAll();
        }
    }

    public static void second() throws InterruptedException {
        synchronized (lock) {
                while (counter != 2) {
                    lock.wait();
                }
                System.out.println(counter);
                counter = 3;
                lock.notifyAll();
        }

    }

    public static void third() throws InterruptedException {
        synchronized (lock) {
                while (counter != 3) {
                    lock.wait();
                }
                System.out.println(counter);
                counter = 1;
                lock.notifyAll();
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
          try {
            first();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });

        Thread t2 = new Thread(() -> {
          try {
            second();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });

        Thread t3 = new Thread(() -> {
          try {
            third();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });

        t1.start();
        t2.start();
        t3.start();
    }

//    public static volatile int current = 0;
//
//    public static void run(int value) {
//        value--;
//        while (true) {
//            synchronized (lock) {
//                while (current != value) {
//                    try {
//                        lock.wait();
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                System.out.println(value + 1);
//                current = (current + 1) % 3;
//                lock.notifyAll();
//            }
//        }
//    }
//
//    public static void first() {
//        run(1);
//    }
//
//    public static void second() {
//        run(2);
//    }
//
//    public static void third() {
//        run(3);
//    }

}
