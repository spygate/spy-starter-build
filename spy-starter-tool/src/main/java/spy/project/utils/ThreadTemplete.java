package spy.project.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 有关闭功能的线程模版，适合不用线程池单线程执行的任务
 */
public abstract class ThreadTemplete implements Runnable {

    private Thread worker;
    private AtomicBoolean run = new AtomicBoolean(false);

    public abstract void dowork();

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        run.set(false);
    }

    public void interrupt() {
        run.set(false);
        worker.interrupt();
    }

    @Override
    public void run() {
        run.set(true);
        while(run.get()) {
            try {
                dowork();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.out.println("thread interrupt");
            }
        }
    }

}
