import java.util.Arrays;

public class Page {
    private final Chunk[] chunks;
    public final static int SIZE;

    static {
        SIZE = (int)Math.pow(2, 10);
    }

    public Page(int numberOfChunks) {
        this.chunks = new Chunk[numberOfChunks];
    }


    public Chunk getChunk(int index) {
        return chunks[index];
    }

    public int getIndexOfFreeChunk() {
        for (int i = 0; i < chunks.length; i++) {
            if (chunks[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public boolean isEmpty() {
        return getIndexOfFreeChunk() != -1;
    }

    public void writeChunk(int value, int size, int offset) {
        chunks[offset] = new Chunk(size, value);
    }
    public void clearChunk(int index){
        chunks[index] = null;
    }

    @Override
    public String toString() {
        return "[" +
                "chunks = " + Arrays.toString(chunks) +
                ']';
    }

    public Chunk[] getChunks() {
        return chunks;
    }
}
