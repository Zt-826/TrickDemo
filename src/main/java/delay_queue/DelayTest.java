package delay_queue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DelayTest {
    public static void main(String[] args) {
        // 创建延迟队列
        DelayQueue<DelayObject<User>> queue = new DelayQueue<>();

        // 填充队列
        fillQueue(queue);

        // 线程池执行任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            while (true) {
                try {
                    DelayObject<User> delayObject = queue.take();
                    System.out.printf("Take user from delayed queue %s:%s at %s%n", delayObject.getObj().getId(),
                            delayObject.getObj().getName(),
                            LocalDateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void fillQueue(DelayQueue<DelayObject<User>> queue) {
        List<User> users = Arrays.asList(new User("1", "Alice"),
                new User("2", "Bob"),
                new User("3", "Celina"));

        // 初始延迟
        int delay = 5000;
        for (User user : users) {
            // 放入延迟队列
            System.out.printf("Begin to offer user to delayed queue %s:%s at %s%n", user.getId(),
                    user.getName(), LocalDateTime.now());
            queue.offer(new DelayObject<>(user, delay));
            delay += 5000;
        }
    }
}
