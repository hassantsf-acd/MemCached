import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Reader implements Runnable {
    private final MemCached memory;
    private final List<String> dataKeys;
    private final ReentrantLock listLock;
    private final Random random;

    public Reader(MemCached memory, List<String> dataKeys, ReentrantLock listLock) {
        this.memory = memory;
        this.dataKeys = dataKeys;
        this.listLock = listLock;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            listLock.lock();
            try {
                if (dataKeys.isEmpty()) {
                    continue;
                }

                int index = random.nextInt(dataKeys.size());

                String key = dataKeys.get(index);
                try {
                    memory.read(key);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } finally {
                listLock.unlock();
            }
            memory.logger(LogType.READ);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}