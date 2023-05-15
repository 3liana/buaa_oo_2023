import java.util.ArrayList;

public class Routes {
    private ArrayList<Integer> floors;
    private ArrayList<Integer> eles;

    public Routes() {
        floors = new ArrayList<Integer>();
        eles = new ArrayList<Integer>();
    }

    public void addFlr(int floor) {
        floors.add(floor);
    }

    public void addEle(int ele) {
        eles.add(ele);
    }

    public ArrayList<Integer> getFloors() {
        return floors;
    }

    public ArrayList<Integer> getEles() {
        return eles;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("floors: ");
        for (int i : floors) {
            sb.append(" ");
            sb.append(i);
        }
        sb.append(" else: ");
        for (int i : eles) {
            sb.append(" ");
            sb.append(i);
        }
        return sb.toString();
    }
}
