package delay_queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayObject<T> implements Delayed {
    private final T obj;
    private final long availableTime;

    public DelayObject(T obj, long delayTime) {
        this.obj = obj;
        this.availableTime = System.currentTimeMillis() + delayTime;
    }

    /**
     * 获取对象
     */
    public T getObj() {
        return obj;
    }

    /**
     * 获取delay
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(availableTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 排序
     */
    @Override
    public int compareTo(Delayed o) {
        long l = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        return l == 0 ? 0 : l > 0 ? 1 : -1;
    }
}
