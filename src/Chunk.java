import java.util.Date;

public class Chunk {
    private final Date expirationDate;
    private final int size;
    private int data;

    public Chunk(int size, int data) {
        this.size = size;
        this.data = data;
        expirationDate = new Date(new Date().getTime() + 10_000);
    }
    public boolean isExpired(){
        Date currentTime = new Date();
        return currentTime.getTime() >= expirationDate.getTime();
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return  "" + data;
    }
}
