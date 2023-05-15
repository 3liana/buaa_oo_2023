import java.util.ArrayList;

public class Accessable {
    private ArrayList<Integer> eleIndexs;

    public Accessable() {
        eleIndexs = new ArrayList<Integer>();
    }

    public void addIndex(int index) {
        eleIndexs.add(index);
    }

    public Accessable(int i) {
        this.eleIndexs = new ArrayList<Integer>();
        eleIndexs.add(i);
    }

    public Accessable(int i, int j) {
        this.eleIndexs = new ArrayList<Integer>();
        eleIndexs.add(i);
        eleIndexs.add(j);
    }

    public Accessable(int i, int j, int k) {
        this.eleIndexs = new ArrayList<Integer>();
        eleIndexs.add(i);
        eleIndexs.add(j);
        eleIndexs.add(k);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("this is an Accessable Elevatot list");
        for (Integer i : eleIndexs) {
            sb.append(" " + i);
        }
        return sb.toString();
    }

    public int firstIndex() {
        return this.eleIndexs.get(0);
    }

    public int lastIndex() {
        return this.eleIndexs.get(eleIndexs.size() - 1);
    }

    public ArrayList<Integer> getEle() {
        return this.eleIndexs;
    }
}
