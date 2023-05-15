public class MyInt {
    private int num;

    public MyInt() {
        this.num = 0;
    }

    public MyInt(int num) {
        this.num = num;
    }

    public synchronized void addOne() {
        notifyAll();
        num++;
    }

    public synchronized void subOne() {
        notifyAll();
        num--;
    }

    public synchronized int getNum() {
        notifyAll();
        return num;
    }

    public synchronized boolean equalZero() {
        notifyAll();
        return num == 0;
    }

    public String toString() {
        return "has" + num;
    }
}
