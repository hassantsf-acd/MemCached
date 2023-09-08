import java.util.concurrent.locks.ReentrantLock;

public class PullingThread implements Runnable {
    private final MemCached memory;
    private final ReentrantLock lock;

    PullingThread(MemCached memory, ReentrantLock lock) {
        this.lock = lock;
        this.memory = memory;

    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                for (Slab slab : memory.getSlabs()) {
                    var pageTableEntries = slab.getPageTableEntries();
                    for (var entry :
                    pageTableEntries) {
                        var key = entry.getKey();
                        var chuckAddress = entry.getValue();
                        var chunk = slab.getChunk(chuckAddress);

                        if (chunk != null && chunk.isExpired()) {
                            slab.clearChunk(key);
                        }
                    }
                }
                memory.logger(LogType.PULL);

            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
