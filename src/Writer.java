import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Writer implements Runnable {
    private final MemCached memory;
    private final List<String> dataKeys;
    private final ReentrantLock listLock;
    private final Random random;

    public Writer(MemCached memory, List<String> dataKeys, ReentrantLock listLock) {
        this.memory = memory;
        this.dataKeys = dataKeys;
        this.random = new Random();
        this.listLock = listLock;
    }

    @Override
    public void run() {
        while (true) {
            int value = random.nextInt(1000);
            int maxValue = memory.getSlabs().get(memory.getSlabs().size() - 1).getChunkSize();

            int size = random.nextInt(maxValue + 1);

            try {
                String key = memory.write(value, size);

                listLock.lock();
                try {
                    dataKeys.add(key);
                } finally {
                    listLock.unlock();
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }

            memory.logger(LogType.WRITE);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

