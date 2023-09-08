import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;


public class MemCached {
    private final ArrayList<Slab> slabs;
    private final ReentrantLock lock;

    public MemCached(ArrayList<Slab> slabs, ReentrantLock lock) {
        slabs.sort(Comparator.comparingInt(Slab::getChunkSize));
        this.slabs = slabs;
        this.lock = lock;
    }

    public int read(String key) throws Exception {
        lock.lock();
        try {
            for (var slab :
                    slabs) {
                if (slab.contains(key)) {
                    var value = slab.read(key);
                    System.out.println("Reader: Key " + key + " - Value " + value);
                    return value;
                }
            }
            throw new Exception("Address not found!");

        } finally {
            lock.unlock();
        }

    }

    public String write(int value, int size) throws Exception {
        try {
            lock.lock();
            var bestFitSlab = selectBestFitSlab(size);
            var key = bestFitSlab.write(value, size);
            System.out.println("Write: Key " + key + " Value " + value + " Size " + size);
            return key;
        } finally {
            lock.unlock();
        }
    }

    private Slab selectBestFitSlab(int size) throws Exception {
        for (var slab :
                slabs) {
            if (slab.getChunkSize() >= size) {
                return slab;
            }
        }

        throw new Exception("Invalid Size!");
    }

    public void logger(LogType logType) {
        try {
            lock.lock();
            System.out.println("---[" + logType + "]---");
            for (Slab slab : slabs) {
                System.out.println(slab);
            }

            System.out.println("=====================================");
            System.out.flush();
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Slab> getSlabs() {
        return slabs;
    }

}
