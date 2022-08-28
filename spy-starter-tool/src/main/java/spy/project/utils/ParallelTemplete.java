package spy.project.utils;

import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发执行工具模版
 * 支持多线程调度拆分的子任务，并返回结果
 * 基于condownlanch实现
 *
 * 场景：例如请求多个api，最后组装数据，可并行执行
 *
 */
public class ParallelTemplete {

    private final ExecutorService executorService;
    private final CountDownLatch countDownLatch;
    private List<FT> taskWorkList;
    private AtomicInteger count;

    public ParallelTemplete(@NonNull ExecutorService executorService, Integer n) {
        this.executorService = executorService;
        this.countDownLatch = new CountDownLatch(n);
        this.taskWorkList = new ArrayList<>();
        count = new AtomicInteger(n);
    }

    // 开始执行任务
    public List<FT> startWorks() throws InterruptedException {
        taskWorkList.forEach(t -> {
            t.setFuture(executorService.submit(t.getFutureTask()));
        });
        countDownLatch.await();
        return taskWorkList;
    }

    // 添加任务
    public <T extends TaskTemplete> boolean addWorks(T futureTask, Class clz) {
        boolean flag = false;
        if(taskWorkList.size() < count.get()) {
            futureTask.setCountDownLatch(countDownLatch);
            taskWorkList.add(new FT(futureTask, clz));
            flag = true;
        }
        return flag;
    }

    // 任务模版类，所有并行任务实现模版方法
    public abstract static class TaskTemplete implements Callable {

        private CountDownLatch countDownLatch;
        public abstract Object dowork();

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Object call() throws Exception {
            Object res = dowork();
            System.out.println(Thread.currentThread().getId() + " countdown");
            countDownLatch.countDown();
            return res;
        }
    }

    public class FT<T extends TaskTemplete> {
        private T futureTask;
        private Future future;
        private Class clz;

        public <T> T getResult() throws ExecutionException, InterruptedException {
            return (T) future.get();
        }

        public FT(T futureTask, Class clz) {
            this.futureTask = futureTask;
            this.clz = clz;
        }

        public T getFutureTask() {
            return futureTask;
        }

        public void setFutureTask(T futureTask) {
            this.futureTask = futureTask;
        }

        public Class getClz() {
            return clz;
        }

        public void setClz(Class clz) {
            this.clz = clz;
        }

        public Future getFuture() {
            return future;
        }

        public void setFuture(Future future) {
            this.future = future;
        }
    }

}
