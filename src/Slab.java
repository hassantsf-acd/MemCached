import java.util.*;
import java.util.Map.Entry;

public class Slab {
    private final ArrayList<Page> pages;
    private final Map<String, ChunkAddress> pageTable;
    private final PageReplacementPolicy replacementPolicy;
    private final int chunkSize;

    public Slab(int numberOfPages, PageReplacementPolicy replacementPolicy, int chunkSize) {
        this.chunkSize = chunkSize;
        this.pages = initPages(numberOfPages);
        this.pageTable = new HashMap<>();
        this.replacementPolicy = replacementPolicy;
    }

    private ArrayList<Page> initPages(int numberOfPages) {
        var pages = new ArrayList<Page>();
        for (int i = 0; i < numberOfPages; i++) {
            pages.add(new Page(getNumberOfChunks()));
        }

        return pages;
    }

    public List<Entry<String, ChunkAddress>> getPageTableEntries() {
        return new ArrayList<>(pageTable.entrySet());
    }

    public Chunk getChunk(ChunkAddress address) {
        var page = pages.get(address.getPageIndex());
        return page.getChunk(address.getOffset());
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public int getNumberOfChunks() {
        return Page.SIZE / chunkSize;
    }

    public boolean contains(String key) {
        return pageTable.containsKey(key);
    }

    public int read(String key) {
        var address = pageTable.get(key);
        replacementPolicy.updateListOnRead(key);

        return getChunk(address).getData();
    }

    private ChunkAddress getFreeChunkAddress() {
        for (int i = 0; i < pages.size(); i++) {
            if (pages.get(i).isEmpty()) {
                var freeOffset = pages.get(i).getIndexOfFreeChunk();
                return new ChunkAddress(i, freeOffset);
            }
        }

        return null;
    }

    public String write(int value, int size) {
        var key = UUID.randomUUID().toString();
        var address = getFreeChunkAddress();

        if (address == null) {
            var swapCandidateKey = replacementPolicy.selectSwapCandidate();
            address = pageTable.get(swapCandidateKey);
            pageTable.remove(swapCandidateKey);
            replacementPolicy.updateListOnOverwrite(key);

        } else {
            replacementPolicy.updateListOnWrite(key);
        }
        pageTable.put(key, address);

        var page = pages.get(address.getPageIndex());
        page.writeChunk(value, size, address.getOffset());
        return key;
    }
    
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Slab with chunk size ");
        output.append(chunkSize);
        output.append(" and ");
        output.append(replacementPolicy.toString());
        output.append(" : ");
        output.append(System.getProperty("line.separator"));

        for (int i = 0; i < pages.size(); i++) {
            output.append(i + 1);
            output.append(" : ");
            output.append(pages.get(i).toString());
            output.append(System.getProperty("line.separator"));
        }
        return output.toString();

    }

    public void clearChunk(String key) {
        var address = pageTable.get(key);
        var page = pages.get(address.getPageIndex());
        page.clearChunk(address.getOffset());
        pageTable.remove(key);
        replacementPolicy.removeKey(key);
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

}
