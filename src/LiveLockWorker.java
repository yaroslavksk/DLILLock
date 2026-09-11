import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public final class LiveLockWorker implements Runnable {

    private final String name;
    private final Lock first;
    private final Lock second;
    private final CyclicBarrier startBarrier;
    private final CyclicBarrier firstLockBarrier;
    private final CyclicBarrier endBarrier;

    LiveLockWorker(String name, Lock first,
                   Lock second,
                   CyclicBarrier startBarrier,
                   CyclicBarrier firstLockBarrier,
                   CyclicBarrier endBarrier) {
        this.name = name;
        this.first = first;
        this.second = second;
        this.startBarrier = startBarrier;
        this.firstLockBarrier = firstLockBarrier;
        this.endBarrier = endBarrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                startBarrier.await();
                if (first.tryLock()) {
                    try {
                        firstLockBarrier.await();
                        if (second.tryLock()) {
                            try {
                                System.out.println("Поток " + name + " выполнил работу");
                            } finally {
                                second.unlock();
                            }
                        }
                        endBarrier.await();
                    } finally {
                        first.unlock();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}