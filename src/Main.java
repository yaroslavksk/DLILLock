public class Main {

    private int i = 0;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

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

    public void deadLock() {
        Thread thread1 = new Thread(this::print1D, "1");
        Thread thread2 = new Thread(this::print2D, "2");

        thread1.start();
        thread2.start();

        joinQuietly(thread1);
        joinQuietly(thread2);
    }

    public void print1D() {
        synchronized (lock1) {
            sleepQuietly(100);
            synchronized (lock2) {
                System.out.println("1");
                i++;
            }
        }
    }

    public void print2D() {
        synchronized (lock2) {
            sleepQuietly(100);
            synchronized (lock1) {
                System.out.println("2");
                i--;
            }
        }
    }

    public static void main(String[] args) {


    }
}