public class ChunkAddress {
    private final int pageIndex;
    private final int offset;

    public ChunkAddress(int pageIndex, int offset) {
        this.pageIndex = pageIndex;
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}