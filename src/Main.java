import java.util.concurrent.Semaphore;

public class Main {

    public void oneAndTwo(int iterations) {

        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);


        Thread t1 = new Thread(() -> {
            for (int i = 0; i < (iterations / 2); i++) {
                try {
                    s1.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("1");
                s2.release();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < (iterations / 2); i++) {
                try {
                    s2.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("2");
                s1.release();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {

        Main main = new Main();
        main.oneAndTwo(6);

    }
}