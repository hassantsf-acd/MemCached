import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        var slabs = new ArrayList<Slab>();
        ReentrantLock lock = new ReentrantLock();
        ReentrantLock listLock = new ReentrantLock();

        slabs.add(new Slab(2, new LRU(), (int) Math.pow(2, 8)));
        slabs.add(new Slab(2, new LRU(), (int) Math.pow(2, 7)));
        var memCached = new MemCached(slabs, lock);
        var list = new ArrayList<String>();
        Writer writer = new Writer(memCached, list, listLock);
        Reader reader = new Reader(memCached, list, listLock);
        PullingThread pullingThread = new PullingThread(memCached, lock);
        new Thread(writer).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(reader).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(pullingThread).start();



        /*
        try {
            var key1 = memCached.write(1, 2);
            memCached.logger();
            Thread.sleep(2000);
            var key2 = memCached.write(2, 2);
            memCached.logger();
            Thread.sleep(2000);
            var key3 = memCached.write(3, 2);
            memCached.logger();
            Thread.sleep(2000);
            var key4 = memCached.write(4, 2);
            memCached.logger();
            Thread.sleep(2000);
            var key5 = memCached.write(5, 2);
            memCached.logger();
            Thread.sleep(2000);
            var key6= memCached.write(6, 256);
            memCached.logger();
            Thread.sleep(2000);
            memCached.write(7, 256);
            memCached.logger();
            Thread.sleep(2000);
            memCached.write(8, 2);
            memCached.logger();
            Thread.sleep(2000);
            memCached.write(9, 2);
            memCached.logger();
            Thread.sleep(2000);
            memCached.read(key6);
            memCached.logger();
            Thread.sleep(2000);

        } catch (Exception exception) {
            System.out.println(exception);
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        */
    }
}
