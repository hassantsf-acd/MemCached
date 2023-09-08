import java.util.LinkedList;

public class LRU implements PageReplacementPolicy {
    private final LinkedList<String> accessList;

    public LRU() {
        this.accessList = new LinkedList<>();
    }

    @Override
    public void updateListOnRead(String key) {
        for (int i = 0; i < accessList.size(); i++) {
            if (accessList.get(i).equals(key)) {
                accessList.remove(i);
                break;
            }
        }

        accessList.addFirst(key);
    }

    @Override
    public String selectSwapCandidate() {
        return accessList.getLast();
    }

    @Override
    public void updateListOnWrite(String key) {
        accessList.addFirst(key);
    }

    @Override
    public void updateListOnOverwrite(String key) {
        accessList.remove(selectSwapCandidate());
        accessList.addFirst(key);
    }

    @Override
    public void removeKey(String key) {
        accessList.remove(key);
    }

    @Override
    public String toString() {
        return "LRU{" +
                "accessList=" + accessList +
                '}';
    }
}
