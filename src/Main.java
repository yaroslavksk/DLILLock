import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Main {

    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void joinQuietly(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void liveLock() {
        Lock first = new ReentrantLock();
        Lock second = new ReentrantLock();

        CyclicBarrier startBarrier = new CyclicBarrier(2);
        CyclicBarrier firstLockBarrier = new CyclicBarrier(2);
        CyclicBarrier endBarrier = new CyclicBarrier(2);

        Thread thread1 = new Thread(new LiveLockWorker("1",
                first,
                second,
                startBarrier,
                firstLockBarrier,
                endBarrier),
                "1");
        Thread thread2 = new Thread(new LiveLockWorker("2",
                second,
                first,
                startBarrier,
                firstLockBarrier,
                endBarrier),
                "2");

        thread1.start();
        thread2.start();

        joinQuietly(thread1);
        joinQuietly(thread2);
    }

    public static void main(String[] args) {
        liveLock();
    }
}