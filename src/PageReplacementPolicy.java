public interface PageReplacementPolicy {
    void updateListOnRead(String key);
    String selectSwapCandidate();
    void updateListOnWrite(String key);
    void updateListOnOverwrite(String key);
    void removeKey(String key);
    String toString();
}
